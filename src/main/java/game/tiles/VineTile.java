package game.tiles;

import game.engine.*;

public class VineTile extends Tile {

    private final int target;

    public VineTile(int index, int target) {
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
