package evan.chess.logic;

public class Move {

	private Position from;
	private Position to;
	
	private Move additionalMove;
	
	public Move(Position from, Position to) {
		this.setFrom(from);
		this.setTo(to);
	}

	public Position getFrom() {
		return from;
	}

	public void setFrom(Position from) {
		this.from = from;
	}

	public Position getTo() {
		return to;
	}

	public void setTo(Position to) {
		this.to = to;
	}
	
	public Move getAdditionalMove() {
		return additionalMove;
	}

	public void setAdditionalMove(Move additionalMove) {
		this.additionalMove = additionalMove;
	}
	
	@Override 
	public Move clone() {
		Move newMove = new Move(from, to);
		if (additionalMove != null) {
			newMove.additionalMove = additionalMove.clone();
		}
		return newMove;
	}
	
	@Override
	public boolean equals(Object other) {
		
		if (other != null && other instanceof Move) {
			Move otherMove = (Move) other;
			return to.equals(otherMove.getTo()) && from.equals(otherMove.getFrom());
		}
		return false;
	}

}
