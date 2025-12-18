package game.engine;

public abstract class Tile {
    protected final int index;
    protected final TileType type;

    public Tile(int index, TileType type) {
        this.index = index;
        this.type = type; // ✅ FIX
    }

    public int getIndex() {
        return index;
    }

    public TileType getType() {
        return type;
    }

    // Returns a message describing what happened on this tile
    public abstract String applyEffect(Player player, Game game);
}
