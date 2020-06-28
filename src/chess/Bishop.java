package chess;

import java.util.ArrayList;

/*********************************************************
 * 
 * Bishop piece for the Chess game. Inherits from Piece.
 * 
 * @author Andrew Fraser
 *
 ********************************************************/
public class Bishop extends Piece {
	
	/**
	 * Creates a bishop based upon the passed location and colour.
	 * @param location The location of the bishop to be created
	 * @param colour The colour of the bishop to be created
	 */
	public Bishop(Coordinate location, Colour colour, ChessGui engine) {
		super(location, colour, engine, 3);
	}

	@Override
	public ArrayList<Coordinate> canThreaten() {
		ArrayList<Coordinate> coors = new ArrayList<Coordinate>();
		// all possible bishop moves
		coors.addAll(movePaths(1, 1));
		coors.addAll(movePaths(1, -1));
		coors.addAll(movePaths(-1, 1));
		coors.addAll(movePaths(-1, -1));
		verifyBounds(coors);
		return coors;
	}

}
