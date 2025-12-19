package game.engine;

public class Ladder extends Tile {

    private final int climbTo; 

    // Constructor receives 0-based index for 'climbTo'
    public Ladder(int index, int climbTo) {
        super(index, TileType.LADDER);
        this.climbTo = climbTo;
    }

    @Override
    public String applyEffect(Player player, Game game) {
        if (player.barredHeavenTurns > 0) {
            return " -> Landed on LADDER, but Barred Heaven prevented the climb!";
        }

        player.setPosition(climbTo);
        
        return " -> LADDER! Climbed up to " + (climbTo) + ".";
    }
}