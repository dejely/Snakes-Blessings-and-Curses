package game.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class Board {
    private final Tile[] tiles = new Tile[100];

    private final List<Tile> tiles;
    private final int size;
    private final Random random = new Random();

    public Board(int size, int snakeCount, int ladderCount, int curseCount, int blessingCount) {
        this.size = size;
        this.tiles = new ArrayList<>(size);

        generateDefaultTiles();
        placeRandomSnakes(snakeCount);
        placeRandomLadders(ladderCount); 
        placeRandomCurses(curseCount);
        placeRandomBlessings(blessingCount);
    }

    private void generateDefaultTiles() {
        for (int i = 0; i < size; i++) {
            tiles.add(new NormalTile(i));
        }
    }

    private void placeRandomSnakes(int count) {
        List<Integer> validIndexes = new ArrayList<>();
        int rowSize = (int) Math.sqrt(size);

<<<<<<< Updated upstream
        // Snakes cannot start in the first row or last tile
        for (int i = rowSize; i < size - 1; i++) {
            if (tiles.get(i).getType() == TileType.NORMAL)
                validIndexes.add(i);
        }

        Collections.shuffle(validIndexes);

        for (int i = 0; i < count && i < validIndexes.size(); i++) {
            int index = validIndexes.get(i);

            // Drop must be below current index
            int dropTo = random.nextInt(index - 1) + 1; 
            tiles.set(index, new Snake(index, dropTo));
        }
    }

    // Helper for blackout curse
    public void shuffleSnakes() {
    List<Integer> snakeIndexes = new ArrayList<>();

    // collect all snake tile indexes
    for (int i = 0; i < tiles.size(); i++) {
        if (tiles.get(i).getType() == TileType.SNAKE) {
            snakeIndexes.add(i);
        }
    }

    // shuffle indexes
    Collections.shuffle(snakeIndexes);

    // move each snake to a new tile
    int idx = 0;
    for (int i = 0; i < tiles.size(); i++) {
        if (tiles.get(i).getType() == TileType.SNAKE) {
            Snake oldSnake = (Snake) tiles.get(i);
            int newDropTo = oldSnake.getDropto();

            // place snake on new shuffled index
            tiles.set(snakeIndexes.get(idx), new Snake(snakeIndexes.get(idx), newDropTo));
            idx++;
        }
    }
}

    // New Method for Ladders/Vines
    private void placeRandomLadders(int count) {
        List<Integer> validIndexes = new ArrayList<>();
        int rowSize = (int) Math.sqrt(size);

        // Ladder cannot start at last row or first tile
        for (int i = 2; i < size - rowSize; i++) {
            if (tiles.get(i).getType() == TileType.NORMAL)
                validIndexes.add(i);
        }

        Collections.shuffle(validIndexes);

        for (int i = 0; i < count && i < validIndexes.size(); i++) {
            int index = validIndexes.get(i);

            int maxClimb = size - 2; // cannot climb to last tile
            int climbTo = (maxClimb > index) ? random.nextInt(maxClimb - index) + index + 1 : size - 1;

            tiles.set(index, new Ladder(index, climbTo));
        }
    }


    private void placeRandomCurses(int count) {
        List<Integer> validIndexes = getRandomTileSlots(count);
        CurseType[] curseTypes = CurseType.values();

        for (int index : validIndexes) {
            CurseType type = curseTypes[random.nextInt(curseTypes.length)];
            tiles.set(index, new Curse(index, type));
        }
    }

    private void placeRandomBlessings(int count) {
        List<Integer> validIndexes = getRandomTileSlots(count);
        BlessingType[] blessingTypes = BlessingType.values();

        for (int index : validIndexes) {
            BlessingType type = blessingTypes[random.nextInt(blessingTypes.length)];
            tiles.set(index, new Blessing(index, type));
        }
    }

    private List<Integer> getRandomTileSlots(int count) {
        List<Integer> indexes = new ArrayList<>();

        // SAFETY FIX: i < size - 1 to protect the winning tile
        for (int i = 2; i < size - 1; i++) { 
            if (tiles.get(i).getType() == TileType.NORMAL) {
                indexes.add(i);
            }
        }

        Collections.shuffle(indexes);
        return indexes.subList(0, Math.min(count, indexes.size()));
=======
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
        placeTile(start, new Ladder(start , end));
    }

    /**
     * Syntactic sugar to make adding Snakes readable.
     * Takes 1-based Board Numbers.
     */
    private void addSnake(int start, int end) {
        placeTile(start, new Snake(start, end));
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    }
}