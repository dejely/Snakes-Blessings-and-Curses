package game.effects.Boon;

import game.engine.*;
import game.effects.Effect;
import game.effects.RollContext;

public class RollOverrideEffect extends Effect {

    private final int value;

    public RollOverrideEffect(int value) {
        super(1); // 🔴 REQUIRED: duration = 1 turn
        this.value = value;
    }

    @Override
    public void onBeforeRoll(Player p, Game g, RollContext ctx) {
        ctx.rollValue = value;
    }
}