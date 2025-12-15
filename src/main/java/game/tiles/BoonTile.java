package game.tiles;

import game.engine.*;
import game.effects.Boon.*;

public class BoonTile extends Tile {

    public BoonTile(int index) {
        super(index);
    }

    @Override
    public void onLand(Player p, Game g) {
        p.addEffect(new DanielsBlessing(3), g);
    }
}
