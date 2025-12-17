package game.engine;

public class Player {

public class Player {
    private final String name;
    private int position = 1;
    private final List<Effect> effects = new ArrayList<>();

    // Curses
    protected boolean hasWhatAreTheOdds = false;
    protected int barredHeavenTurns = 0;
    protected boolean skipNextTurn = false;
    protected boolean pillarOfSalt = false;
    protected int blackoutTurns = 0;

    // Blessings
    protected boolean hasForetoldFate = false;
    protected boolean hasShackled = false;
    protected boolean canSwitcheroo = false;
    protected boolean hasSemented = false;
    protected int jacobsLadderCharges = 0;
    protected int snakesMouthShutTurns = 0;

	public Player(String name) {
		this.name = name;
	}

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void move(int steps) {
        position = Math.min(position + steps, 100);
    }

    /**
     * Move the player by rolling the dice (curses/blessings applied automatically)
     */
    public void move(Board board) {
        int roll = Dice.DiceRollRNG(this); // roll dice with all effects
        System.out.println(getName() + " rolled a " + roll);

    public List<Effect> getEffects() {
        return Collections.unmodifiableList(effects);
    }

        // Keep within board limits
        if (newPos >= board.getSize()) newPos = board.getSize() - 1;
        else if (newPos < 0) newPos = 0;

        position = newPos;
        System.out.println(name + " moved to tile " + position);

        // Apply tile effects
        Tile tile = board.getTile(position);
        tile.applyEffect(this);
        
    }

    // Helper for blackout curse
    public void applyAllCurses() {
        hasWhatAreTheOdds = true;
        barredHeavenTurns = 3;
        skipNextTurn = true;
        pillarOfSalt = true;
        blackoutTurns = 4;
    }
}
