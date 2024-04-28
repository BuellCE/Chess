package evan.chess.main;

/*
TODO:
-Allow player to use both colors
-Fix Castling to move rook
*/
public class Application{
	
	public static void main(String[] args) {
		Game chess = Game.getGameInstance();
		chess.startGame();
	}
	
	
}
