/*****************************************************************************
 * @author Andrew Fraser
 * 
 * Implements a GUI for a chess game. Also holds the main method and creates
 * the piece objects.
 ****************************************************************************/
package chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import java.awt.Toolkit;

public class ChessGui extends JFrame {

	private static final long serialVersionUID = -945515800065004758L;

	// Graphics
	private JPanel contentPane;
	private JLabel turnLabel;
	private JLayeredPane boardPanel;

	// Engine Data
	private JButton[][] buttons;
	private ArrayList<Piece> pieces;
	private Colour turn;
	private ArrayList<JLabel> moveOptions;
	private Piece pieceHolder;
	private Piece whiteKing;
	private Piece blackKing;
	private ArrayList<Coordinate> currMoveOptions;
	private HashMap<Coordinate, Piece> map;
	private JLabel[] capturedLabels;
	private int[][] piecesRemaining;
	private Coordinate enPassant;
	private Colour p1Colour;
	private boolean soloGame;
	private ChessAI ai;

	// Constants
	private static final int buttonSize = 98;
	private static boolean rotateScreen = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChessGui frame = new ChessGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws CloseGameException 
	 */
	public ChessGui() throws CloseGameException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ChessGui.class.getResource("images/WhitePawn.png")));
		setTitle("Chess");
		setFocusable(false);
		setVisible(true);

		// holds the options to either play again or close the program
		String[] options = { "1", "2" };
		// shows a new panel prompting the user to select a piece
		JOptionPane playerPanel = new JOptionPane();
		playerPanel.setBounds(100, 250, 200, 500);
		@SuppressWarnings("static-access")
		int returnValue = playerPanel.showOptionDialog(contentPane, "<html><br>How many players would like to play?</html>",
				"How many players?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(
						ChessGui.class.getResource("images/WhiteKing.png")),
				options, options[0]);

		// assumes that the user wants to close the program if the prompt window is
		// closed
		if (returnValue == JOptionPane.CLOSED_OPTION) {
			throw new CloseGameException();
		}
		
		
		soloGame = returnValue == 0;
		if(returnValue == 0) {
			JOptionPane colorPanel = new JOptionPane();
			colorPanel.setBounds(100, 250, 200, 500);
			Icon[] icons = new Icon[2];
			icons[0] = new ImageIcon(ChessGui.class.getResource("images/WhitePawn.png"));
			icons[1] = new ImageIcon(ChessGui.class.getResource("images/BlackPawn.png"));
			
			@SuppressWarnings("static-access")
			int colorValue = colorPanel.showOptionDialog(contentPane, "What color will you play?", "Choose your color!",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icons[0], icons, icons[0]);
			
			if (colorValue == JOptionPane.CLOSED_OPTION) {
				colorValue = 0;
			}
			
			p1Colour = colorValue == 0 ? Colour.WHITE : Colour.BLACK;
			ai = new GoodTradesAI(this, colorValue == 0 ? Colour.BLACK : Colour.WHITE);
		}

		// starts the game by launching the GUI and waiting for the user to act
		generatePieces();
		createContentPane();
		createinfoPanel();
		createBoardPanel();
		
		if(soloGame && p1Colour == Colour.BLACK) {
			moveAIPiece();
			rotateScreen();
		}
		createActionListeners();
	}

	private void generatePieces() {
		// initializes the necessary arrays and maps needed to store each piece's
		// location
		// throughout the game
		pieces = new ArrayList<Piece>();
		map = new HashMap<Coordinate, Piece>();
		moveOptions = new ArrayList<JLabel>();
		pieceHolder = null;
		// creates an array of pieces containing all of the pieces that start in a game
		// of chess
		Piece[][] pieceArray = new Piece[][] { { new King(new Coordinate(4, 0), Colour.WHITE, this),
				new Queen(new Coordinate(3, 0), Colour.WHITE, this), new Rook(new Coordinate(0, 0), Colour.WHITE, this),
				new Rook(new Coordinate(7, 0), Colour.WHITE, this),
				new Knight(new Coordinate(1, 0), Colour.WHITE, this),
				new Knight(new Coordinate(6, 0), Colour.WHITE, this),
				new Bishop(new Coordinate(2, 0), Colour.WHITE, this),
				new Bishop(new Coordinate(5, 0), Colour.WHITE, this),
				new Pawn(new Coordinate(0, 1), Colour.WHITE, this), new Pawn(new Coordinate(1, 1), Colour.WHITE, this),
				new Pawn(new Coordinate(2, 1), Colour.WHITE, this), new Pawn(new Coordinate(3, 1), Colour.WHITE, this),
				new Pawn(new Coordinate(4, 1), Colour.WHITE, this), new Pawn(new Coordinate(5, 1), Colour.WHITE, this),
				new Pawn(new Coordinate(6, 1), Colour.WHITE, this),
				new Pawn(new Coordinate(7, 1), Colour.WHITE, this) },
				{ new King(new Coordinate(4, 7), Colour.BLACK, this),
						new Queen(new Coordinate(3, 7), Colour.BLACK, this),
						new Rook(new Coordinate(0, 7), Colour.BLACK, this),
						new Rook(new Coordinate(7, 7), Colour.BLACK, this),
						new Knight(new Coordinate(1, 7), Colour.BLACK, this),
						new Knight(new Coordinate(6, 7), Colour.BLACK, this),
						new Bishop(new Coordinate(2, 7), Colour.BLACK, this),
						new Bishop(new Coordinate(5, 7), Colour.BLACK, this),
						new Pawn(new Coordinate(0, 6), Colour.BLACK, this),
						new Pawn(new Coordinate(1, 6), Colour.BLACK, this),
						new Pawn(new Coordinate(2, 6), Colour.BLACK, this),
						new Pawn(new Coordinate(3, 6), Colour.BLACK, this),
						new Pawn(new Coordinate(4, 6), Colour.BLACK, this),
						new Pawn(new Coordinate(5, 6), Colour.BLACK, this),
						new Pawn(new Coordinate(6, 6), Colour.BLACK, this),
						new Pawn(new Coordinate(7, 6), Colour.BLACK, this), } };
		// holds the white and black kings as fields for easy access
		whiteKing = pieceArray[0][0];
		blackKing = pieceArray[1][0];
		// adds each created piece to pieces, an ArrayList holding all remaining pieces,
		// and map, a HashMap mapping each coordinate to the piece on that coordinate
		for (Piece[] array : pieceArray) {
			for (Piece el : array) {
				pieces.add(el);
				map.put(el.getLocation(), el);
			}
		}
	}

	/**
	 * Creates the content pane for the chess game to take place on.
	 */
	private void createContentPane() {
		// creates the content pane, which contains the entire GUI for the game
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 926, 862);
		contentPane = new JPanel();
		contentPane.setFocusable(false);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	/**
	 * Creates the score panel for the chess game.
	 */
	private void createinfoPanel() {
		// creates the infoPanel, which holds all of the information going on in the
		// game
		JPanel infoPanel = new JPanel();
		infoPanel.setFocusable(false);
		contentPane.add(infoPanel, BorderLayout.WEST);
		infoPanel.setLayout(new GridLayout(3, 1, 0, 0));

		// creates the black panel, which holds all of the information for black's side
		// of the board
		JPanel BlackPanel = new JPanel();
		BlackPanel.setFocusable(false);
		infoPanel.add(BlackPanel);

		// creates the label holding all of the information regarding what black pieces
		// remain
		JLabel lblBlackRemaining = new JLabel(
				"<html>Black Remaining:<br>Pawns: 8<br>Knights: 2<br>Bishops: 2<br>" + "Rooks: 2<br>Queens: 1</html>");
		lblBlackRemaining.setFocusable(false);
		BlackPanel.add(lblBlackRemaining);

		// creates the panel holding whose turn is going on at any given time, and if
		// the current player is in check
		JPanel turnPanel = new JPanel();
		turnPanel.setFocusable(false);
		infoPanel.add(turnPanel);
		turnPanel.setLayout(new BorderLayout());

		// creates the label holding whose turn it is and displays a warning if the
		// current player is in check
		turnLabel = new JLabel();
		turnLabel.setFocusable(false);
		turnLabel.setText("<html>White's<br>Turn</html>");
		turnLabel.setFont(new Font("CopperPlate Gothic Light", Font.BOLD, 22));
		turnPanel.add(turnLabel, BorderLayout.CENTER);
		turn = Colour.WHITE;

		// creates the panel for holding all of the information concerning the white
		// pieces
		JPanel WhitePanel = new JPanel();
		WhitePanel.setFocusable(false);
		infoPanel.add(WhitePanel);
		WhitePanel.setLayout(null);

		// creates the label holding all of the information regarding which black pieces
		// remain
		JLabel lblWhiteRemaining = new JLabel(
				"<html>White Remaining:<br>Pawns: 8<br>Knights: 2<br>Bishops: 2<br>" + "Rooks: 2<br>Queens: 1</html>");
		lblWhiteRemaining.setBounds(0, 136, 102, 125);
		lblWhiteRemaining.setFocusable(false);
		WhitePanel.add(lblWhiteRemaining);

		// initializes an array to store the two labels for remaining pieces so they can
		// be
		// easily accessed and altered as pieces are removed from the game
		capturedLabels = new JLabel[2];
		capturedLabels[0] = lblWhiteRemaining;
		capturedLabels[1] = lblBlackRemaining;

		// initializes an array to hold the number of pieces remaining for each side
		piecesRemaining = new int[][] { { 8, 2, 2, 2, 1 }, { 8, 2, 2, 2, 1 } };
	}

	/**
	 * Creates the board panel for the chess game.
	 */
	private void createBoardPanel() {
		// creates the board panel, which holds the buttons that make up the board
		boardPanel = new JLayeredPane();
		boardPanel.setFocusable(false);
		contentPane.add(boardPanel, BorderLayout.CENTER);

		// sets the size for all buttons to be created
		// creates the buttons based on locations and boundaries
		buttons = new JButton[8][8];
		int x = 0;
		int y = buttonSize * 7;
		boolean white = false;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				buttons[j][i] = new JButton();
				buttons[j][i].setBounds(x, y, buttonSize, buttonSize);
				buttons[j][i].setFocusable(false);
				if (white) {
					buttons[j][i].setBackground(Color.WHITE);
				} else {
					buttons[j][i].setBackground(new Color(128, 0, 0));
				}
				boardPanel.add(buttons[j][i]);
				boardPanel.setLayer(buttons[j][i], 2);
				x += buttonSize;
				white = (!white);
			}
			y -= buttonSize;
			x = 0;
			white = (!white);
		}
		for (Piece el : pieces) {
			buttons[el.getLocation().getX()][el.getLocation().getY()].setIcon(el.getIcon());
		}
	}

	/**
	 * Creates the action listeners for each button on the chess board. Performs
	 * many actions for the chess game based upon the Piece class and various helper
	 * methods.
	 */
	private void createActionListeners() {
		for (JButton[] array : buttons) {
			for (JButton button : array) {
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						// if there is currently a piece selected
						if (pieceHolder != null) {
							// for each coordinate the piece can move to
							for (Coordinate coor : currMoveOptions) {
								// if the clicked button equals that coordinate, move the piece
								if (buttonToCoor(button).equals(coor)) {
									movePiece(coor);
									if(soloGame) {
										moveAIPiece();
									}
									break;
								}
							}
							// whether or not a piece is moved, the piece needs to be unselected and
							// move options should be cleared
							pieceHolder = null;
							clearMoveOptions();
						} else {
							// selects the piece that has been clicked on
							pieceHolder = map.get(buttonToCoor(button));
							// if the clicked square has no piece, clear the move options
							if (pieceHolder == null || !pieceHolder.getColour().equals(turn)) {
								pieceHolder = null;
								clearMoveOptions();
							} else
								generateMoveOptions();
						}
					}
				});
			}
		}
	}

	/**
	 * Clears all move option labels on the board.
	 */
	private void clearMoveOptions() {
		for (JLabel el : moveOptions)
			el.setVisible(false);

		moveOptions.clear();
	}

	/**
	 * Takes in a button and returns the respective coordinate that the button
	 * represents.
	 * 
	 * @param button The button to be converted to coordinates of type JButton
	 * @return The Coordinate relating to the passed JButton
	 */
	private Coordinate buttonToCoor(JButton button) {
		if ((turn.equals(Colour.BLACK) && rotateScreen) || soloGame && (p1Colour == Colour.BLACK))
			return new Coordinate(button.getX() / buttonSize, button.getY() / buttonSize);
		else
			return new Coordinate(button.getX() / buttonSize, 7 - (button.getY() / buttonSize));
	}

	/**
	 * Getter method for the pieces ArrayList<Piece> holding all of the pieces
	 * remaining in the game
	 * 
	 * @return pieces of type ArrayList<Piece>
	 */
	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	/**
	 * Getter method for the map HashMap<Coordinate, Piece> holding all of the
	 * coordinates that have been mapped to pieces
	 * 
	 * @return map of type HashMap<Coordinate, Piece>
	 */
	public HashMap<Coordinate, Piece> getMap() {
		return map;
	}

	/**
	 * Getter method for the enPassant Coordinate holding the coordinate that can be
	 * taken next turn by a pawn through en passant.
	 * 
	 * @return enPassant of type Coordinate
	 */
	public Coordinate getEnPassant() {
		return enPassant;
	}
	
	/**
	 * Sets the current piece to be moved, used in allowing the AI to move.
	 * @param piece
	 */
	public void setPieceHolder(Piece piece) {
		pieceHolder = piece;
	}

	/**
	 * Checks to see if the passed coordinate project is threatened by any enemy
	 * pieces.
	 * 
	 * @param coor The coordinate to check for being threatened
	 * @return True if the passed coordinate is threatened, false if not
	 */
	public boolean isCoorThreatened(Coordinate coor, Colour allyColour) {
		for (Piece piece : pieces) {
			if (!piece.getColour().equals(allyColour)) {
				for (Coordinate el : piece.canThreaten()) {
					if (el.equals(coor)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Moves the currently selected piece to the passed coordinate related to the
	 * passed button.
	 * 
	 * @param coor   The coordinate that the currently selected piece should be
	 *               moved to
	 * @param button The button relating to the passed coordinate
	 */
	public void movePiece(Coordinate coor) {
		// changes icons and map
		buttons[pieceHolder.getLocation().getX()][pieceHolder.getLocation().getY()].setIcon(null);
		changeCapturedLabels(map.get(coor), -1);
		pieces.remove(map.get(coor));
		map.remove(pieceHolder.getLocation());
		map.put(coor, pieceHolder);
		// checks that the movement wasn't a castle or en passant, and acts properly
		// if the movements were of those types
		checkForCastle();
		checkForEnPassant();
		allowForEnPassant();

		// changes location of piece and icon of destination
		pieceHolder.setLocation(coor);
		buttons[coor.getX()][coor.getY()].setIcon(pieceHolder.getIcon());
		pieceHolder.setHasMoved(true);
		// checks for promotions, swaps the turn, and checks for checkmate and stalemate
		checkForPromotion();
		swapTurn();
		checkForMates();
	}

	@SuppressWarnings("unchecked")
	/**
	 * Generates JLabels to indicate each move option that the currently selected
	 * piece has.
	 */
	private void generateMoveOptions() {
		int i = 0;
		// gets the current move options and verifies that none of them put the king in
		// check
		currMoveOptions = (ArrayList<Coordinate>) pieceHolder.canMove().clone();
		// adds icons for each legal move option on the board to show where the
		// currently
		// selected piece can move
		int turnColor = (turn.equals(Colour.BLACK) && rotateScreen) || (soloGame && (p1Colour == Colour.BLACK)) ? 0 : 7;
		while (i < currMoveOptions.size()) {
			moveOptions.add(new JLabel(new ImageIcon(ChessGui.class.getResource("images/MoveMarker.png"))));
			moveOptions.get(i).setBounds(
					(currMoveOptions.get(i).getX() * buttonSize + (int) (Math.round(buttonSize * 0.4))),
					(Math.abs(turnColor - currMoveOptions.get(i).getY()) * buttonSize
							+ (int) (Math.round(buttonSize * 0.4))),
					(int) (Math.round(buttonSize * 0.2)), (int) (Math.round(buttonSize * 0.2)));
			boardPanel.add(moveOptions.get(i));
			boardPanel.setLayer(moveOptions.get(i), 3);
			i++;
		}
	}

	/**
	 * Checks to make sure that each move that each move of the current piece does
	 * not result in the king being threatened. Removes all moves that do this from
	 * being legal moves.
	 */
	public void verifyMoveOptions(Piece currPiece, ArrayList<Coordinate> moveOps) {
		// gets the current piece's location and prepares to emulate moves
		Coordinate oldCoor = currPiece.getLocation();
		ArrayList<Coordinate> coorsToRemove = new ArrayList<Coordinate>();
		Piece oldPiece;
		// tests each move to see if it results in a threatened king, then
		// removes each move that results in such
		for (Coordinate moveOption : moveOps) {
			// emulates the piece moving to each move option
			oldPiece = emulateMove(currPiece, moveOption);
			// removes the move option from the list of current move options
			if (isCoorThreatened(getCurrKing().getLocation(), turn)) {
				coorsToRemove.add(moveOption);
			}

			unemulateMove(oldPiece, currPiece, oldCoor);
		}

		moveOps.removeAll(coorsToRemove);
	}
	
	/**
	 * Orders the current AI to move a piece
	 */
	public void moveAIPiece() {
		ai.makeMove();
		pieceHolder = null;
	}

	/**
	 * Emulates a move to determine whether or not it would result in check.
	 * 
	 * @param newCoor The coordinate to be moved to when emulating
	 * @return The piece that may have been removed during the emulation
	 */
	private Piece emulateMove(Piece currPiece, Coordinate newCoor) {
		// sets a piece's new location to the newCoor passed in
		newCoor = new Coordinate(newCoor.getX(), newCoor.getY());

		// removes a piece that was potentially captured
		Piece oldPiece = map.get(newCoor);
		pieces.remove(oldPiece);
		// maps the new location to the current piece
		map.remove(currPiece.getLocation());
		map.put(newCoor, currPiece);
		currPiece.setLocation(newCoor);
		return oldPiece;
	}

	/**
	 * Returns to the original position after emulating a move.
	 * 
	 * @param oldPiece The piece that was in the position the selected piece moved
	 *                 to
	 * @param oldCoor  The original location of the currently selected piece
	 */
	private void unemulateMove(Piece oldPiece, Piece currPiece, Coordinate oldCoor) {
		// sets the piece to its old coordinate and returns the potentially
		// captured piece to its original position
		oldCoor = new Coordinate(oldCoor.getX(), oldCoor.getY());
		map.put(currPiece.getLocation(), oldPiece);
		map.put(oldCoor, currPiece);
		currPiece.setLocation(oldCoor);
		if (oldPiece != null)
			pieces.add(oldPiece);
	}

	/**
	 * Returns the current king object based on whose turn it is.
	 * 
	 * @return The king on the side that is taking their turn
	 */
	private Piece getCurrKing() {
		return turn.equals(Colour.WHITE) ? whiteKing : blackKing;
	}

	/**
	 * Returns the current king object based on whose turn it is not.
	 * 
	 * @return The king on the side that isn't taking their turn
	 */
	private Piece getOtherKing() {
		return turn.equals(Colour.WHITE) ? blackKing : whiteKing;
	}

	/**
	 * Rotates the board 180 degrees whenever the turn is swapped, so the current
	 * player can view from their own perspective.
	 */
	private void rotateScreen() {
		for (JButton[] btns : buttons) {
			for (JButton btn : btns) {
				Rectangle rect = btn.getBounds();
				btn.setBounds((int) rect.getX(), (buttonSize * 7) - (int) rect.getY(), buttonSize, buttonSize);
			}
		}

		// sets a turnColor to decide which labels to swap to which
		int turnColor = turn.equals(Colour.WHITE) ? 1 : 0;

		// swaps the locations of the labels on the screen to align with the swapped
		// board angle
		capturedLabels[Math.abs(turnColor - 1)].setText("<html>White Remaining:<br> Pawns: " + piecesRemaining[0][0]
				+ "<br> Knights: " + piecesRemaining[0][1] + "<br> Bishops: " + piecesRemaining[0][2] + "<br> Rooks: "
				+ piecesRemaining[0][3] + "<br> Queens: " + piecesRemaining[0][4]);

		capturedLabels[turnColor].setText("<html>Black Remaining:<br> Pawns: " + piecesRemaining[1][0]
				+ "<br> Knights: " + piecesRemaining[1][1] + "<br> Bishops: " + piecesRemaining[1][2] + "<br> Rooks: "
				+ piecesRemaining[1][3] + "<br> Queens: " + piecesRemaining[1][4]);
	}

	/**
	 * Checks to see if a pawn was moved onto the back row. If so, a promotion
	 * prompt appears, and the player is asked if they want to promote to a knight,
	 * bishop, rook, or queen.
	 */
	private void checkForPromotion() {
		// promotion cannot occur if the last moved piece isn't a pawn
		if (!(pieceHolder instanceof Pawn)) {
			return;
		}
		// checks to make sure that the pawn moved to the correct position in order to
		// be promoted
		if (!(pieceHolder.getColour() == Colour.WHITE && pieceHolder.getLocation().getY() == 7)
				&& !(pieceHolder.getColour() == Colour.BLACK && pieceHolder.getLocation().getY() == 0)) {
			return;
		}

		// creates an array of possible pieces for promotion to show on the prompt's
		// labels
		Icon[] icons = new Icon[4];
		Piece[] possiblePromos = { new Knight(pieceHolder.getLocation(), pieceHolder.getColour(), this),
				new Bishop(pieceHolder.getLocation(), pieceHolder.getColour(), this),
				new Rook(pieceHolder.getLocation(), pieceHolder.getColour(), this),
				new Queen(pieceHolder.getLocation(), pieceHolder.getColour(), this) };
		// gets each possible promo's icon in an array of icons to show on the input
		// dialog
		for (int i = 0; i < 4; i++) {
			icons[i] = possiblePromos[i].getIcon();
		}
		// shows a new panel prompting the user to select a piece
		JOptionPane promoPanel = new JOptionPane();
		promoPanel.setBounds(100, 250, 200, 500);
		@SuppressWarnings("static-access")
		int returnValue = promoPanel.showOptionDialog(contentPane, "Choose your Promotion!", "Pawn Promotion",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, pieceHolder.getIcon(), icons, icons[3]);

		// if the panel is closed, the queen is picked for promotion automatically
		if (returnValue == JOptionPane.CLOSED_OPTION) {
			returnValue = 3;
		}

		// adds the newly promoted piece to the pieces array with the pawn's old
		// location, and removes
		// the pawn from the game
		Piece newPiece = possiblePromos[returnValue];
		map.put(pieceHolder.getLocation(), newPiece);
		buttons[pieceHolder.getLocation().getX()][pieceHolder.getLocation().getY()].setIcon(newPiece.getIcon());
		pieces.remove(pieceHolder);
		pieces.add(newPiece);
		newPiece.setHasMoved(true);
		changeCapturedLabels(pieceHolder, ++returnValue);
	}

	/**
	 * Checks to see if the last move taken by the king was a castle, then performs
	 * the proper move for the corresponding rook.
	 */
	private void checkForCastle() {
		// the last move cannot be a castle if the piece used wasn't a king or
		// the king has been moved before
		if (!(pieceHolder instanceof King) || pieceHolder.getHasMoved()) {
			return;
		}
		Coordinate currLocation = pieceHolder.getLocation();
		Piece rookToCastle;
		Coordinate newLocation;
		// sets the rookToCastle properly based on which rook the king moved towards
		if (pieceHolder == map.get(new Coordinate(currLocation.getX() - 2, currLocation.getY()))) {
			rookToCastle = map.get(new Coordinate(0, currLocation.getY()));
			newLocation = new Coordinate(currLocation.getX() - 1, currLocation.getY());
		} else if (pieceHolder == map.get(new Coordinate(currLocation.getX() + 2, currLocation.getY()))) {
			rookToCastle = map.get(new Coordinate(7, currLocation.getY()));
			newLocation = new Coordinate(currLocation.getX() + 1, currLocation.getY());

		} else {
			return;
		}
		// remove the rook from its previous location
		buttons[rookToCastle.getLocation().getX()][rookToCastle.getLocation().getY()].setIcon(null);
		map.remove(rookToCastle.getLocation());
		// add it to its new location
		map.put(newLocation, rookToCastle);
		rookToCastle.setLocation(newLocation);
		buttons[rookToCastle.getLocation().getX()][rookToCastle.getLocation().getY()].setIcon(rookToCastle.getIcon());
		rookToCastle.setHasMoved(true);
	}

	/**
	 * Checks if the most recent move was an en passant. If it was, deletes the
	 * piece that was "passed" by the en passant action.
	 */
	private void checkForEnPassant() {
		// enPassant cannot happen with non-pawn pieces, and also cannot happen if a
		// pawn didn't move
		// two squares the previous turn
		if (!(pieceHolder instanceof Pawn) || enPassant == null) {
			return;
		}

		// confirms that the pawn moved into the enPassant square
		if (pieceHolder.equals(map.get(enPassant))) {
			int turnColour = turn.equals(Colour.WHITE) ? -1 : 1;
			Piece pawnToPass = map.get(new Coordinate(enPassant.getX(), enPassant.getY() + turnColour));

			// remove the pawn to be passed
			buttons[pawnToPass.getLocation().getX()][pawnToPass.getLocation().getY()].setIcon(null);
			map.remove(pawnToPass.getLocation());
			pieces.remove(pawnToPass);
			changeCapturedLabels(pawnToPass, -1);
		}
	}

	/**
	 * If a pawn ever moves two squares, this method holds the coordinate it hopped
	 * over in the enPassant field. Then if a pawn tries to capture that certain
	 * square the next turn, it will be allowed.
	 */
	private void allowForEnPassant() {
		if (!(pieceHolder instanceof Pawn)) {
			enPassant = null;
			return;
		}

		// checks to see if the current pawn has moved two squares
		Coordinate currLocation = pieceHolder.getLocation();

		// checks to see if the pawn has moved two squares in one movement. If so, sets
		// the enPassant coordinate to the coordinate that can be captured by other
		// pawns
		// for the next turn.
		if (pieceHolder.equals(map.get(new Coordinate(currLocation.getX(), currLocation.getY() + 2)))) {
			enPassant = new Coordinate(currLocation.getX(), currLocation.getY() + 1);
		} else if (pieceHolder.equals(map.get(new Coordinate(currLocation.getX(), currLocation.getY() - 2)))) {
			enPassant = new Coordinate(currLocation.getX(), currLocation.getY() - 1);
		} else {
			enPassant = null;
		}
	}

	@SuppressWarnings("unchecked")
	private void checkForMates() {
		// checks every single move that a player can make. If a player cannot
		// move anywhere on their turn, this means that there is either a checkmate
		// or a stalemate.
		Piece oldHolder = pieceHolder;
		ArrayList<Piece> allPieces = (ArrayList<Piece>) pieces.clone();
		for (Piece el : allPieces) {
			if (el.getColour().equals(turn)) {
				pieceHolder = el;
				ArrayList<Coordinate> testOptions = pieceHolder.canMove();
				if (testOptions.size() != 0) {
					pieceHolder = oldHolder;
					return;
				}
			}
		}
		
		// Gets rid of move options for a clean-looking ending screen
		clearMoveOptions();
		
		Colour winner;
		if (turn == Colour.WHITE) {
			winner = Colour.BLACK;
		} else {
			winner = Colour.WHITE;
		}

		// creates a message stating if there was a stalemate or checkmate
		String message = checkForCheck() ? "<html>Checkmate! " + winner + " Wins!" : "<html>Stalemate! It's a Draw!";

		// holds the options to either play again or close the program
		String[] options = { "Yes", "No" };
		// shows a new panel prompting the user to select a piece
		JOptionPane promoPanel = new JOptionPane();
		promoPanel.setBounds(100, 250, 200, 500);
		@SuppressWarnings("static-access")
		int returnValue = promoPanel.showOptionDialog(contentPane, message + "<br>Would you like to play again?</html>",
				"Game Over!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, getOtherKing().getIcon(),
				options, options[0]);

		// assumes that the user wants to close the program if the prompt window is
		// closed
		if (returnValue == JOptionPane.CLOSED_OPTION) {
			returnValue = 1;
		}

		// closes the current instance of the program
		this.dispose();

		// starts a new one if the user answered that they wish to play again
		if (returnValue == 0) {
			try {
				new ChessGui();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Checks to see if the current king is currently in check.
	 * 
	 * @return True if the current king is in check, false if it is not
	 */
	private boolean checkForCheck() {
		return isCoorThreatened(getCurrKing().getLocation(), turn);
	}

	/**
	 * Swaps the turn along with the turn label based on which turn is currently
	 * going. Also adds the check notification to the turnLabel if the current
	 * player is in check.
	 */
	private void swapTurn() {
		// swaps the turn
		if (turn.equals(Colour.WHITE)) {
			turn = Colour.BLACK;
		} else {
			turn = Colour.WHITE;
		}

		// changes the information label based on whether or not the current player is
		// in check.
		if (checkForCheck()) {
			turnLabel.setText("<html>" + turn.toString() + "'s<br>Turn<br><br>Check!</html>");
		} else {
			turnLabel.setText("<html>" + turn.toString() + "'s<br>Turn</html>");
		}
		if (rotateScreen) {
			rotateScreen();
		}
	}

	/**
	 * Changes the captured labels by decrementing the type of the passed piece by
	 * one, then incrementing the type of the other passed piece by one in the case
	 * of a promotion.
	 * 
	 * @param pieceToRemove The piece that was removed
	 * @param promotion     A holder for the type of piece that was promoted into
	 *                      (only used in cases of promotion)
	 */
	private void changeCapturedLabels(Piece pieceToRemove, int promotion) {
		if (pieceToRemove == null) {
			return;
		}
		// sets the array location based on what color the removed piece is
		int color = pieceToRemove.getColour().equals(Colour.WHITE) ? 0 : 1;
		int pieceType;

		// sets the pieceType of the removed piece to the array location needed
		switch (pieceToRemove.toString().split(" ")[0]) {
		case "Pawn":
			pieceType = 0;
			break;
		case "Knight":
			pieceType = 1;
			break;
		case "Bishop":
			pieceType = 2;
			break;
		case "Rook":
			pieceType = 3;
			break;
		case "Queen":
			pieceType = 4;
			break;
		default:
			pieceType = -1;
		}

		// removes one piece from the counter of the chosen piece
		piecesRemaining[color][pieceType]--;

		// adds one piece to the counter of the piece is the piece was a result of
		// promotion
		if (promotion != -1) {
			piecesRemaining[color][promotion]++;
		}

		// sets the new captured label based upon the changes made above
		capturedLabels[color].setText(
				"<html>" + pieceToRemove.getColour().toString() + " Remaining:<br> Pawns: " + piecesRemaining[color][0]
						+ "<br> Knights: " + piecesRemaining[color][1] + "<br> Bishops: " + piecesRemaining[color][2]
						+ "<br> Rooks: " + piecesRemaining[color][3] + "<br> Queens: " + piecesRemaining[color][4]);
	}
}

class CloseGameException extends Exception {
	private static final long serialVersionUID = 1L;
}