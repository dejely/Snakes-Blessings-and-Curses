package game.exceptions;

/**
 * Thrown when a move or operation attempts to place
 * a player outside the valid bounds of the board. eg 101, 0, -1, etc.
 */

public class OutofBoundsException extends Exception{

	public OutofBoundsException() {
	
		public OutOfBoundsException() {
        	super("Move goes out of the board's bounds.");
		}

		public OutOfBoundsException(String message) {
			super(message);
		}

	}

}

