package game.character.passiveSkills;

import game.character.*;
import game.engine.*;
import game.effects.*;

public class SnakeImmunity implements Skill {

    private boolean used = false;

    @Override
    public boolean canActivate(Player p, Game g) {
        return false;
    }

    @Override
    public void activate(Player p, Game g) {}

    @Override
    public void onTurnStart(Player p, Game g) {
        if (!used) {
            p.addEffect(new Effect(1) {
                @Override
                public void onAfterMove(Player p, Game g) {
                    used = true;
                }
            }, g);
        }
    }
}
