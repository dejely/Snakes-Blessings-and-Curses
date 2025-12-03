package game.exceptions;

/**
 * Thrown when a player's attempted move violates
 * the game rules or is not permitted at the moment.
 */

public class InvalidMoveException {

	public InvalidMoveException() {
		
		public InvalidMoveException() {
			super("Invalid move attempted.");
		}

		public InvalidMoveException(String message) {
			super(message);
		}
	}

}
