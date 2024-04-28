package evan.chess.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import evan.chess.logic.Position;
import evan.chess.logic.pieces.PlayingPiece;
import evan.chess.main.Settings;

public class GameVisual {

	private int squareSize = 0;
	
	public GameVisual() {
		squareSize = (int) (Settings.WINDOW_SIZE_PER_SLOT * 0.98f);
	}
	
	public void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, Settings.WINDOW_SIZE_PER_SLOT * Settings.BOARD_SIZE, Settings.WINDOW_SIZE_PER_SLOT * Settings.BOARD_SIZE);
		int count = 0;
		for (int x = 0; x < Settings.BOARD_SIZE; x++) {
			count++;
			for (int y = 0; y < Settings.BOARD_SIZE; y++) {
				
				int startX = x * Settings.WINDOW_SIZE_PER_SLOT;
				int startY = y * Settings.WINDOW_SIZE_PER_SLOT;
				
				count++;
				if (count % 2 == 1) {
					graphics.setColor(Settings.DARK_SQUARE_COLOR);
				}else {
					graphics.setColor(Settings.LIGHT_SQUARE_COLOR);
				}
				
				graphics.fillRect(startX, startY, squareSize, squareSize);
			}
		}
	}

	public void paintGamePieces(Graphics graphics, PlayingPiece[][] rawBoard) {
		for (int x = 0; x < Settings.BOARD_SIZE; x++) {
			for (int y = 0; y < Settings.BOARD_SIZE; y++) {

				PlayingPiece pieceToRender = rawBoard[y][x];
				
				if (pieceToRender == null) {
					continue;
				}
				
				Image sprite = pieceToRender.getImageOfPiece();

				int startX = x * Settings.WINDOW_SIZE_PER_SLOT;
				int startY = y * Settings.WINDOW_SIZE_PER_SLOT + 3;

				graphics.drawImage(sprite, startX, startY, Settings.WINDOW_SIZE_PER_SLOT - 10, Settings.WINDOW_SIZE_PER_SLOT - 10, null);

			}
		}
	}
	

	public void paintPossibleMovements(Graphics graphics, Position selectedPiece, ArrayList<Position> moves) {
		if (moves == null || selectedPiece == null) {
			return;
		}
		graphics.setColor(Settings.SELECTED_PIECE_COLOR);
		graphics.fillRect(selectedPiece.y * Settings.WINDOW_SIZE_PER_SLOT, selectedPiece.x * Settings.WINDOW_SIZE_PER_SLOT, squareSize, squareSize);
		
		graphics.setColor(Settings.POSSIBLE_MOVES_COLOR);
		for (Position p : moves) {

			int startX = p.x * Settings.WINDOW_SIZE_PER_SLOT;
			int startY = p.y * Settings.WINDOW_SIZE_PER_SLOT;

			graphics.fillRect(startY, startX, squareSize, squareSize);
		}
	}
	
	public void paintGameOverText(Graphics graphics) {
		graphics.setColor(Color.CYAN);
		graphics.setFont(new Font("Aerial", 0, 85));
		graphics.drawString("Game Over", Settings.WINDOW_SIZE_PER_SLOT * 2, Settings.WINDOW_SIZE_PER_SLOT * 4);
		graphics.setFont(new Font("Aerial", 0, 50));
		graphics.drawString("Hit Space To Start New Game", Settings.WINDOW_SIZE_PER_SLOT * 1, Settings.WINDOW_SIZE_PER_SLOT * 5);
	}
	
	
}


