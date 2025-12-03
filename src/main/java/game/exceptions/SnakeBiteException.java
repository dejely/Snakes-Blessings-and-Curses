package game.exceptions;

/**
 * Thrown when a player is affected by a snake tile
 * or snake-related hazard on the board.
 */

public class SnakeBiteException extends Exception{

	public SnakeBiteException() {
        super("The player has been bitten by a snake.");
    }

    public SnakeBiteException(String message) {
        super(message);
    }

}
