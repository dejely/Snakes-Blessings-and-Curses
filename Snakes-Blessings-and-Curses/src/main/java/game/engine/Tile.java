package game.engine;

public abstract class Tile {
    protected final int index;
    protected final TileType type;

    // Constructor accepts both index and type
    public Tile(int index, TileType type) {
        this.index = index;
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

class NormalTile extends Tile {
    public NormalTile(int index) {
        super(index, TileType.NORMAL);
    }

    @Override
    public void applyEffect(Player player) {
        System.out.println(player.getName() + " landed on a normal tile.");
    }
}
