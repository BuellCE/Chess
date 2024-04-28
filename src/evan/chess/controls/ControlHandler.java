package evan.chess.controls;

import evan.chess.logic.GameController;
import evan.chess.logic.Position;
import evan.chess.main.Settings;

public class ControlHandler {

	private KeyboardControls keyboardControls;
	private MouseControls mouseControls;
	private GameController controller;
	
	public ControlHandler(GameController controller) {
		this.controller = controller;
		this.mouseControls = new MouseControls(this);
		this.keyboardControls = new KeyboardControls(this);
	}
	
	public void playerPressedMouse(int xPosition, int yPosition) {
		
		//determine which space was clicked
		int colClicked = (xPosition - 9) / Settings.WINDOW_SIZE_PER_SLOT;
		int rowClicked = (yPosition - 31) / Settings.WINDOW_SIZE_PER_SLOT;
		controller.selectPieceAtLocation(new Position(rowClicked, colClicked));
	}
	
	public void playerReleaseMouse(int xPosition, int yPosition) {
		
		//determine which space was clicked
		int colClicked = (xPosition - 9) / Settings.WINDOW_SIZE_PER_SLOT;
		int rowClicked = (yPosition - 31) / Settings.WINDOW_SIZE_PER_SLOT;
		controller.dropPieceAtLocation(new Position(rowClicked, colClicked));
	}
	
	
	private void resetGameButtonPressed() {
		controller.resetGamePressed();
	}
	
	public void playerPressedKeyboard(char key) {
		if (key == Settings.RESET_GAME_CONTROL) {
			System.out.println("RESET");
			resetGameButtonPressed();
		}
	}
	
	public MouseControls getMouseControls() {
		return mouseControls;
	}
	
	public KeyboardControls getKeyboardControls() {
		return keyboardControls;
	}
	
}
