package game.engine;

public class Snake extends Tile {

	private final int dropTo; // Tail (where the player gets dropped to )

	public Snake(int index, int dropTo) {
		super(index, TileType.SNAKE);
		this.dropTo = dropTo;
	}

	public int getDropto(){
		return dropTo;
	}

	@Override
	public void applyEffect(Player player) {
		// Pillar of Salt curse effect
		if (player.hasPillarOfSalt) {
        System.out.println(player.getName() + " triggered Pillar of Salt! Will skip next turn.");
        player.skipNextTurn = true;
        player.hasPillarOfSalt = false;
		return;
    }
	
		// Blackout curse effect
	    if (player.blackoutTurns > 0) {
        System.out.println("Blackout revealed the snake at " + getIndex() + "!");
    }

	    // Daniel’s Blessing blocks snake
		if (player.danielBlessingTurns > 0) {
			System.out.println(player.getName() + " is protected by Daniel's Blessing! Snake ignored.");
			player.danielBlessingTurns--; // decrement counter
			return; // skip snake effect
	}

		// Normal snake effect
		System.out.println("Snake! Slide down from " + getIndex() + " to " + dropTo);
        player.setPosition(dropTo);
	}

}
