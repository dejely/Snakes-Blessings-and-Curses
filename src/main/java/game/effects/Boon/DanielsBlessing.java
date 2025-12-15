package game.effects.Boon;

import game.effects.*;
import game.engine.*;

public class DanielsBlessing extends Effect {

    public DanielsBlessing(int turns) {
        super(turns);
    }

    @Override
    public void onBeforeRoll(Player p, Game g, RollContext ctx) {
        ctx.canUseSnakes = false;
    }
}
