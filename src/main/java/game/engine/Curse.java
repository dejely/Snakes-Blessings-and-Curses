package game.engine;

import java.util.Random;

public class Curse extends Tile {

    private final CurseType curseType;

    public Curse(int index, CurseType curseType) {
        super(index, TileType.CURSE);
        this.curseType = curseType;
    }

    public Curse(int index) {
        super(index, TileType.CURSE);
        this.curseType = randomType();
    }

    private CurseType randomType() {
        CurseType[] values = CurseType.values();
        return values[new Random().nextInt(values.length)];
    }

    @Override
    public String applyEffect(Player player, Game game) {
        // 1. Check for Jacob's Ladder
        if (player.jacobsLadderCharges > 0) {
            player.jacobsLadderCharges--;
            return " -> Landed on CURSE " + curseType + ", but Jacob's Ladder nullified it!";
        }

        StringBuilder msg = new StringBuilder();
        msg.append(" -> Landed on CURSE: ").append(curseType).append("! ");

        switch (curseType) {
            case WHAT_ARE_THE_ODDS -> {
                player.whatAreTheOddsTurns = 2; // Set duration to 2 turns
                msg.append("\n(What are the odds?) Next 2 rolls: Even = Forward, Odd = Backward.");
            }
            case BARRED_HEAVEN -> {
                player.barredHeavenTurns = 3;
                msg.append("\n(Barred Heaven) For the next 3 turns, ladders won't move you up.");
            }
            case UNMOVABLE_MAN -> {
                player.skipNextTurn = true;
                msg.append("\n(Unmovable Man) Your next turn is given up to the player in last place.");
            }
            case PILLAR_OF_SALT -> {
                player.hasPillarOfSalt = true;
                msg.append("\n(Pillar of Salt) You are cursed. You will lose a turn if eaten by a snake.");
            }
            case JOBS_SUFFERING -> {
                player.applyAllCurses();
                msg.append("\n(Job's Suffering) ALL curses applied to you!");
            }
        }
        return msg.toString();
    }
}