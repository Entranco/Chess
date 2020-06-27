package chess;

import java.util.ArrayList;

/*********************************************************
 * 
 * Rook piece for the Chess game. Inherits from Piece.
 * 
 * @author Andrew Fraser
 *
 ********************************************************/
public class Rook extends Piece {
	
	/**
	 * Creates a rook based upon the passed location and colour.
	 * @param location The location of the rook to be created
	 * @param colour The colour of the rook to be created
	 */
	public Rook(Coordinate location, Colour colour, ChessGui engine) {
		super(location, colour, engine);
	}

	@Override
	public ArrayList<Coordinate> canThreaten() {
		ArrayList<Coordinate> coors = new ArrayList<Coordinate>();
		coors.addAll(movePaths(1, 0));
		coors.addAll(movePaths(0, 1));
		coors.addAll(movePaths(-1, 0));
		coors.addAll(movePaths(0, -1));
		verifyBounds(coors);
		return coors;
	}
	
}
