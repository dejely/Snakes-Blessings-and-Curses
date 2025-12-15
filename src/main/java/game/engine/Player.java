package game.engine;

public class Player {
	
    boolean hasWhatAreTheOdds = false;
    boolean hasForetoldFate = false;
    boolean hasShackled = false;
    
    private String name;
    private int position = 1;

	public Player(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

     // getters
    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    // setter
    public void setPosition(int position) {
        this.position = position;
    }

    // Moves the player by dice roll, the board will then apply tile effects separately.

    public void move(int roll, Board board) {
        System.out.println(name + " rolled a " + roll);

        int newPos = position + roll;

        // Do not exceed board limit (assuming 100)
        if (newPos > board.getSize() - 1) {
            newPos = board.getSize() - 1;
        }

        this.position = newPos;

        System.out.println(name + " moved to tile " + position);

        // Apply tile effects
        Tile tile = board.getTile(position);
        tile.applyEffect(this);
    }

}
