package game.effects.curses;

import game.effects.*;
import game.engine.*;

public class DiminishingRollCurse extends Effect {

    public DiminishingRollCurse(int turns) {
        super(turns);
    }

    @Override
    public void onBeforeRoll(Player p, Game g, RollContext ctx) {
        if (ctx.rollValue % 2 == 1) {
            ctx.canMoveForward = false;
        }
    }
}
