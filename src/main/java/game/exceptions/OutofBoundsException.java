package game.exceptions;

/*
 * Thrown when a move or operation attempts to place
 * a player outside the valid bounds of the board. eg 101, 0, -1, etc.
 */

public class OutofBoundsException extends Exception{

	public OutofBoundsException() {
        super("Move goes out of the board's bounds.");
	}

	public OutofBoundsException(String message) {
		super(message);
	}
	

}

