package evan.chess.logic;

public class MoveScore {

	private Move move;
	private int score;
	
	public MoveScore(Move move, int score) {
		this.setMove(move);
		this.setScore(score);
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}
