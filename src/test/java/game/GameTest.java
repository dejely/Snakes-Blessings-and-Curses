package game;

import game.engine.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class GameTest {

    private Game game;
    private Player p1;
    private Player p2;

    @Before
    public void setUp() {
        // Initialize a 2-player game for interaction tests
        game = new Game(2);
        List<Player> players = game.getPlayers();
        p1 = players.get(0);
        p2 = players.get(1);
        
        // Reset positions to 1
        p1.setPosition(1);
        p2.setPosition(1);
    }

//Movement Tiles
    @Test
    public void testLadderClimb() {
        // Create a ladder at index 10 (Tile 11) that goes to Tile 50
        Ladder ladder = new Ladder(10, 50);
        
        // Apply effect
        String result = ladder.applyEffect(p1, game);
        
        assertEquals("Player should climb to 50", 50, p1.getPosition());
        assertTrue("Message should mention Ladder", result.contains("LADDER"));
    }

    @Test
    public void testSnakeSlide() {
        // Player is at 99. Snake at 99 drops to 10.
        p1.setPosition(99);
        Snake snake = new Snake(98, 10); // Index 98 = Tile 99
        
        String result = snake.applyEffect(p1, game);
        
        assertEquals("Player should slide to 10", 10, p1.getPosition());
        assertTrue("Message should mention Snake", result.contains("Snake"));
    }


    //BLESSINGS
  

    @Test
    public void testBlessingForetoldFate() {
        Blessing b = new Blessing(5, BlessingType.FORETOLD_FATE);
        b.applyEffect(p1, game);
        
        assertTrue("Player should have Foretold Fate flag", p1.hasForetoldFate);
    }

    @Test
    public void testBlessingDanielsBlessing() {
        Blessing b = new Blessing(5, BlessingType.DANIELS_BLESSING);
        b.applyEffect(p1, game);
        
        assertEquals("Should have 2 turns of protection", 2, p1.danielBlessingTurns);
    }

    @Test
    public void testBlessingShackled() {
        Blessing b = new Blessing(5, BlessingType.SHACKLED);
        b.applyEffect(p1, game);
        
        // FIX: Check p2 (the opponent), not p1. 
        // The blessing chains "OTHER" players, not the user.
        assertTrue("Opponent should be shackled", p2.isShackled);
        assertFalse("Self should NOT be shackled", p1.isShackled);
    }

    @Test
    public void testBlessingJacobsLadder() {
        Blessing b = new Blessing(5, BlessingType.JACOBS_LADDER);
        b.applyEffect(p1, game);
        
        assertEquals("Should have 2 charges", 2, p1.jacobsLadderCharges);
    }

    @Test
    public void testBlessingSwitcheroo() {
        //setup: P1 at 10, P2 (Target) ahead at 50
        p1.setPosition(10);
        p2.setPosition(50);
        
        Blessing b = new Blessing(9, BlessingType.SWITCHEROO);
        b.applyEffect(p1, game);
        
        assertEquals("P1 should swap to 50", 50, p1.getPosition());
        assertEquals("P2 should swap to 10", 10, p2.getPosition());
    }

    @Test
    public void testBlessingSemented() {
        // Setup: P2 is ahead
        p1.setPosition(10);
        p2.setPosition(20);
        
        Blessing b = new Blessing(9, BlessingType.SEMENTED);
        b.applyEffect(p1, game);
        
        assertTrue("P1 should have Semented status", p1.hasSemented);
        assertEquals("P1 should be linked to P2", p2, p1.sementedTarget);
        assertEquals("Duration should be 3 turns", 3, p1.sementedTurns);
    }

    // 3.CURSES

    @Test
    public void testCurseUnmovableMan() {
        Curse c = new Curse(5, CurseType.UNMOVABLE_MAN);
        c.applyEffect(p1, game);
        
        assertTrue("Player should skip next turn", p1.skipNextTurn);
    }

    @Test
    public void testCurseBarredHeaven() {
        Curse c = new Curse(5, CurseType.BARRED_HEAVEN);
        c.applyEffect(p1, game);
        
        assertEquals("Should be barred for 3 turns", 3, p1.barredHeavenTurns);
    }

    @Test
    public void testCursePillarOfSalt() {
        Curse c = new Curse(5, CurseType.PILLAR_OF_SALT);
        c.applyEffect(p1, game);
        
        assertTrue("Should have Pillar of Salt status", p1.hasPillarOfSalt);
    }

    @Test
    public void testCurseWhatAreTheOdds() {
        Curse c = new Curse(5, CurseType.WHAT_ARE_THE_ODDS);
        c.applyEffect(p1, game);
        
        assertEquals("Should last 3 turns", 3, p1.whatAreTheOddsTurns);
    }

    @Test
    public void testCurseJobsSuffering() {
        Curse c = new Curse(5, CurseType.JOBS_SUFFERING);
        c.applyEffect(p1, game);
        
        // Should trigger ALL other curses
        assertTrue("Should trigger Unmovable Man", p1.skipNextTurn);
        assertTrue("Should trigger Pillar of Salt", p1.hasPillarOfSalt);
        assertEquals("Should trigger Barred Heaven", 3, p1.barredHeavenTurns);
        assertEquals("Should trigger What Are The Odds", 3, p1.whatAreTheOddsTurns);
    }

    //4. COMPLEX INTERACTIONS

    @Test
    public void testInteraction_JacobsLadder_Vs_Curse() {
        // 1. Give player protection
        p1.jacobsLadderCharges = 1;
        
        // 2. Hit a Curse
        Curse c = new Curse(5, CurseType.UNMOVABLE_MAN);
        String result = c.applyEffect(p1, game);
        
        // 3. Assertions
        assertFalse("Curse should NOT apply", p1.skipNextTurn);
        assertEquals("Charge should be used up", 0, p1.jacobsLadderCharges);
        assertTrue("Message should mention protection", result.contains("Jacob's Ladder protected"));
    }

    @Test
    public void testInteraction_DanielsBlessing_Vs_Snake() {
        // 1. Give player snake protection
        p1.danielBlessingTurns = 2;
        p1.setPosition(50);
        
        // 2. Hit a Snake (drops to 10)
        Snake snake = new Snake(49, 10);
        String result = snake.applyEffect(p1, game);
        
        // 3. Assertions
        assertEquals("Player should NOT slide", 50, p1.getPosition());
        assertTrue("Message should mention Daniel", result.contains("Daniel's Blessing"));
    }

    @Test
    public void testInteraction_BarredHeaven_Vs_Ladder() {
        // 1. Curse the player
        p1.barredHeavenTurns = 1;
        p1.setPosition(10);
        
        // 2. Hit a Ladder (climbs to 50)
        Ladder ladder = new Ladder(9, 50);
        String result = ladder.applyEffect(p1, game);
        
        // 3. Assertions
        assertEquals("Player should NOT climb", 10, p1.getPosition());
        assertTrue("Message should mention Barred Heaven", result.contains("Barred Heaven"));
    }

    @Test
    public void testInteraction_PillarOfSalt_Vs_Snake() {
        // 1. Curse the player
        p1.hasPillarOfSalt = true;
        p1.setPosition(50);
        p1.skipNextTurn = false; // Ensure false initially
        
        // 2. Hit a Snake (drops to 10)
        Snake snake = new Snake(49, 10);
        String result = snake.applyEffect(p1, game);
        
        // 3. Assertions
        assertEquals("Player should NOT slide (frozen instead)", 50, p1.getPosition());
        assertTrue("Player should lose turn", p1.skipNextTurn);
        assertFalse("Pillar status should be consumed", p1.hasPillarOfSalt);
        assertTrue("Message should mention Stone", result.contains("stone"));
    }
    
    @Test
    public void testPlayerBounceBack() {
        // Setup: Player is at Tile 99
        p1.setPosition(99);
        
        //Action: Roll a 2
        //Calculation: 99 + 2 = 101. 
        //Overshoot is 1. 
        //New Pos = 100 - 1 = 99.
        try {
            p1.move(2, game.getBoard());
        } catch (Exception e) {
            fail("Should not throw exception during bounce back");
        }
        
        //Assertion
        assertEquals("Player should bounce back to 99", 99, p1.getPosition());
    }

    @Test
    public void testPlayerBounceBackLargeRoll() {
        //Setup: Player is at Tile 98
        p1.setPosition(98);
        
        //Action: Roll a 5
        //Calculation: 98 + 5 = 103. 
        //Overshoot is 3. 
        //New Pos = 100 - 3 = 97.
        try {
            p1.move(5, game.getBoard());
        } catch (Exception e) {
            fail("Should not throw exception");
        }
        
        //Assertion
        assertEquals("Player should bounce back to 97", 97, p1.getPosition());
    }

    @Test
    public void testPlayerExactWin() {
        //Setup: Player is at Tile 98
        p1.setPosition(98);
        
        //Action: Roll a 2
        //Calculation: 98 + 2 = 100.
        //No overshoot.
        try {
            p1.move(2, game.getBoard());
        } catch (Exception e) {
            fail("Should not throw exception");
        }
        
        // Assertion
        assertEquals("Player should land exactly on 100", 100, p1.getPosition());
    }
    
    
}