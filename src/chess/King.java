package chess;

import java.util.ArrayList;
import java.util.HashMap;

/*********************************************************
 * 
 * King piece for the Chess game. Inherits from Piece.
 * 
 * @author Andrew Fraser
 *
 ********************************************************/
public class King extends Piece {
	
	/**
	 * Creates a king based upon the passed location and colour.
	 * @param location The location of the king to be created
	 * @param colour The colour of the king to be created
	 */
	public King(Coordinate location, Colour colour, ChessGui engine) {
		super(location, colour, engine);
	}

	@Override
	public ArrayList<Coordinate> canThreaten() {
		ArrayList<Coordinate> coors = new ArrayList<Coordinate>();
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(!isBlocked(i + location.getX(), j + location.getY())) {
					coors.add(new Coordinate(this.getLocation().getX() + i, this.getLocation().getY() + j));
				}
			}
		}
		verifyBounds(coors);
		return coors;
	}
	
	@Override
	public ArrayList<Coordinate> canMove() {
		ArrayList<Coordinate> coors = super.canMove();
		
		if(!hasMoved) {
			if(canLeftCastle()) {
				coors.add(new Coordinate(location.getX() - 2, location.getY()));
			}
			if(canRightCastle()) {
				coors.add(new Coordinate(location.getX() + 2, location.getY()));
			}
		}
		return coors;
	}
	
	private boolean canLeftCastle() {
		HashMap<Coordinate, Piece> map = engine.getMap();
		if(engine.isCoorThreatened(location)) {
			return false;
		}
		for(int i = location.getX() - 1; i > 0; i--) {
			Coordinate currCoor = new Coordinate(i, location.getY());
			if(map.get(currCoor) != null) {
				return false;
			}
			if(engine.isCoorThreatened(currCoor)) {
				return false;
			}
		}
		Piece currRook = map.get(new Coordinate(0, location.getY()));
		if(currRook == null || !(currRook instanceof Rook) || currRook.getHasMoved()) {
			return false;
		}
		return true;
	}
	
	private boolean canRightCastle() {
		HashMap<Coordinate, Piece> map = engine.getMap();
		if(engine.isCoorThreatened(location)) {
			return false;
		}
		for(int i = location.getX() + 1; i < 7; i++) {
			Coordinate currCoor = new Coordinate(i, location.getY());
			if(map.get(currCoor) != null) {
				return false;
			}
			if(engine.isCoorThreatened(currCoor)) {
				return false;
			}
		}
		Piece currRook = map.get(new Coordinate(7, location.getY()));
		if(currRook == null || !(currRook instanceof Rook) || currRook.getHasMoved()) {
			return false;
		}
		return true;
	}
}
