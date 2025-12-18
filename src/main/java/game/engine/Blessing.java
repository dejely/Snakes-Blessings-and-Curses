package game.engine;

import java.util.Random;

public class Blessing extends Tile {

    private final BlessingType blessingType;

    public Blessing(int index, BlessingType blessingType) {
        super(index, TileType.BLESSING);
        this.blessingType = blessingType;
    }

    public Blessing(int index) {
        super(index, TileType.BLESSING);
        this.blessingType = randomType();
    }

    private BlessingType randomType() {
        BlessingType[] values = BlessingType.values();
        return values[new Random().nextInt(values.length)];
    }

    public BlessingType getBlessingType() {
        return blessingType;
    }

    @Override
    public void applyEffect(Player player) {
        System.out.println(player.getName() + " landed on a blessing: " + blessingType);

        switch (blessingType) {
            case FORETOLD_FATE -> player.hasForetoldFate = true;
            case DANIELS_BLESSING -> player.danielBlessingTurns = 2; 
            case SWITCHEROO -> player.hasSwitcheroo = true;
            case JACOBS_LADDER -> player.jacobsLadderCharges = 2;
            case SHACKLED -> player.hasShackled = true;
            case SEMENTED -> player.hasSemented = true;
        }
    }
}
