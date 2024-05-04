package evan.chess.logic;

import java.util.ArrayList;

import evan.chess.main.Settings;

public class GameAI {

	private Board board;
	
	@SuppressWarnings("unused")
	private int outcomeCount;
	private static final int AI_SEARCH_DEPTH = 3;
	private Move nextMove;
	
	public GameAI(Board board) {
		this.board = board;
	}
	
	public Move getNextMove() {
		return nextMove;
	}
	
	public void chooseNextMove() {
		
		outcomeCount = 0;
		
		MoveScore scoredMove = findBestOutcome(board, AI_SEARCH_DEPTH, Settings.BLACK_PLAYER_ID, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		nextMove = scoredMove.getMove();
		
	}
	
	public MoveScore findBestOutcome(Board board, int depth, int playerId, int alpha, int beta) {
		
		ArrayList<Move> moves = board.getAllyMoves(playerId);
		
		if (board.isCheckMate()) {
			outcomeCount++;
			return new MoveScore(null, board.getScoreFromBoard() + depth);
		}
		
		if (depth <= 0 || moves.isEmpty()) {
			outcomeCount++;
			int score =  board.getScoreFromBoard() + depth;
			return new MoveScore(null, score);
		}
		depth--;
		if (playerId == Settings.BLACK_PLAYER_ID) {
			int value = Integer.MIN_VALUE;
			Move bestMove = null;
			for (Move move : moves) {
				
				Board copyBoard = board.copyBoard();
				copyBoard.makeAMove(move, Settings.BLACK_PLAYER_ID);
				
				int val = findBestOutcome(copyBoard, depth, Settings.WHITE_PLAYER_ID, alpha, beta).getScore();

				if (val >= value) {
					bestMove = move;
					value = val;
				}
				
				alpha = Math.max(alpha, val);
				if (alpha > beta) {
					break;
				}
				
			}
			
			return new MoveScore(bestMove, value);
		}else {
			int value = Integer.MAX_VALUE;
			Move bestMove = null;
			for (Move move : moves) {
				
				Board copyBoard = board.copyBoard();
				
				copyBoard.makeAMove(move, Settings.WHITE_PLAYER_ID);
				
				int val = findBestOutcome(copyBoard, depth, Settings.BLACK_PLAYER_ID, alpha, beta).getScore();

				if (val <= value) {
					bestMove = move;
					value = val;
				}
				
				beta = Math.min(beta, val);
				if (alpha > beta) {
					break;
				}
				
			}
			return new MoveScore(bestMove, value);
		}
	}
	
	
}



