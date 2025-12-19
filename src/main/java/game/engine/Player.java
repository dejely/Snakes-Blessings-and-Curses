package game.engine;

import java.util.ArrayList;
import java.util.List;

import game.exceptions.InvalidMoveException;
import game.exceptions.OutofBoundsException;

public class Player {

    private String name;
    private int position = 1;

    // --- CURSES ---
    public int whatAreTheOddsTurns = 0; 
    public int barredHeavenTurns = 0;
    public boolean skipNextTurn = false;
    public boolean hasPillarOfSalt = false;
    
    // --- BLESSINGS / STATUS EFFECTS ---
    public boolean hasForetoldFate = false;
    public boolean isShackled = false; 
    public boolean hasSwitcheroo = false;
    public int jacobsLadderCharges = 0;
    public int danielBlessingTurns = 0;

    // --- SEMENTED ---
    public boolean hasSemented = false; 
    public Player sementedTarget = null; 
    public int sementedTurns = 0;       

    public Player(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public int getPosition() { return position; }

    public void setPosition(int position) {
        if (position < 1) position = 1;
        if (position > 100) position = 100;
        this.position = position;
    }

 // In Player.java

    public Tile move(int steps, Board board) throws OutofBoundsException, InvalidMoveException {
        // Calculate the theoretical new position
        int newPos = position + steps;

        // --- BACKTRACKING SYSTEM (Bounce Logic) ---
        if (newPos > 100) {
            int overshoot = newPos - 100;
            newPos = 100 - overshoot;
            // Example: At 99, Roll 2 -> Target 101 -> Overshoot 1 -> NewPos 99
        }
        // ------------------------------------------

        // Safety clamp for negative movement (e.g. curses)
        if (newPos < 1) {
            newPos = 1;
        }

        // Apply the new position
        this.position = newPos;

        // Return the tile we landed on (index is position - 1)
        return board.getTile(position - 1);
    }

    public void applyAllCurses() {
        whatAreTheOddsTurns = 2; 
        barredHeavenTurns = 3;
        skipNextTurn = true;
        hasPillarOfSalt = true;
    }

    /**
     * Decrements counters for all active effects.
     * Called by Game.java at the end of every turn.
     */
    public void onTurnEnd() {
        if (whatAreTheOddsTurns > 0) whatAreTheOddsTurns--;
        if (barredHeavenTurns > 0) barredHeavenTurns--;
        if (danielBlessingTurns > 0) danielBlessingTurns--;
        
        if (sementedTurns > 0) {
            sementedTurns--;
            if (sementedTurns == 0) {
                hasSemented = false;
                sementedTarget = null;
            }
        }
    }

    public String getStatusDisplay() {
        List<String> statusList = new ArrayList<>();

        if (whatAreTheOddsTurns > 0) statusList.add("What are the Odds: " + whatAreTheOddsTurns);
        if (barredHeavenTurns > 0) statusList.add("Barred Heaven: " + barredHeavenTurns);
        if (danielBlessingTurns > 0) statusList.add("Daniel's Blessing: " + danielBlessingTurns);
        if (sementedTurns > 0) statusList.add("Semented: " + sementedTurns);
        if (jacobsLadderCharges > 0) statusList.add("Jacob's Ladder: " + jacobsLadderCharges);
        
        if (skipNextTurn) statusList.add("Frozen");
        if (isShackled) statusList.add("Shackled");
        if (hasForetoldFate) statusList.add("Foretold Fate");
        if (hasPillarOfSalt) statusList.add("Pillar of Salt");

        return statusList.isEmpty() ? "" : String.join(", ", statusList);
    }
}