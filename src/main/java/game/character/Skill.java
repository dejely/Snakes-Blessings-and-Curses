package game.character;

import game.engine.*;

public interface Skill {

	boolean canActivate(Player p, Game g);
	void activate(Player p, Game g);
	void onTurnStart(Player p, Game g);

}
