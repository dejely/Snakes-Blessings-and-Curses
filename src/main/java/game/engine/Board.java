package game.engine;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Tile[] tiles = new Tile[100];

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
        // Note: Removed 94 from here as it conflicts with the Curse at 94
        int[] blessingLocs = {4, 16, 19, 21, 35, 40, 41, 50, 53, 57, 61, 69, 71, 74, 80, 85, 86, 98};
        
        for (int loc : blessingLocs) {
            // We pass (loc - 1) to the constructor so the Tile knows its 0-based index
            placeTile(loc, new Blessing(loc - 1));
        }

        // --- CURSES ---
        // 94 is kept here (it will be a Curse)
        int[] curseLocs = {10, 14, 22, 29, 36, 48, 60, 66, 78, 83, 89, 93};
        
        for (int loc : curseLocs) {
            placeTile(loc, new Curse(loc - 1));
        }

        // --- LADDERS ---
        addLadder(6, 26);
        addLadder(23, 42);
        addLadder(39, 58);
        addLadder(44, 54);
        addLadder(63, 72);
        addLadder(67, 78);

        // --- SNAKES ---   
        addSnake(24, 7);
        addSnake(47, 27); 
        addSnake(73, 52);
        addSnake(97, 75);
    }

    /**
     * Helper to place a tile based on "Board Number" (1-100).
     * Handles the 0-based index conversion internally.
     */
    private void placeTile(int tileNumber, Tile tile) {
        if (tileNumber > 0 && tileNumber <= size) {
            tiles.set(tileNumber - 1, tile);
        }
    }

    /**
     * Syntactic sugar to make adding Ladders readable.
     * Takes 1-based Board Numbers.
     */
    private void addLadder(int start, int end) {
        // We calculate (start - 1) and (end - 1) here to keep the setup method clean
        placeTile(start, new Ladder(start - 1, end - 1));
    }

    /**
     * Syntactic sugar to make adding Snakes readable.
     * Takes 1-based Board Numbers.
     */
    private void addSnake(int start, int end) {
        placeTile(start, new Snake(start - 1, end - 1));
    }
}