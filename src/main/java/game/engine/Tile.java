package game.engine;

public abstract class Tile {
    protected final int index;
    protected final TileType type;

    // Constructor now accepts both index and type
    public Tile(int index, TileType type) {
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public TileType getType() {
        return type;
    }

    // All tiles define their own effect
    public abstract void applyEffect(Player player);
}
