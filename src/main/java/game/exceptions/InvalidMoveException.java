package game.exceptions;

public class InvalidMoveException extends Exception{

	public InvalidMoveException() {
		
		public InvalidMoveException() {
			super("Invalid move attempted.");
		}

		public InvalidMoveException(String message) {
			super(message);
		}
	}

}
