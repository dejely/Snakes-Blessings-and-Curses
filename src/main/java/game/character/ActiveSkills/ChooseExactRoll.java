package game.character.ActiveSkills;

import game.character.ActiveSkill;
import game.engine.*;
import game.effects.Boon.RollOverrideEffect;

public class ChooseExactRoll extends ActiveSkill {

    private int chosenRoll;

    public ChooseExactRoll() {
        super(3); // cooldown
    }

    public void setChosenRoll(int roll) {
        this.chosenRoll = roll;
    }

    @Override
    public void activate(Player p, Game g) {
        super.activate(p, g);
        p.addEffect(new RollOverrideEffect(chosenRoll), g);
    }
}
