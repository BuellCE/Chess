package evan.chess.logic.pieces;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import evan.chess.logic.Board;
import evan.chess.logic.Move;
import evan.chess.logic.Position;
import evan.chess.main.Settings;

public class King extends PlayingPiece{

	public static final int PIECE_ID = 6;
	
	public static final int PIECE_SCORE = 2000;
	public static final int[][] POSITIONAL_SCORES = 
		{{-4,-4,-4,-4,-4,-4,-4,-4},
		 {-4,-4,-4,-4,-4,-4,-4,-4},
		 {-4,-4,-4,-4,-4,-4,-4,-4},
		 {-4,-4,-4,-4,-4,-4,-4,-4},
		 {-3,-3,-3,-3,-3,-3,-3,-3},
		 {-2,-2,-2,-2,-2,-2,-2,-2},
		 { 1, 1, 0, 0, 0, 0, 1, 1},
		 { 3, 3, 1, 0, 0, 1, 3, 3}
	};
	
	//All the moves a king can make
	private static final int[][] OFFSETS = {
		{1,1}, {1,-1}, {-1,1}, {-1,-1}, {1,0}, {-1,0}, {0,1}, {0,-1}
	};
	
	private static BufferedImage whiteImage;
	private static BufferedImage blackImage;
	
	public boolean canCastleLeft = false;
	public boolean canCastleRight = false;
	
	public King(int owner, Position location) {
		super(owner, PIECE_ID, location);
		if (whiteImage == null || blackImage == null) {
			initializeImages();
		}
		
	}
	
	private void initializeImages() {
		File black = new File("src/ChessPieces/BlackKing.png");
		File white = new File("src/ChessPieces/WhiteKing.png");
		try {   
			whiteImage = ImageIO.read(white);
			blackImage = ImageIO.read(black);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getWorth() {
		int score = PIECE_SCORE;
		Position p = getPosition();
		if (getOwner()==Settings.WHITE_PLAYER_ID) {
			score += POSITIONAL_SCORES[p.x][p.y];
		}else {
			score += POSITIONAL_SCORES[Settings.BOARD_SIZE - p.x - 1][Settings.BOARD_SIZE - p.y - 1];
		}
		
		return score;
	}
	
	
	@Override
	public BufferedImage getImageOfPiece() {
		if (isAWhitePiece()) {
			return whiteImage;
		}else {
			return blackImage;
		}
	}
	
	@Override
	public ArrayList<Move> generateMoves(Board board) {
		
		canCastleLeft = false;
		canCastleRight = false;
		
		ArrayList<Move> generatedMoves = new ArrayList<Move>();
		W: for (int[] offset : OFFSETS) {
			Position move = new Position(getPosition().x + offset[0], getPosition().y + offset[1]);
			
			if (!board.isInBounds(move.x, move.y)) {
				continue;
			}
			PlayingPiece pieceAtP = board.getPieceAt(move);
			
			if (pieceAtP == null || pieceAtP.getOwner() != getOwner()) {
				for (Move m : board.getEnemyMoves(getOwner())) {
					if (m.getTo().x == move.x && m.getTo().y == move.y) {
						continue W;
					}
				}
				
				generatedMoves.add(new Move(this.getPosition(), move));
			}
			
			
		}
		
		//Castling 
		if (!hasMovedBefore()) {
			Position index = new Position(getPosition().x, getPosition().y);
			index.y = 0;
			PlayingPiece rook = board.getPieceAt(index);
			if (rook != null && board.getPieceAt(index) instanceof Rook) {
				if (rook.getOwner() == getOwner() && !rook.hasMovedBefore()) {
					if (board.isSquareEmpty(index.x, index.y + 1) 
							&& board.isSquareEmpty(index.x, index.y + 2) 
							&& board.isSquareEmpty(index.x, index.y + 3)) {
						Position move = new Position(getPosition().x, getPosition().y - 2);
						
						for (Move m : board.getEnemyMoves(getOwner())) {
							if (m.getTo().x == move.x && m.getTo().y == move.y) {
								move = null;
								break;
							}
						}
						if (move != null) {
							canCastleLeft = true;
							Move castleLeft = new Move(this.getPosition(), move);
							Position rookPosition = rook.getPosition();
							Position newRookPosition = new Position(rookPosition.x, rookPosition.y + 3);
							Move rookMoveInCastle = new Move(rookPosition, newRookPosition);
							castleLeft.setAdditionalMove(rookMoveInCastle);
							generatedMoves.add(castleLeft);
						}
						
					}
				}
			}
			index.y = Settings.BOARD_SIZE - 1;
			rook = board.getPieceAt(index);
			if (rook != null && board.getPieceAt(index) instanceof Rook) {
				if (rook.getOwner() == getOwner() && !rook.hasMovedBefore()) {
					if (board.isSquareEmpty(index.x, index.y - 1) 
							&& board.isSquareEmpty(index.x, index.y - 2)) {
						Position move = new Position(getPosition().x, getPosition().y + 2);
						
						for (Move m : board.getEnemyMoves(getOwner())) {
							if (m.getTo().x == move.x && m.getTo().y == move.y) {
								move = null;
								break;
							}
						}
						if (move != null) {
							canCastleLeft = false;
							Move castleRight = new Move(this.getPosition(), move);
							Position rookPosition = rook.getPosition();
							Position newRookPosition = new Position(rookPosition.x, rookPosition.y - 2);
							Move rookMoveInCastle = new Move(rookPosition, newRookPosition);
							castleRight.setAdditionalMove(rookMoveInCastle);
							generatedMoves.add(castleRight);
						}
						
					}
				}
			}
		}
		
		
		return generatedMoves;
	}

	@Override
	public ArrayList<Position> generatePressuredPositions(Board board, int playerToPressure) {
		return super.generateSingularPressuredPositions(OFFSETS, board);
	}

}



