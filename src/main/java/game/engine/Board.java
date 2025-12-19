package game.engine;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private final List<Tile> tiles;
    private final int size;

    // Constructor setup
    public Board(int size) {
        this.size = size;
        this.tiles = new ArrayList<>(size);

        // 1. Initialize board with Normal Tiles (0-based index)
        for (int i = 0; i < size; i++) {
            tiles.add(new NormalTile(i));
        }

        // 2. Overwrite specific indices with Special Tiles
        setupFixedTiles();
    }

    public Tile getTile(int index) {
        if (index < 0 || index >= tiles.size()) {
            return null;
        }
        return tiles.get(index);
    }

    public int getSize() {
        return size;
    }

    // Needs fixing
    private void setupFixedTiles() {
        // --- BLESSINGS ---
        int[] blessingLocs = {4, 16, 19, 30, 35, 40, 41, 50, 53, 57, 62 , 70, 71, 74, 80, 85, 86, 94, 98};
        
        for (int loc : blessingLocs) {
            // We pass (loc - 1) to the constructor so the Tile knows its 0-based index
            placeTile(loc, new Blessing(loc - 1));
        }
    }

    private List<Integer> getRandomTileSlots(int count) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 2; i < size - 1; i++) { 
            if (tiles.get(i).getType() == TileType.NORMAL) indexes.add(i);
        }
        Collections.shuffle(indexes);
        return indexes.subList(0, Math.min(count, indexes.size()));
    }

    public Tile getTile(int index) { return tiles.get(index); }
    public int getSize() { return size; }
}