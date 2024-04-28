package evan.chess.logic.pieces;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import evan.chess.logic.Board;
import evan.chess.logic.Position;
import evan.chess.main.Settings;

public abstract class PlayingPiece extends Object {
	
	private int owner;
	private int pieceId;
	private Position location;
	private boolean hasMoved;
	public ArrayList<Position> limitedMovement;
	
	public PlayingPiece(int owner, int pieceId, Position location) {
		this.owner = owner;
		this.pieceId = pieceId;
		this.location = location;
		this.hasMoved = false;
		limitedMovement = new ArrayList<Position>();
	}
	
	public abstract ArrayList<Position> generateMoves(Board board);
	public abstract ArrayList<Position> generatePressuredPositions(Board board, int playerToPressure);
	public abstract BufferedImage getImageOfPiece();
	public abstract int getWorth();

	
	public boolean isMovePossible(Board board, Position move) {
		if (!limitedMovement.isEmpty()) {
			
			for (Position pos : limitedMovement) {
				if (pos.equals(move)) {
					return true;
				}
			}
			return false;
		}
		
		if (board.isSquareEmpty(move.x, move.y)) {
			return true;
			
		}else if (board.isSquareAWhitePiece(move.x, move.y) != isAWhitePiece()) { //an enemy piece
			
			return true;
		}
		return false;
	}
	
	public ArrayList<Position> generateLinearPositions(int[][] offsets, Board board){
		ArrayList<Position> generatedMoves = new ArrayList<Position>();
		
		for (int[] offset : offsets) {
			Position move = new Position(getPosition().x, getPosition().y);
			while(true) {
				move.x += offset[0];
				move.y += offset[1];
				if (!board.isInBounds(move.x, move.y)) {
					break;
				}
				if (isMovePossible(board, move)) {
					generatedMoves.add(new Position(move.x, move.y));
				}
				if (!board.isSquareEmpty(move.x, move.y)) {
					break;
				}
			}
		}
		
		return generatedMoves;
	}
	
	
	public ArrayList<Position> generateLinearPressuredPositions(int[][] offsets, Board board) {
		
		ArrayList<Position> generatedMoves = new ArrayList<Position>();
		
		for (int[] offset : offsets) {
			Position move = new Position(getPosition().x, getPosition().y);
			PlayingPiece pressureKingPiece = null; //TEST
			while(true) {
				move.x += offset[0];
				move.y += offset[1];
				
				if (pressureKingPiece != null) {
					if (!board.isInBounds(move.x, move.y)) {
						break;
					}
					if (!board.isSquareEmpty(move.x, move.y)) {
						if (board.getEnemyKing(getOwner()).getPosition().equals(move)) {
							
							pressureKingPiece.limitedMovement.add(getPosition());
							Position kingPos = board.getEnemyKing(getOwner()).getPosition();
							int x = getPosition().x;
							int y = getPosition().y;
							while(kingPos.x != x || kingPos.y != y) {
								pressureKingPiece.limitedMovement.add(new Position(x, y));
								if (x < kingPos.x) {
									x++;
								}else if (x > kingPos.x) {
									x--;
								}
								if (y < kingPos.y) {
									y++;
								}else if (y > kingPos.y) {
									y--;
								}
							}
							
							break;
						}else {
							break;
						}
					}else {
						continue;
					}
				}
				
				if (board.isSquareEmpty(move.x, move.y)) {
					generatedMoves.add(new Position(move.x, move.y));
					
				}else if (board.isInBounds(move.x, move.y)) { //an enemy or ally piece
					
					generatedMoves.add(new Position(move.x, move.y));
					
					Position enemyKing = board.getEnemyKing(getOwner()).getPosition();
					
					if (enemyKing.x != move.x || enemyKing.y != move.y) {
						
						PlayingPiece piece = board.getPieceAt(move.x, move.y); //TEST
						
						if (piece.getOwner() != getOwner()) { //TEST
							pressureKingPiece = piece; //TEST
						}else {
							break;
						}
						
					}
				}else { //an ally piece (or off the board)
					break;
				}
			}
		}
		
		return generatedMoves;
		
	}
	
	public ArrayList<Position> generateSingularPositions(int[][] offsets, Board board) {
		ArrayList<Position> generatedMoves = new ArrayList<Position>();
		for (int[] offset : offsets) {
			Position move = new Position(getPosition().x + offset[0], getPosition().y + offset[1]);
			
			if (isMovePossible(board, move)) {
				generatedMoves.add(new Position(move.x, move.y));
			}
			
		}
		
		return generatedMoves;
	}
	
	
	public ArrayList<Position> generateSingularPressuredPositions(int[][] offsets, Board board) {
		ArrayList<Position> generatedMoves = new ArrayList<Position>();
		
		for (int[] offset : offsets) {
			Position move = new Position(getPosition().x + offset[0], getPosition().y + offset[1]);
			if (board.isInBounds(move.x, move.y)) {
				generatedMoves.add(move);
			}
		}
		
		return generatedMoves;
	}
	
	
	@Override
	public PlayingPiece clone() {
		return null;
	}
	
	public void setLocation(Position location) {
		this.location = location;
	}

	public boolean isAWhitePiece() {
		return owner == Settings.WHITE_PLAYER_ID;
	}
	
	public boolean isABlackPiece() {
		return owner == Settings.BLACK_PLAYER_ID;
	}
	
	public int getOpposingPlayer() {
		if (isABlackPiece()) {
			return Settings.WHITE_PLAYER_ID;
		}else {
			return Settings.BLACK_PLAYER_ID;
		}
	}
	
	public int getOwner() {
		return owner;
	}

	public Position getPosition() {
		return location;
	}
	
	public void movedTo(Position p) {
		location = p;
		hasMoved = true;
	}
	
	public boolean hasMovedBefore() {
		return hasMoved;
	}

	public int getPieceId() {
		return pieceId;
	}

}
