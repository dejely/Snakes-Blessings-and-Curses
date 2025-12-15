package game.tiles;

import game.engine.*;

public class SnakeTile extends Tile {

    private final int target;

    public SnakeTile(int index, int target) {
        super(index);
        this.target = target;
    }

    @Override
    public void onLand(Player p, Game g) {
        p.move(target - p.getPosition());
    }

    public int getTarget() {
        return target;
    }

}
