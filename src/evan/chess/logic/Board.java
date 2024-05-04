package evan.chess.logic;

import java.util.ArrayList;

import evan.chess.logic.pieces.Bishop;
import evan.chess.logic.pieces.King;
import evan.chess.logic.pieces.Knight;
import evan.chess.logic.pieces.Pawn;
import evan.chess.logic.pieces.PlayingPiece;
import evan.chess.logic.pieces.Queen;
import evan.chess.logic.pieces.Rook;
import evan.chess.main.Settings;

public class Board {

	private PlayingPiece[][] board;
	private ArrayList<Move> whiteMoves;
	private ArrayList<Move> blackMoves;
	
	private King whiteKing;
	private King blackKing;
	private boolean isCheckmate = false;
	private int checkmatedPlayer = 0;
	private Move[] previousWhiteMoves;
	private Move[] previousBlackMoves;
	
	
	public Board() {
		previousBlackMoves = new Move[2];
		previousWhiteMoves = new Move[2];
		board = new PlayingPiece[Settings.BOARD_SIZE][Settings.BOARD_SIZE];
	}
	
	public void makeAMove(Move move, int movingPlayerId) {
		setPieceAt(move.getTo(), getPieceAt(move.getFrom()));
		
		Move additionalMove = move.getAdditionalMove();
		if (additionalMove != null) {
			setPieceAt(additionalMove.getTo(), getPieceAt(additionalMove.getFrom()));
			setPieceAt(additionalMove.getFrom(), null);
		}
		
		
		PlayingPiece piece = setPieceAt(move.getFrom(), null);
		

		Move[] prevMoves = getPreviousMovesOfPlayer(movingPlayerId);
		prevMoves[1] = prevMoves[0];
		prevMoves[0] = move;
		
		if (piece instanceof Pawn) {
			checkIfPawnShouldBeQueen(move.getTo(), (Pawn) piece);
		}
		
		generateLegalMoves(movingPlayerId);
	}
	
	public void setBoardToDefaultLayout() {
		checkmatedPlayer = 0;
		isCheckmate = false;
		for (int x = 0; x < Settings.BOARD_SIZE; x++) {
			for (int y = 0; y < Settings.BOARD_SIZE; y++) {
				PlayingPiece piece = null;
				int playerId = Settings.WHITE_PLAYER_ID;
				int pieceId = Settings.DEFAULT_BOARD_LAYOUT[x][y];
				Position piecePosition = new Position(x, y);
				if (pieceId < 0) {
					pieceId = -pieceId;
					playerId = Settings.BLACK_PLAYER_ID;
				}
				switch(pieceId) {
					case Pawn.PIECE_ID: piece = new Pawn(playerId, piecePosition); break;
					case Rook.PIECE_ID: piece = new Rook(playerId, piecePosition); break;
					case Knight.PIECE_ID: piece = new Knight(playerId, piecePosition); break;
					case Bishop.PIECE_ID: piece = new Bishop(playerId, piecePosition); break;
					case Queen.PIECE_ID: piece = new Queen(playerId, piecePosition); break;
					case King.PIECE_ID:{
						King king = new King(playerId, piecePosition);
						if (playerId == Settings.WHITE_PLAYER_ID) {
							whiteKing = king;
						}else {
							blackKing = king;
						}
						piece = king; 
						break;
					}
				}
				board[x][y] = piece;
			}
		}
		generateLegalMoves(Settings.BLACK_PLAYER_ID);
	}
	
	
	public int getEnemyId(int id) {
		if (id == Settings.WHITE_PLAYER_ID) {
			return Settings.BLACK_PLAYER_ID;
		}else {
			return Settings.WHITE_PLAYER_ID;
		}
	}
	
