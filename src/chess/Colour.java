/**************************************************************
 * @author Andrew Fraser
 * 
 * An enum colour used to represent whether a piece is white
 * or black.
 *************************************************************/

package chess;

public enum Colour {
	WHITE, BLACK;
	
	@Override
	public String toString() {
		if(this == Colour.WHITE) {
			return "White";
		}
		return "Black";
	}
}
