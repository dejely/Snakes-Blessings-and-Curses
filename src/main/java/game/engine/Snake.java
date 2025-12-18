package game.engine;

public class Snake extends Tile {

    private final int dropTo;

    public Snake(int index, int dropTo) {
        super(index, TileType.SNAKE);
        this.dropTo = dropTo;
    }

    @Override
    public String applyEffect(Player player, Game game) {
        // 1. Check Pillar of Salt
        if (player.hasPillarOfSalt) {
            player.skipNextTurn = true;
            player.hasPillarOfSalt = false;
            return " -> Landed on SNAKE, but Pillar of Salt turned you to stone! Did not slide, but Turn Skipped.";
        }

        // 2. Check Daniel's Blessing
        // FIX: We do NOT decrement here. It lasts for 2 full turns (handled in Game.java).
        if (player.danielBlessingTurns > 0) {
            return " -> Landed on SNAKE, but Daniel's Blessing shut its mouth! You are safe.";
        }

        // 3. Normal Slide
        player.setPosition(dropTo);
        
        // We add +1 so the text matches visual board numbers (1-100)
        return " -> OH NO! Snake ate you! Slid down to " + (dropTo + 1) + ".";
    }
}