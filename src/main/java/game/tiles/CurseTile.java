package game.tiles;

import game.engine.*;
import game.effects.curses.*;

public class CurseTile extends Tile {

    public CurseTile(int index) {
        super(index);
    }

    @Override
    public void onLand(Player p, Game g) {
        p.addEffect(new DiminishingRollCurse(3), g);
    }
}
