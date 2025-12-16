package game.character.types;


import game.character.Character;
import game.character.passiveSkills.*;
import game.character.ActiveSkills.*;

public class Trickster extends Character{
	
	public Trickster() {
		passiveSkill = new NoOpPassiveSkill();
		activeSkill = new ChooseExactRoll();
	}
}