	private void generateLegalMoves(int pressuringPlayer) {
		
		whiteMoves = new ArrayList<Move>();
		blackMoves = new ArrayList<Move>();
		
		int movingPlayer = getEnemyId(pressuringPlayer);
		King movingKing = getKing(movingPlayer);
		ArrayList<PlayingPiece> piecesPuttingKingInCheck = new ArrayList<PlayingPiece>();
		
		for (int x = 0; x < Settings.BOARD_SIZE; x++) {
			for (int y = 0; y < Settings.BOARD_SIZE; y++) {
				PlayingPiece p = board[x][y];
				if (p == null) {
					continue;
				}
				p.limitedMovement.clear();
			}
		}
		
		for (int x = 0; x < Settings.BOARD_SIZE; x++) {
			for (int y = 0; y < Settings.BOARD_SIZE; y++) {
				PlayingPiece p = board[x][y];
				if (p == null) {
					continue;
				}
				if (p.getOwner() == pressuringPlayer) {
					for (Position pos : p.generatePressuredPositions(this, 1)) {
						getAllyMoves(pressuringPlayer).add(new Move(p.getPosition(), pos));
					}
				}
			}
		}
		
		for (int x = 0; x < Settings.BOARD_SIZE; x++) {
			for (int y = 0; y < Settings.BOARD_SIZE; y++) {
				PlayingPiece p = board[x][y];
				if (p == null) {
					continue;
				}
				if (p.getOwner() == movingPlayer) {
					for (Move move : p.generateMoves(this)) {
						Position pos = move.getTo();
						if (!isInBounds(pos.x, pos.y)) {
							continue;
						}
						Move[] previous = getPreviousMovesOfPlayer(movingPlayer);
						if (move.equals(previous[1])) {
							if (previous[0].getFrom().equals(pos)) {
								if (previous[0].getTo().equals(move.getFrom())) {
									continue;
								}
							}
						}
						
						if (p.limitedMovement.isEmpty()) {
							getAllyMoves(movingPlayer).add(move);
						}else {
							for (Position limited : p.limitedMovement) {
								if (pos.equals(limited)) {
									getAllyMoves(movingPlayer).add(move);
									break;
								}
							}
						}
					}
				}else {
					for (Move move : p.generateMoves(this)) {
						Position pos = move.getTo();
						if (pos.equals(movingKing.getPosition())) {
							piecesPuttingKingInCheck.add(p);
						}
					}
				}
			}
		}
		
		
		if (piecesPuttingKingInCheck.size() >= 2) {
			//if there are two pieces putting the king in check, and the king can't move, then the game is over
			
			ArrayList<Move> kingMoves = getKing(movingPlayer).generateMoves(this);
			if (kingMoves.size() == 0) {
				isCheckmate = true;
			}else {
				getAllyMoves(movingPlayer).clear();
				for (Move move : kingMoves) {
					getAllyMoves(movingPlayer).add(move);
				}
			}
			
		}else if (piecesPuttingKingInCheck.size() == 1) {
			PlayingPiece piece = piecesPuttingKingInCheck.get(0);
			ArrayList<Position> blockingCheckPositions = new ArrayList<Position>();
			blockingCheckPositions.add(piece.getPosition()); //killing the piece that put king in check

			Position line = movingKing.getPosition();
			if (!(piece instanceof Knight)) {
				Position attackerPosition = piece.getPosition();
				int x = line.x;
				int y = line.y;

				while(x != attackerPosition.x || y != attackerPosition.y) {
					if (x < attackerPosition.x) {
						x++;
					}else if (x > attackerPosition.x) {
						x--;
					}
					if (y < attackerPosition.y) {
						y++;
					}else if (y > attackerPosition.y) {
						y--;
					}
					blockingCheckPositions.add(new Position(x,y));
				}
			}

			ArrayList<Move> newMoves = new ArrayList<Move>();
			
			for (Position blockingLocations : blockingCheckPositions) {
				for (Move move : getAllyMoves(movingPlayer)) {
					if (move.getTo().equals(blockingLocations) && !move.getFrom().equals(getKing(movingPlayer).getPosition())) {
						newMoves.add(move);
					}
				}
			}
			for (Move kingMoves : movingKing.generateMoves(this)) {
				newMoves.add(kingMoves);
			}
			getAllyMoves(movingPlayer).clear();
			getAllyMoves(movingPlayer).addAll(newMoves);

			if (newMoves.isEmpty()) {
				isCheckmate = true;
			}

		}
		
		if (isCheckmate) {
			checkmatedPlayer = movingPlayer;
		}
		
		if (getAllyMoves(movingPlayer).isEmpty()) {
			isCheckmate = true;
		}
		
	}
	
	public Move[] getPreviousMovesOfPlayer(int playerId) {
		if (playerId == Settings.WHITE_PLAYER_ID) {
			return previousWhiteMoves;
		}else {
			return previousBlackMoves;
		}
	}
	
	public Board copyBoard() {
		Board boardCopy = new Board();
		
		for (int i = 0; i < board.length; i++) {
			for (int x = 0; x < board[i].length; x++) {
				PlayingPiece piece = board[i][x];
				PlayingPiece newPiece = null;
				if (piece != null) {
					switch(piece.getPieceId()) {
					case Pawn.PIECE_ID: newPiece = new Pawn(piece.getOwner(), piece.getPosition()); break;
					case Rook.PIECE_ID: newPiece = new Rook(piece.getOwner(), piece.getPosition()); break;
					case Knight.PIECE_ID: newPiece = new Knight(piece.getOwner(), piece.getPosition()); break;
					case Bishop.PIECE_ID: newPiece = new Bishop(piece.getOwner(), piece.getPosition()); break;
					case Queen.PIECE_ID: newPiece = new Queen(piece.getOwner(), piece.getPosition()); break;
					case King.PIECE_ID:
						King king = new King(piece.getOwner(), piece.getPosition());
						if (piece.getOwner() == Settings.WHITE_PLAYER_ID) {
							boardCopy.whiteKing = king;
						}else {
							boardCopy.blackKing = king;
						}
						newPiece = king; 
						break;
					}
					boardCopy.board[i][x] = newPiece;
				}
			}
		}
		
		boardCopy.blackMoves = blackMoves;
		boardCopy.whiteMoves = whiteMoves;
		
		boardCopy.blackKing = (King) getKing(Settings.BLACK_PLAYER_ID);
		boardCopy.whiteKing = (King) getKing(Settings.WHITE_PLAYER_ID);
		
		
		return boardCopy;
	}
	
