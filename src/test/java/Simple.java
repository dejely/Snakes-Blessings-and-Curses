

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import game.TestUtils;
import game.character.types.*;
import game.effects.*;

import game.engine.*;
import game.TestUtils;

class Simple {

	@Test
	void startTurn_entersSkillPhase() {
	    Game game = TestUtils.twoPlayerGame(
	            new Player("A", new Seer()),
	            new Player("B", new Seer())
	    );

	    game.startTurn();

	    assertEquals(TurnPhase.SKILL, game.getPhase());
	}
	
	@Test
	void rollDice_advancesToNextPlayer() {
	    Game game = TestUtils.twoPlayerGame(
	            new Player("A", new Seer()),
	            new Player("B", new Seer())
	    );

	    Player first = game.getCurrentPlayer();

	    game.startTurn();
	    game.rollDice();

	    assertNotEquals(first, game.getCurrentPlayer());
	}
	
	@Test
	void skillCannotActivateOutsideSkillPhase() {
	    Player p = new Player("A", new Seer());
	    Game game = TestUtils.twoPlayerGame(p, new Player("B", new Seer()));

	    // No startTurn() → not SKILL phase
	    game.activateSkill();

	    assertTrue(p.canActivateSkill(game));
	}
	
	@Test
	void playerNeverBelowOne() {
	    Player p = new Player("P", new Seer());
	    p.move(-1000);
	    assertEquals(1, p.getPosition());
	}

	@Test
	void playerNeverAboveHundred() {
	    Player p = new Player("P", new Seer());
	    p.move(1000);
	    assertEquals(100, p.getPosition());
	}
	
	@Test
	void effectCanModifyRoll() {
	    Player p = new Player("P", new Seer());
	    Game g = new Game(List.of(p), TestUtils.emptyBoard());

	    p.addEffect(new Effect(1) {
	        @Override
	        public void onBeforeRoll(Player p, Game g, RollContext ctx) {
	            ctx.rollValue = 6;
	        }
	    }, g);

	    RollContext ctx = p.buildRollContext(g, 2);
	    assertEquals(6, ctx.rollValue);
	}
	@Test
	void activeSkillGoesOnCooldown() {
	    Player p = new Player("P", new Seer());
	    Game g = new Game(List.of(p), TestUtils.emptyBoard());

	    g.startTurn();
	    assertTrue(p.canActivateSkill(g));

	    g.activateSkill();
	    assertFalse(p.canActivateSkill(g));
	}
	






}
