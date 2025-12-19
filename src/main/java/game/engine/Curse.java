package game.engine;

import java.util.Random;

public class Curse extends Tile {

    private final CurseType curseType;

    // Fix: Added a constructor that picks a random curse type automatically
    public Curse(int index) {
        super(index, TileType.CURSE);
        this.curseType = randomType();
    }

    public Curse(int index, CurseType curseType) {
        super(index, TileType.CURSE);
        this.curseType = curseType;
    }

    private CurseType randomType() {
        CurseType[] values = CurseType.values();
        return values[new Random().nextInt(values.length)];
    }

    @Override
    public String applyEffect(Player player, Game game) {
        // Check if player has Jacob’s Ladder blessing first
        if (player.jacobsLadderCharges > 0) {
            player.jacobsLadderCharges--;
            return " -> Landed on CURSE (" + curseType + "), but Jacob's Ladder protected you!";
        }

        String msg = " -> OH NO! Cursed by " + curseType + ".";

        switch (curseType) {
            case WHAT_ARE_THE_ODDS -> {
                player.whatAreTheOddsTurns = 3; // Fixed variable name
                msg += " (Must roll EVEN to move forward for 3 turns)";
            }
            case BARRED_HEAVEN -> {
                player.barredHeavenTurns = 3;
                msg += " (Ladders are blocked for 3 turns)";
            }
            case UNMOVABLE_MAN -> {
                player.skipNextTurn = true;
                msg += " (You are frozen for the next turn)";
            }
            case PILLAR_OF_SALT -> {
                player.hasPillarOfSalt = true;
                msg += " (If you hit a snake next, you turn to stone)";
            }
            case JOBS_SUFFERING -> {
                player.applyAllCurses();
                msg += " (You suffer from EVERYTHING!)";
            }
        }
        return msg;
    }
}