	public boolean isInBounds(int x, int y) {
		if (x < 0 || x >= Settings.BOARD_SIZE || y < 0 || y >= Settings.BOARD_SIZE) {
			return false;
		}
		return true;
	}
	
	public boolean isSquareAWhitePiece(int x, int y) {
		if (isInBounds(x, y) && board[x][y] != null && board[x][y].isAWhitePiece()) {
			return true;
		}
		return false;
	}
	
	public boolean isSquareABlackPiece(int x, int y) {
		if (isInBounds(x, y) && board[x][y] != null && board[x][y].isABlackPiece()) {
			return true;
		}
		return false;
	}
	
	public boolean isSquareEmpty(int x, int y) {
		if (isInBounds(x, y) && board[x][y] == null) {
			return true;
		}
		
		return false;
	}
	
	public int getScoreFromBoard() {
		int score = 0;
		
		if (checkmatedPlayer == Settings.WHITE_PLAYER_ID) {
			return 10000;
		}else if (checkmatedPlayer == Settings.BLACK_PLAYER_ID) {
			return -10000;
		}
		
		for (int x = 0; x < Settings.BOARD_SIZE; x++) {
			for (int y = 0; y < Settings.BOARD_SIZE; y++) {
				PlayingPiece p = board[x][y];
				if (p == null) {
					continue;
				}
				if (p.getOwner() == Settings.WHITE_PLAYER_ID) {
					score -= p.getWorth();
				}else {
					score += p.getWorth();
				}
			}
		}

		return score;
	}
	
	public PlayingPiece getPieceAt(int x, int y) {
		return board[x][y];
	}
	
	public PlayingPiece getPieceAt(Position p) {
		return board[p.x][p.y];
	}
	
	
	public int getPieceIdAt(int x, int y) {
		return board[x][y].getPieceId();
	}
	
	public int getPieceIdAt(Position p) {
		return board[p.x][p.y].getPieceId();
	}
	
	public ArrayList<Move> getBlackMoves(){
		return blackMoves;
	}
	
	public ArrayList<Move> getWhiteMoves(){
		return whiteMoves;
	}
	
	public boolean isCheckMate() {
		return isCheckmate;
	}
	
	public PlayingPiece setPieceAt(Position p, PlayingPiece toPiece) {
		PlayingPiece currentPiece = board[p.x][p.y];
		board[p.x][p.y] = toPiece;
		if (toPiece != null) {
			toPiece.movedTo(p);
		}
		return currentPiece;
	}
	
	public PlayingPiece[][] getRawBoard() {
		return board;
	}

	public King getEnemyKing(int playerId) {
		if (playerId == Settings.BLACK_PLAYER_ID) {
			return whiteKing;
		}else if (playerId == Settings.WHITE_PLAYER_ID) {
			return blackKing;
		}
		return null;
	}
	
	public King getKing(int playerId) {
		if (playerId == Settings.BLACK_PLAYER_ID) {
			return blackKing;
		}else if (playerId == Settings.WHITE_PLAYER_ID) {
			return whiteKing;
		}
		return null;
	}
	
	public ArrayList<Move> getAllyMoves(int playerId) {
		if (playerId == Settings.BLACK_PLAYER_ID) {
			return blackMoves;
		}else if (playerId == Settings.WHITE_PLAYER_ID) {
			return whiteMoves;
		}
		return null;
	}
	
	public ArrayList<Move> getEnemyMoves(int playerId) {
		if (playerId == Settings.BLACK_PLAYER_ID) {
			return whiteMoves;
		}else if (playerId == Settings.WHITE_PLAYER_ID) {
			return blackMoves;
		}
		return null;
	}
	
	private void checkIfPawnShouldBeQueen(Position pos, Pawn pawn) {
		if (pawn.isAtEndOfBoard()) {
			Position pawnPos = pawn.getPosition();
			setPieceAt(pawnPos, new Queen(pawn.getOwner(), pawnPos));
		}
	}
	
	@SuppressWarnings("unused")
	private static int addScore(int amount, int playerId) {
		int score = 0;
		
		return score;
	}


	
}
