package chess;

import java.util.ArrayList;

/*********************************************************
 * 
 * Queen piece for the Chess game. Inherits from Piece.
 * 
 * @author Andrew Fraser
 *
 ********************************************************/
public class Queen extends Piece {
	
	/**
	 * Creates a queen based upon the passed location and colour.
	 * @param location The location of the queen to be created
	 * @param colour The colour of the queen to be created
	 */
	public Queen(Coordinate location, Colour colour) {
		super(location, colour);
	}

	@Override
	public ArrayList<Coordinate> canThreaten() {
		ArrayList<Coordinate> coors = new ArrayList<Coordinate>();
		coors.addAll(movePaths(1, 1));
		coors.addAll(movePaths(1, -1));
		coors.addAll(movePaths(-1, 1));
		coors.addAll(movePaths(-1, -1));
		coors.addAll(movePaths(1, 0));
		coors.addAll(movePaths(0, 1));
		coors.addAll(movePaths(-1, 0));
		coors.addAll(movePaths(0, -1));
		return coors;
	}

}
