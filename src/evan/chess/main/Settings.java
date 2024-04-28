package evan.chess.main;

import java.awt.Color;

import javax.swing.JFrame;

public interface Settings {

	//Window Settings
	public static final String TITLE = "Chess - By Evan Buell";
	public static final int WINDOW_SIZE_PER_SLOT = 100;
	public static final int DEFAULT_CLOSE_OPERATION = JFrame.EXIT_ON_CLOSE;
	public static final boolean ALLOW_RESIZING = false;
	
	//Game Settings
	
	public static final char RESET_GAME_CONTROL = ' ';
	
	public static final Color LIGHT_SQUARE_COLOR = new Color(220, 200, 180);
	public static final Color DARK_SQUARE_COLOR = new Color(150, 130, 110);
	public static final Color POSSIBLE_MOVES_COLOR = new Color (60,190,255);
	public static final Color SELECTED_PIECE_COLOR = new Color(150, 130, 250);
	
	public static final int WHITE_PLAYER_ID = 1;
	public static final int BLACK_PLAYER_ID = -1;
	
	public static final int KNIGHT_WORTH = 60;
	public static final int BISHOP_WORTH = 60;
	public static final int ROOK_WORTH = 100;
	public static final int QUEEN_WORTH = 180;
	public static final int KING_WORTH = 2000;
	
	public static final int BOARD_SIZE = 8;
	//board layout should have an x and y size of board_size
	//Positional scores for each piece should also be the same size.
	public static final int[][] DEFAULT_BOARD_LAYOUT = 
		{{-2,-3,-4,-5,-6,-4,-3,-2},
		 {-1,-1,-1,-1,-1,-1,-1,-1},
		 { 0, 0, 0, 0, 0, 0, 0, 0},
		 { 0, 0, 0, 0, 0, 0, 0, 0},
		 { 0, 0, 0, 0, 0, 0, 0, 0},
		 { 0, 0, 0, 0, 0, 0, 0, 0},
		 { 1, 1, 1, 1, 1, 1, 1, 1},
		 { 2, 3, 4, 5, 6, 4, 3, 2}
	};
	
}
