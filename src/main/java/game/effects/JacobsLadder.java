package game.effects;

import game.engine.*;

public class JacobsLadder extends Effect {

    public JacobsLadder(int charges) {
        super(charges);
    }

    @Override
    public void onApply(Player p, Game g) {
        // Exists purely as an interceptor
    }

    @Override
    public void onBeforeRoll(Player p, Game g, RollContext ctx) {
        // Hook reserved for curse interception logic
    }
}
