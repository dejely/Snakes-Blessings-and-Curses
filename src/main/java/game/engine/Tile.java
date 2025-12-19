package game.engine;

public abstract class Tile {
    protected int index;
    protected TileType type;

    public enum TileType { NORMAL, SNAKE, LADDER, BLESSING, CURSE }

    public Tile(int index, TileType type) {
        this.index = index;
        this.type = type;
    }

    /**
     * @return Narrative string for the Chronicle log.
     */
    public abstract String applyEffect(Player player, Game game);
    
    public int getIndex() { return index; }
    public TileType getType() { return type; }
}