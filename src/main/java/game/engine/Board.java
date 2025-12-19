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

        // --- CURSES ---
        int[] curseLocs = {10, 14, 22, 29, 36, 43, 60, 65, 78, 82, 88, 93};
        
        for (int loc : curseLocs) {
            placeTile(loc, new Curse(loc - 1));
        }

        // --- LADDERS ---
        addLadder(6, 25);
        addLadder(28, 49);
        addLadder(39, 58);
        addLadder(47, 54);
        addLadder(64, 83);
        addLadder(68, 89);

        // --- SNAKES ---   
        addSnake(27, 7);
        addSnake(44, 24); 
        addSnake(73, 52);
        addSnake(97, 76);
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
        placeTile(start, new Ladder(start - 1, end));
    }

    /**
     * Syntactic sugar to make adding Snakes readable.
     * Takes 1-based Board Numbers.
     */
    private void addSnake(int start, int end) {
        placeTile(start, new Snake(start - 1, end));
    }
}