package game.engine;

import java.util.Random;

public class Curse extends Tile {

    private final CurseType curseType;

    public Curse(int index, CurseType curseType) {
        super(index, TileType.CURSE);
        this.curseType = curseType;
    }

    @Override
    public void applyEffect(Player player) {
        // Check if player has Jacob’s Ladder blessing first
        if (player.jacobsLadderCharges > 0) {
            System.out.println(player.getName() + " nullified the curse " + curseType + " with Jacob's Ladder!");
            player.jacobsLadderCharges--;
            return; // skip applying the curse
        }

        System.out.println(player.getName() + " landed on a curse: " + curseType);
        Random random = new Random();

        switch (curseType) {
            case WHAT_ARE_THE_ODDS -> player.hasWhatAreTheOdds = true;
            case BARRED_HEAVEN -> player.barredHeavenTurns = 3; // fixed 3 turns
            case UNMOVABLE_MAN -> player.skipNextTurn = true;
            case PILLAR_OF_SALT -> player.hasPillarOfSalt = true; 
            case BLACKOUT -> player.blackoutTurns = 4 + random.nextInt(2); // random 4–5
            case JOBS_SUFFERING -> player.applyAllCurses(); // applies all fixed durations
        }
    }
}
