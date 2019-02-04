package chess;

import java.util.ArrayList;

/*********************************************************
 * 
 * Pawn piece for the Chess game. Inherits from Piece.
 * 
 * @author Andrew Fraser
 *
 ********************************************************/
public class Pawn extends Piece {

	/**
	 * Creates a pawn based upon the passed location and colour.
	 * @param location The location of the pawn to be created
	 * @param colour The colour of the pawn to be created
	 */
	public Pawn(Coordinate location, Colour colour) {
		super(location, colour);
	}

	
	@Override
	public ArrayList<Coordinate> canThreaten() {
		ArrayList<Coordinate> coors = new ArrayList<Coordinate>();
		int colorNum = 1;
		if(this.getColour().equals(Colour.BLACK)) {
			colorNum = -1;
		}
		
		// taking a piece diagonally right
		if(canCapture(1, colorNum))
			coors.add(new Coordinate(location.getX() + 1, location.getY() + colorNum));
		
		// taking a piece diagonally left
		if(canCapture(-1, colorNum))
			coors.add(new Coordinate(location.getX() - 1, location.getY() + colorNum));
		
		return coors;
	}
	
	@Override
	public ArrayList<Coordinate> canMove() {
		ArrayList<Coordinate> coors = super.canMove();
		
		int colorNum = 1;
		if(colour == Colour.BLACK)
			colorNum = -1;
		
		// moving forward
		if(!isMoveBlocked(0, 1 * colorNum))
			coors.add(new Coordinate(location.getX(), location.getY() + 1 * colorNum));
		
		//moving forward two squares for white
		if(colorNum == 1 && location.getY() == 1 && !isMoveBlocked(0, 2 * colorNum) && !isMoveBlocked(0, 1 * colorNum))
			coors.add(new Coordinate(location.getX(), location.getY() + 2 * colorNum));
		
		// moving forward two squares for black
		if(colorNum == -1 && location.getY() == 6 && !isMoveBlocked(0, 2 * colorNum) && !isMoveBlocked(0, 1 * colorNum))
			coors.add(new Coordinate(location.getX(), location.getY() + 2 * colorNum));
		
		return coors;
	}
	
	/**
	 * Determines whether or not the pawn is blocked by a certain square relative to itself to
	 * move forward.
	 * @param x The x distance from the pawn to check
	 * @param y The y distance from the pawn to check
	 * @return True if the pawn is blocked, false if it is not
	 */
	private boolean isMoveBlocked(int x, int y) {
		x += location.getX();
		y += location.getY();
		Piece destinationPiece = ChessGui.getMap().get(new Coordinate(x, y));
		if(destinationPiece != null  || x > 7 || x < 0 || y > 7 || y < 0)
			return true;
		return false;
	}
	
	/**
	 * Determines whether or not the pawn can capture at a square x length and y height
	 * away from itself.
	 * @param x The x distance from the pawn to check
	 * @param y The y distance from the pawn to check
	 * @return True if the pawn can capture at that location, false if not
	 */
	private boolean canCapture(int x, int y) {
		x += location.getX();
		y += location.getY();
		Piece destinationPiece = ChessGui.getMap().get(new Coordinate(x, y));
		boolean enPass = new Coordinate(x, y).equals(ChessGui.getEnPassant());
		if(destinationPiece != null || enPass) {
			return true;
		}
		return false;
	}

}
