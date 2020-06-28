package chess;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/*****************************************************************************************************
 * 
 * An abstract class used to represent the various kinds of chess pieces. Has a location, colour,
 * array of coordinates, and an icon.
 * 
 * @author Andrew Fraser
 * 
 ****************************************************************************************************/
public abstract class Piece {
	public Coordinate location;
	public final Colour colour;
	public final Icon icon;
	public final ChessGui engine;
	public boolean hasMoved;
	
	public Piece(Coordinate location, Colour colour, ChessGui engine) {
		this.location = location;
		this.colour = colour;
		this.engine = engine;
		hasMoved = false;
		icon = new ImageIcon((Piece.class.getResource("images/" + colour.toString() 
			+ getClass().getSimpleName() + ".png")));
	}
	
	/**
	 * Checks to see what Coordinates the piece can move to based upon its current Coordinate.
	 * @return The array of Coordinates that the piece can move to.
	 */
	public ArrayList<Coordinate> canMove() {
		ArrayList<Coordinate> coors = canThreaten();
		verifyColours(coors);
		engine.verifyMoveOptions(this, coors);
		return coors;
	}
	
	/**
	 * Checks to see what Coordinates are threatened by this piece. Holds most of the code used in the canMove method
	 * @return the array of Coordinates that the piece threatens
	 */
	public abstract ArrayList<Coordinate> canThreaten();
	
	/**
	 * @return the location
	 */
	public Coordinate getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Coordinate location) {
		this.location = location;
	}
	
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}

	/**
	 * @return the color
	 */
	public Colour getColour() {
		return colour;
	}
	
	public Icon getIcon() {
		return icon;
	}
	
	public boolean getHasMoved() {
		return hasMoved;
	}
	
	public void verifyBounds(ArrayList<Coordinate> coors) {
		ArrayList<Coordinate> removedCoors = new ArrayList<>();
		for(Coordinate el: coors) {
			if(el.getX() > 7 || el.getX() < 0 || el.getY() > 7 || el.getY() < 0) {
				removedCoors.add(el);
			}
		}
		for(Coordinate el: removedCoors) {
			coors.remove(el);
		}
	}
	
	public void verifyColours(ArrayList<Coordinate> coors) {
		ArrayList<Coordinate> removedCoors = new ArrayList<>();
		HashMap<Coordinate, Piece> map = engine.getMap();
		for(Coordinate el: coors) {
			Piece currPiece = map.get(el);
			if(currPiece != null && currPiece.colour.equals(colour))
				removedCoors.add(el);
		}
		for(Coordinate el: removedCoors) {
			coors.remove(el);
		}
	}
	
	protected boolean isBlocked(int x, int y) {
		Piece destinationPiece = engine.getMap().get(new Coordinate(x, y));
		if((destinationPiece != null && destinationPiece.getColour() == this.colour)  || x > 7 || x < 0 || y > 7 || y < 0)
			return true;
		return false;
	}
	
	protected ArrayList<Coordinate> movePaths(int xMod, int yMod) {
		ArrayList<Coordinate> localCoors = new ArrayList<Coordinate>();
		HashMap<Coordinate, Piece> map = engine.getMap();
		Coordinate testCoor = new Coordinate(location.getX(), location.getY());
		while(!isBlocked(testCoor.getX() + xMod, testCoor.getY() + yMod)) {
			testCoor.setX(testCoor.getX() + xMod);
			testCoor.setY(testCoor.getY() + yMod);
			localCoors.add(new Coordinate(testCoor.getX(), testCoor.getY()));
			if(map.get(testCoor) != null)
				break;
		}
		return localCoors;
	}
	
	public String toString() {
		return this.getClass().getSimpleName() + " " + this.getColour() + " (" + this.getLocation().getX() + "," + this.getLocation().getY() + ")";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colour == null) ? 0 : colour.hashCode());
		result = prime * result + ((icon == null) ? 0 : icon.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		if (colour != other.colour)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}
}