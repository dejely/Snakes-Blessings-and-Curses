package game.character.types;

import game.character.Character;
import game.character.ActiveSkills.*;
import game.character.passiveSkills.*;

public class Survivor extends Character{
	
	public Survivor() {
		activeSkill = new NoOpActiveSkill();
		passiveSkill = new NoOpPassiveSkill();
	}
	
}
