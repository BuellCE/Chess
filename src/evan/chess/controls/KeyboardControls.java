package evan.chess.controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardControls implements KeyListener{

	private ControlHandler handler;
	
	public KeyboardControls(ControlHandler handler){
		this.handler = handler;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		handler.playerPressedKeyboard(e.getKeyChar());
	}

	
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}
