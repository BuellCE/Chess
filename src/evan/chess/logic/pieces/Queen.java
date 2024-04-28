package evan.chess.logic.pieces;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import evan.chess.logic.Board;
import evan.chess.logic.Position;
import evan.chess.main.Settings;

public class Queen extends PlayingPiece{

	public static final int PIECE_ID = 5;
	
	public static final int PIECE_SCORE = 180;
	public static final int[][] POSITIONAL_SCORES = 
		{{-2,-1,0,0,0,0,-1,-2},
		 {-1,0,0,0,0,0,0,-1},
		 {-1,0,0,0,0,0,0,-1},
		 {0,0,0,1,1,0,0,0},
		 {0,0,0,1,1,0,0,0},
		 {-1,0,0,0,0,0,0,-1},
		 {-1,0,0,0,0,0,0,-1},
		 {-2,-1,-1,0,0,-1,-1,-2}
	};
	
	private static final int[][] OFFSETS = {
		{1,1}, {1,-1}, {-1,1}, {-1,-1}, {1,0}, {-1,0}, {0,1}, {0,-1}
	};
	
	private static BufferedImage whiteImage;
	private static BufferedImage blackImage;
	
	public Queen(int owner, Position location) {
		super(owner, PIECE_ID, location);
	}

	static {
		File black = new File("src/ChessPieces/BlackQueen.png");
		File white = new File("src/ChessPieces/WhiteQueen.png");
		try {   
			whiteImage = ImageIO.read(white);
			blackImage = ImageIO.read(black);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public ArrayList<Position> generateMoves(Board board) {
		return super.generateLinearPositions(OFFSETS, board);
	}
	
	@Override
	public ArrayList<Position> generatePressuredPositions(Board board, int playerToPressure) {
		return super.generateLinearPressuredPositions(OFFSETS, board);
	}

}



//ArrayList<Position> generatedMoves = new ArrayList<Position>();
//
//for (int[] offset : OFFSETS) {
//	Position move = new Position(getPosition().x, getPosition().y);
//	while(true) {
//		move.x += offset[0];
//		move.y += offset[1];
//		if (!board.isInBounds(move.x, move.y)) {
//			break;
//		}
//		if (super.isMovePossible(board, move)) {
//			generatedMoves.add(new Position(move.x, move.y));
//		}
//		if (!board.isSquareEmpty(move.x, move.y)) {
//			System.out.println("C");
//			break;
//		}
/////////////
//		//TEST
//		if (!limitedMovement.isEmpty()) {
//			
//			for (Position pos : limitedMovement) {
//				if (pos.equals(move)) {
//					generatedMoves.add(new Position(move.x, move.y));
//				}
//			}
//			continue;
//		}
//		//TEST
//		
//		
//		if (board.isSquareEmpty(move.x, move.y)) {
//			generatedMoves.add(new Position(move.x, move.y));
//			
//		}else if (board.isSquareAWhitePiece(move.x, move.y) != isAWhitePiece()) { //an enemy piece
//			
//			generatedMoves.add(new Position(move.x, move.y));
//			break;
//		}else { //an ally piece (or off the board)
//			break;
//		}
//	}
//}
//
//return generatedMoves;



