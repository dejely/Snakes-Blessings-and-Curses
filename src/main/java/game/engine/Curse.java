package game.engine;

import java.util.Random;

public class Curse extends Tile {

    private final CurseType curseType;

    public Curse(int index, CurseType curseType) {
        super(index, TileType.CURSE);
        this.curseType = curseType;
    }

    @Override
    public String applyEffect(Player player, Game game) {
        // Check if player has Jacob’s Ladder blessing first
        if (player.jacobsLadderCharges > 0) {
            player.jacobsLadderCharges--;
            return player.getName() + " nullified the curse " + curseType + " with Jacob's Ladder!"; // skip applying the curse
        }

        Random random = new Random();

        switch (curseType) {
            case WHAT_ARE_THE_ODDS -> player.whatAreTheOddsTurns = 2;
            case BARRED_HEAVEN -> player.barredHeavenTurns = 3; // fixed 3 turns
            case UNMOVABLE_MAN -> player.skipNextTurn = true;
            case PILLAR_OF_SALT -> player.hasPillarOfSalt = true; 
            case JOBS_SUFFERING -> player.applyAllCurses(); // applies all fixed durations
        }
		return player.getName() + " landed on a curse: " + curseType;
    }
}
