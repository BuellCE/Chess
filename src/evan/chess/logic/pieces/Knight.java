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

public class Knight extends PlayingPiece{

	public static final int PIECE_ID = 3;
	
	public static final int PIECE_SCORE = 60;
	public static final int[][] POSITIONAL_SCORES = 
		{{-3,-3,-3,-3,-3,-3,-3,-3},
		 {-3,-2, 0, 0, 0, 0,-2,-3},
		 { 0, 0, 1, 2, 2, 1, 0, 0},
		 { 0, 1, 2, 3, 3, 2, 1, 0},
		 { 0, 1, 2, 3, 3, 2, 1, 0},
		 { 0, 0, 1, 2, 2, 1, 0, 0},
		 {-3, 0, 0, 0, 0, 0, 0,-3},
		 {-3,-3,-2,-2,-2,-2,-3,-3}
	};
	
	//All the moves a knight can make
	private static final int[][] OFFSETS = {
		{2,1}, {2,-1}, {-2,1}, {-2,-1}, {1,2}, {-1,2}, {1,-2}, {-1,-2}
	};
	
	private static BufferedImage whiteImage;
	private static BufferedImage blackImage;
	
	public Knight(int owner, Position location) {
		super(owner, PIECE_ID, location);
		if (whiteImage == null || blackImage == null) {
			initializeImages();
		}
	}
	
	private void initializeImages() {
		File black = new File("src/ChessPieces/BlackKnight.png");
		File white = new File("src/ChessPieces/WhiteKnight.png");
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
		
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for (Position pos : super.generateSingularPositions(OFFSETS, board)) {
			moves.add(new Move(this.getPosition(), pos));
		}
		
		return moves;
		
		
	}

	@Override
	public ArrayList<Position> generatePressuredPositions(Board board, int playerToPressure) {
		return super.generateSingularPressuredPositions(OFFSETS, board);
	}

}



