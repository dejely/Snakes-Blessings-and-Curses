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
    }

    public int getClimbTo() {
        return climbTo;
    }
}
