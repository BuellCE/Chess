package evan.chess.logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import evan.chess.controls.ControlHandler;
import evan.chess.logic.pieces.PlayingPiece;
import evan.chess.main.Game;
import evan.chess.main.Settings;

public class GameController {

	private Board gameBoard;
	
	private Game game;
	private ControlHandler controls;
	private GameAI cpu;
	private boolean isWhitesTurn;
	private boolean gameIsOver;
	private Timer timer;
	
	private Position selectedPiece;
	private ArrayList<Move> possibleMoves;
	
	
	public GameController(Game game) {
		this.game = game;
		
		gameBoard = new Board();
		
		controls = new ControlHandler(this);
		cpu = new GameAI(gameBoard);
		
		startNewGame();
	}
	
	public void makeAMove(Move move, int playerId) {
		
		if (gameIsOver) {
			return;
		}
		
		gameBoard.makeAMove(move, playerId);
		
		game.repaint();
		
		if (gameBoard.isCheckMate()) {
			endGame();
		}else {
			swapTurns();
		}
		
	}
	
	public void selectPieceAtLocation(Position p) {
		if (gameIsOver) {
			return;
		}
		if (isWhitesTurn) {
			if (gameBoard.isSquareAWhitePiece(p.x, p.y)) {
				PlayingPiece piece = gameBoard.getPieceAt(p);
				if (piece == null) {
					return;
				}
				ArrayList<Move> moves = new ArrayList<Move>();

				for (Move mo : gameBoard.getWhiteMoves()) {
					for (Move move : piece.generateMoves(gameBoard)) {
						Position possibleMoves = move.getTo();
						if (mo.getFrom().equals(p) && mo.getTo().equals(possibleMoves)) {
							moves.add(move);
						}
					}
				}
				possibleMoves = moves;
				
				selectedPiece = p;
				game.repaint();
			}
		}
	}
	
	
	public void dropPieceAtLocation(Position p) {
		if (selectedPiece != null && gameBoard.isInBounds(p.x, p.y)) {
			
			if (selectedPiece.x != p.x || selectedPiece.y != p.y) {
				
				for (Move move : possibleMoves) {
					Position possible = move.getTo();
					if (possible.x == p.x && possible.y == p.y) {
						makeAMove(move, Settings.WHITE_PLAYER_ID);
						break;
					}
				}
				
			}
		}
		
		possibleMoves = null;
		selectedPiece = null;
		game.repaint();
	}

	public void cpuAttemptMove(Move move) {
		makeAMove(move, Settings.BLACK_PLAYER_ID);
	}
	
	public void startNewGame() {
		isWhitesTurn = true;
		gameIsOver = false;
		if (timer != null) {
			timer.stop();
			timer = null;
		}
		gameBoard.setBoardToDefaultLayout();
		
		game.repaint();
	}
	
	public void resetGamePressed() {
		startNewGame();
	}
	
	public ControlHandler getControls() {
		return controls;
	}

	public Board getBoard() {
		return gameBoard;
	}
	
	public ArrayList<Move> getPossibleMoves(){
		return possibleMoves;
	}
	
	public Position getSelectedPiece() {
		return selectedPiece;
	}
	
	public boolean isGameOver() {
		return gameIsOver;
	}
	
	private void endGame() {
		if (gameIsOver == false) {
			gameIsOver = true;
		}
	}
	
	
	private void swapTurns() {
		if (isWhitesTurn) {
			isWhitesTurn = false;
			
			cpu.chooseNextMove(); //prepare the AI's next move, then wait 200ms before making it.
			
			timer = new Timer(200, new ActionListener(){    
				@Override
	            public void actionPerformed(ActionEvent e) {
					timer.stop();
					
					cpuAttemptMove(cpu.getNextMove());
					
	            }
	        });
			timer.start();
			
			
		}else{
			isWhitesTurn = true;
		}
	}
	
	
}




