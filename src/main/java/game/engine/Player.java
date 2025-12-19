package game.engine;

import java.util.ArrayList;
import java.util.List;

import game.exceptions.OutofBoundsException;

public class Player {

    private String name;
    private int position = 1;

    // --- CURSES ---
    // CHANGED: boolean -> int (to track the 2 turns duration)
    public int whatAreTheOddsTurns = 0; 
    
    public int barredHeavenTurns = 0;
    public boolean skipNextTurn = false;
    public boolean hasPillarOfSalt = false;
    
    // --- BLESSINGS / STATUS EFFECTS ---
    public boolean hasForetoldFate = false;
    public boolean isShackled = false; // DEBUFF: -2 to roll
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

    // Moves player and returns the Tile they landed on
    public Tile move(int roll, Board board) throws OutofBoundsException {
    int newPos = position + roll;

    // THROW the exception if they go too far
    if (newPos > board.getSize()) {
        this.position = board.getSize(); // We still move them to the end
        throw new OutofBoundsException("Player tried to move past " + board.getSize());
    }

    this.position = newPos;

    if (position > 0) {
        return board.getTile(position - 1);
    }
    return null;
}

    public void applyAllCurses() {
        whatAreTheOddsTurns = 2; // Fixed: Set to 2 turns
        barredHeavenTurns = 3;
        skipNextTurn = true;
        hasPillarOfSalt = true;
    }

    /**
     * Generates the UI string for active effects.
     * Example output: "[Barred Heaven: 2] [Shackled]"
     */
    public String getStatusDisplay() {
        List<String> statusList = new ArrayList<>();

        if (whatAreTheOddsTurns > 0) statusList.add("What are the Odds: " + whatAreTheOddsTurns);
        if (barredHeavenTurns > 0) statusList.add("Barred Heaven: " + barredHeavenTurns);
        if (danielBlessingTurns > 0) statusList.add("Daniel's Blessing: " + danielBlessingTurns);
        if (sementedTurns > 0) statusList.add("Semented: " + sementedTurns);
        if (jacobsLadderCharges > 0) statusList.add("Jacob's Ladder: " + jacobsLadderCharges);
        
        if (skipNextTurn) statusList.add("Frozen");
        if (isShackled) statusList.add("Shackled");
        if (hasPillarOfSalt) statusList.add("Pillar of Salt");
        if (hasForetoldFate) statusList.add("Foretold Fate");
        if (hasSwitcheroo) statusList.add("Switcheroo Ready");

        if (statusList.isEmpty()) return "";
        return "[" + String.join("] [", statusList) + "]";
    }
    
}