package game.tiles;

import game.engine.*;

public class NormalTile extends Tile {

    public NormalTile(int index) {
        super(index);
    }

    @Override
    public void onLand(Player p, Game g) {
        // no-op
    }
}
