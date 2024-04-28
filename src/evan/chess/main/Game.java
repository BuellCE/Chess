package evan.chess.main;

import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import evan.chess.graphics.GameVisual;
import evan.chess.logic.GameController;

//singleton class to ensure only one instance of the game exists
@SuppressWarnings("serial")
public class Game extends JPanel{

	private static Game window;
	
	private GameController gameController;
	private GameVisual gameVisuals;
	private JFrame gameFrame;
	private int window_size;
	
	private Game(GridBagLayout gridBagLayout){
		super(gridBagLayout);
		window_size = Settings.WINDOW_SIZE_PER_SLOT * Settings.BOARD_SIZE;
		gameFrame = new JFrame();
		gameController = new GameController(this);
		gameVisuals = new GameVisual();
		addControlListeners();
		initializeGameWindow();
	}
	
	public static Game getGameInstance() {
		if (window == null) {
			window = new Game(new GridBagLayout());
		}
		return window;
	}
	
	private void initializeGameWindow() {
		gameFrame.setSize(window_size + 13, window_size + 35);
		gameFrame.setTitle(Settings.TITLE);
		gameFrame.setDefaultCloseOperation(Settings.DEFAULT_CLOSE_OPERATION);
		gameFrame.setResizable(Settings.ALLOW_RESIZING);
		gameFrame.getContentPane().add(this);
	}
	
	private void addControlListeners() {
		gameFrame.addMouseListener(gameController.getControls().getMouseControls());
		gameFrame.addKeyListener(gameController.getControls().getKeyboardControls());
	}
	
	public GameController getGameController() {
		return gameController;
	}
	
	public void startGame() {
		gameFrame.setVisible(true);
	}
	
	public void exitGame() {
		gameFrame.dispose();
	}
	

	//called whenever graphics need to be refreshed
    @Override
    public void paintComponent(Graphics graphics) {
    	super.paintComponent(graphics);
    	gameVisuals.paintBackground(graphics);
    	gameVisuals.paintPossibleMovements(graphics, gameController.getSelectedPiece(), gameController.getPossibleMoves());
    	gameVisuals.paintGamePieces(graphics, gameController.getBoard().getRawBoard());
    	if (gameController.isGameOver()) {
    		gameVisuals.paintGameOverText(graphics);
    	}
    }

    
	
}
