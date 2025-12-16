package game.character.passiveSkills;

import game.character.Skill;
import game.engine.Game;
import game.engine.Player;

public class NoOpPassiveSkill implements Skill{

	@Override
	public boolean canActivate(Player p, Game g) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activate(Player p, Game g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTurnStart(Player p, Game g) {
		// TODO Auto-generated method stub
		
	}
	
}
