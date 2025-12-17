package game.engine;

public abstract class Tile {
    protected final int index;

    protected Tile(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public abstract void onLand(Player p, Game g);
}