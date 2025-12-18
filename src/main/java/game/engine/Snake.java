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

	public String applyEffect(Player player, Game game) {
		// Pillar of Salt curse effect
		if (player.hasPillarOfSalt) {
        player.skipNextTurn = true;
        player.hasPillarOfSalt = false;
		return player.getName() + " triggered Pillar of Salt! Will skip next turn.";
    }
	
		// Blackout curse effect

	    // Daniel’s Blessing blocks snake
		if (player.danielBlessingTurns > 0) {
			player.danielBlessingTurns--; // decrement counter
			return player.getName() + " is protected by Daniel's Blessing! Snake ignored."; // skip snake effect
	}

		// Normal snake effect
        player.setPosition(dropTo);
		return "Snake! Slide down from " + getIndex() + " to " + dropTo;
	}


}
