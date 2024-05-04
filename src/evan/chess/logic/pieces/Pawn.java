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

public class Pawn extends PlayingPiece{

	public static final int PIECE_ID = 1;
	
	public static final int PIECE_SCORE = 20;
	public static final int[][] POSITIONAL_SCORES = 
		{{30,30,30,30,30,30,30,30},
		 {9,9,9,9,9,9,9,9},
		 {2,2,3,4,4,3,2,2},
		 {1,1,2,3,3,2,1,1},
		 {0,1,1,2,2,1,1,0},
		 {1,0,0,0,0,0,0,1},
		 {0,0,0,0,0,0,0,0},
		 {0,0,0,0,0,0,0,0}
	};
	
	private static BufferedImage whiteImage;
	private static BufferedImage blackImage;
	
	private int movingDirection = 0;
	
	public Pawn(int owner, Position location) {
		super(owner, PIECE_ID, location);
		
		if (owner == Settings.WHITE_PLAYER_ID) {
			movingDirection = -1;
		}else if (owner == Settings.BLACK_PLAYER_ID) {
			movingDirection = 1;
		}
		
		if (whiteImage == null || blackImage == null) {
			initializeImages();
		}
	}
	
	private void initializeImages() {
		File black = new File("src/ChessPieces/BlackPawn.png");
		File white = new File("src/ChessPieces/WhitePawn.png");
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
	
	public boolean isAtEndOfBoard() {
		int x = getPosition().x + movingDirection;
		if (x < 0 || x >= Settings.BOARD_SIZE) {
			return true;
		}
		return false;
	}
	
	@Override
	public ArrayList<Move> generateMoves(Board board) {
		ArrayList<Move> generatedMoves = new ArrayList<Move>();

		Position upLeft = new Position(getPosition().x + movingDirection, getPosition().y - 1);
		Position upRight = new Position(getPosition().x + movingDirection, getPosition().y + 1);
		
		if (!limitedMovement.isEmpty()) {

			if (board.isInBounds(upLeft.x, upLeft.y)) {
				for (Position pos : limitedMovement) {
					if (pos.equals(upLeft)) {
						generatedMoves.add(new Move(this.getPosition(), upLeft));
					}
				}
			}
			
			if (board.isInBounds(upRight.x, upRight.y)) {
				for (Position pos : limitedMovement) {
					if (pos.equals(upRight)) {
						generatedMoves.add(new Move(this.getPosition(), upRight));
					}
				}
			}
			
			return generatedMoves;
		}
		
		Position up = new Position(getPosition().x + movingDirection, getPosition().y);
		if (board.isSquareEmpty(up.x, up.y)) {
			generatedMoves.add(new Move(this.getPosition(), up));
		}
		if (!hasMovedBefore()) {
			Position up2 = new Position(getPosition().x + (movingDirection * 2), getPosition().y);
			if (board.isSquareEmpty(up.x, up.y) && board.isSquareEmpty(up2.x, up2.y)) {
				generatedMoves.add(new Move(this.getPosition(), up2));
			}
		}
		
		if (!board.isSquareEmpty(upRight.x, upRight.y) && board.isSquareAWhitePiece(upRight.x, upRight.y) != isAWhitePiece()) {
			generatedMoves.add(new Move(this.getPosition(), upRight));
		}
		if (!board.isSquareEmpty(upLeft.x, upLeft.y) && board.isSquareAWhitePiece(upLeft.x, upLeft.y) != isAWhitePiece()) {
			generatedMoves.add(new Move(this.getPosition(), upLeft));
		}
		
		return generatedMoves;
	}

	@Override
	public ArrayList<Position> generatePressuredPositions(Board board, int playerToPressure) {
		
		return super.generateSingularPressuredPositions(new int[][] {{movingDirection, 1},{movingDirection, -1}}, board);
		
	}

}
