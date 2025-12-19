package game.engine;

public abstract class Tile {
    protected final int index;
    protected final TileType type;

    public Tile(int index, TileType type) {
        this.index = index;
        this.type = type; // <--- THIS WAS MISSING
    }

    public int getIndex() { return index; }
    public TileType getType() { return type; }

    // Abstract method for effects
    public abstract String applyEffect(Player player, Game game);
}

// Keep this class inside the same file (package-private)
class NormalTile extends Tile {
    public NormalTile(int index) {
        super(index, TileType.NORMAL);
    }

    @Override
    public String applyEffect(Player player, Game game) {
        return ""; // No message for normal tiles
    }
}