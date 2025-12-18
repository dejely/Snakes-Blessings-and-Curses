package game.engine;

class NormalTile extends Tile {

    public NormalTile(int index) {
        super(index, TileType.NORMAL);
    }

    @Override
    public String applyEffect(Player player, Game game) {
        return ""; // No effect, no message
    }
}
