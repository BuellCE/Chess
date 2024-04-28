package evan.chess.controls;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

public class MouseControls implements MouseListener{

	private ControlHandler handler;
	
	public MouseControls(ControlHandler handler){
		this.handler = handler;
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			handler.playerPressedMouse(e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			handler.playerReleaseMouse(e.getX(), e.getY());
		}
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
