package game.effects;

import game.engine.Game;
import game.engine.Player;

public abstract class Effect {
    protected int remainingTurns;

    protected Effect(int turns) {
        this.remainingTurns = turns;
    }

    public void onApply(Player p, Game g) {}
    public void onTurnStart(Player p, Game g) {}
    public void onBeforeRoll(Player p, Game g, RollContext ctx) {}
    public void onAfterMove(Player p, Game g) {}
    public void onTurnEnd(Player p, Game g) {}

    public boolean isExpired() {
        return remainingTurns <= 0;
    }

    public void decrementDuration() {
        remainingTurns--;
    }
}
