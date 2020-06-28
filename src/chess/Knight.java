package chess;

import java.util.ArrayList;

/*********************************************************
 * 
 * Knight piece for the Chess game. Inherits from Piece.
 * 
 * @author Andrew Fraser
 *
 ********************************************************/
public class Knight extends Piece {
	
	/**
	 * Creates a knight based upon the passed location and colour.
	 * @param location The location of the knight to be created
	 * @param colour The colour of the knight to be created
	 */
	public Knight(Coordinate location, Colour colour, ChessGui engine) {
		super(location, colour, engine, 3);
	}

	@Override
	public ArrayList<Coordinate> canThreaten() {
		ArrayList<Coordinate> coors = new ArrayList<Coordinate>();
		// all possible knight moves
		coors.add(new Coordinate(this.getLocation().getX() + 2, this.getLocation().getY() + 1));
		coors.add(new Coordinate(this.getLocation().getX() + 2, this.getLocation().getY() - 1));
		coors.add(new Coordinate(this.getLocation().getX() - 2, this.getLocation().getY() + 1));
		coors.add(new Coordinate(this.getLocation().getX() - 2, this.getLocation().getY() - 1));
		coors.add(new Coordinate(this.getLocation().getX() + 1, this.getLocation().getY() + 2));
		coors.add(new Coordinate(this.getLocation().getX() + 1, this.getLocation().getY() - 2));
		coors.add(new Coordinate(this.getLocation().getX() - 1, this.getLocation().getY() + 2));
		coors.add(new Coordinate(this.getLocation().getX() - 1, this.getLocation().getY() - 2));
		
		
		verifyBounds(coors);
		return coors;
	}

}
