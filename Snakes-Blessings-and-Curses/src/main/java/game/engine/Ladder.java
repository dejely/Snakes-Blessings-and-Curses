package game.engine;

public class Ladder extends Tile {

    private final int climbTo; // tile index to move player up to

    public Ladder(int index, int climbTo) {
        super(index, TileType.LADDER);
        this.climbTo = climbTo;
    }

    @Override
    public void applyEffect(Player player) {
        System.out.println(player.getName() + " landed on a ladder! Climbing from tile "
                + getIndex() + " to tile " + climbTo);

        // Barred Heaven curse prevents ladder movement
        if (player.barredHeavenTurns > 0) {
            System.out.println(player.getName() + " is affected by Barred Heaven curse! Cannot climb.");
            return;
        }

        // Move player up the ladder
        player.setPosition(climbTo);
<<<<<<< Updated upstream
=======
        
        // FIX: We add +1 here so the text matches the visual board numbers (1-100)
        // Previous code printed 'climbTo', which was the 0-based index (e.g., 51 instead of 52).
        return " -> LADDER! Climbed up to " + (climbTo) + ".";
>>>>>>> Stashed changes
    }

    public int getClimbTo() {
        return climbTo;
    }
}
