package game.engine;

public class Tile {

    // Removed final so we can update it during blackout curse or shuffles
    private int index; 
    private final TileType type;

    public Tile(int index, TileType type) {
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    // Needed for the blackout curse to update tile positions
    public void setIndex(int index) {
        this.index = index;
    }

    public TileType getType() {
        return type;
    }

    // Children classes (Snake, Curse) will override this.
    public void applyEffect(Player player) {
        System.out.println("Player landed on a normal tile at " + index);
    }
}