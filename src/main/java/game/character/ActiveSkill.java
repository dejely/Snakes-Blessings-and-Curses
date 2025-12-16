package game.character;

import game.engine.*;

public abstract class ActiveSkill implements Skill {
    protected final int cooldown;
    protected int remainingCooldown = 0;

    protected ActiveSkill(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public boolean canActivate(Player p, Game g) {
        return remainingCooldown == 0;
    }

    @Override
    public void activate(Player p, Game g) {
        remainingCooldown = cooldown;
    }

    @Override
    public void onTurnStart(Player p, Game g) {}

    public void tickCooldown() {
        if (remainingCooldown > 0) {
            remainingCooldown--;
        }
    }
}
