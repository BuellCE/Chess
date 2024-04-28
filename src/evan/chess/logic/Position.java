package evan.chess.logic;

public class Position {

	public int x;
	public int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (other instanceof Position) {
			Position otherPosition = (Position) other;
			if (otherPosition.x == this.x && otherPosition.y == this.y) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return x + " " + y;
	}
	
}
