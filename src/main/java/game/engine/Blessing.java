package game.engine;

public class Blessing extends Tile {

    private final BlessingType blessingType;

    public Blessing(int index, BlessingType blessingType) {
        super(index, TileType.BLESSING);
        this.blessingType = blessingType;
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
