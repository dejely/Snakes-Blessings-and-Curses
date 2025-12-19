package game.engine;

public class Snake extends Tile {
    private int destination;

    public Snake(int head, int tail) {
        super(head, TileType.SNAKE);
        this.destination = tail;
    }

    @Override
    public String applyEffect(Player player, Game game) {
        // Check for Daniel's Blessing protection
        if (player.danielBlessingTurns > 0) {
            return "\n[!] DIVINE SHIELD: A snake strikes, but Daniel's Blessing protects you!";
        }
        
        player.setPosition(destination);
        return "\n[!] SNAKE: You stepped on a serpent's head! Slithered down to " + destination;
    }
}