package game.character.types;

import game.character.Character;
import game.character.passiveSkills.*;
import game.character.ActiveSkills.*;

public class Seer extends Character {

    public Seer() {
        passiveSkill = new SnakeImmunity();
        activeSkill = new ChooseExactRoll();
    }
}
