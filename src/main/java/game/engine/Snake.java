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
		System.out.println("Snake! Slide down from " + getIndex() + " to " + dropTo); // message subject to change
        player.setPosition(dropTo); // placeholder
	}

}
