package game.engine;
import java.util.Random;

import game.exceptions.OutofBoundsException;

public class Player {

public class Player {
    private final String name;
    private int position = 1;
    private final List<Effect> effects = new ArrayList<>();

    // Curses
    protected boolean hasWhatAreTheOdds = false;
    protected int barredHeavenTurns = 0;
    protected boolean skipNextTurn = false;
    protected boolean hasPillarOfSalt = false;
    protected int blackoutTurns = 0;

    // Blessings
    protected boolean hasForetoldFate = false;
    protected boolean hasShackled = false;
    protected boolean hasSwitcheroo = false;
    protected boolean hasSemented = false;
    protected int jacobsLadderCharges = 0;
    protected int danielBlessingTurns = 0;

	public Player(String name) {
		this.name = name;
	}

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

<<<<<<< Updated upstream
    public void move(int steps) {
        position = Math.min(position + steps, 100);
=======
    // Moves player and returns the Tile they landed on
    public Tile move(int roll, Board board) throws OutofBoundsException {
    int newPos = position + roll;

    // THROW the exception if they go too far
    if (newPos > board.getSize()) {
        this.position = board.getSize(); // We still move them to the end
        throw new OutofBoundsException("Player tried to move past " + board.getSize());
    }

    this.position = newPos;

    if (position > 0) {
        return board.getTile(position - 1);
    }
    return null;
}

    public void applyAllCurses() {
        whatAreTheOddsTurns = 2; // Fixed: Set to 2 turns
        barredHeavenTurns = 3;
        skipNextTurn = true;
        hasPillarOfSalt = true;
>>>>>>> Stashed changes
    }

    /**
     * Move the player by rolling the dice (curses/blessings applied automatically)
     */
    public void move(int roll, Board board) {
        System.out.println(name + " rolled a " + roll);

    public List<Effect> getEffects() {
        return Collections.unmodifiableList(effects);
    }
<<<<<<< Updated upstream

        // Do not exceed board limit
        if (newPos > board.getSize() - 1) newPos = board.getSize() - 1;

        this.position = newPos;
        System.out.println(name + " moved to tile " + position);

        // Apply tile effect
        Tile tile = board.getTile(position);
        tile.applyEffect(this);
    }

    // Helper/implementation for Job's suffering curse
    public void applyAllCurses() {
        Random random = new Random();

        // What Are The Odds
        if (!hasWhatAreTheOdds) {
            hasWhatAreTheOdds = true;
        }

        // Barred Heaven (fixed 3 turns)
        if (barredHeavenTurns <= 0) {
            barredHeavenTurns = 3;
        }

        // Unmovable Man (skip next turn)
        if (!skipNextTurn) {
            skipNextTurn = true;
        }

        // Pillar of Salt
        if (!hasPillarOfSalt) {
            hasPillarOfSalt = true;
        }

        // Blackout (random 4–5 turns)
        if (blackoutTurns <= 0) {
            blackoutTurns = 4 + random.nextInt(2);
        }

        System.out.println(getName() + " is affected by Job’s Suffering! All curses applied.");
    }
}
=======
    
}
>>>>>>> Stashed changes
