package game.exceptions;

public class InvalidMoveException extends Exception{

	public InvalidMoveException() {
		super("Invalid Move Attempted!");
	}
		


	public InvalidMoveException(String message) {
		super(message);
	}
	

}
