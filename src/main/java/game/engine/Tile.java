package game.engine;

public abstract class Tile {
    protected final int index;
    protected final TileType type;

    public Tile(int index, TileType type) {
        this.index = index;
    }

    public int getIndex() { return index; }
    public TileType getType() { return type; }

    // NOW RETURNS A MESSAGE STRING AND TAKES GAME CONTEXT
    public abstract String applyEffect(Player player, Game game);
}

class NormalTile extends Tile {
    public NormalTile(int index) {
        super(index, TileType.NORMAL);
    }

    @Override
    public String applyEffect(Player player, Game game) {
        return ""; // No message for normal tiles to keep chat clean
    }
}