package game.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class Board {

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

    // FIXED BOARD constructor
    public Board(int size) {
        this.size = size;
        this.tiles = new ArrayList<>(size);

        generateDefaultTiles();
        setupFixedTiles();
    }

    private void generateDefaultTiles() {
        for (int i = 0; i < size; i++) {
            tiles.add(new NormalTile(i));
        }
    }

    private void placeRandomSnakes(int count) {
        List<Integer> validIndexes = new ArrayList<>();
        int rowSize = (int) Math.sqrt(size);

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
    }

    private void setupFixedTiles() {
        // ---- LADDERS ----
        tiles.set(5,  new Ladder(5, 24));
        tiles.set(27, new Ladder(27, 48));
        tiles.set(31, new Ladder(31, 52));
        tiles.set(46, new Ladder(46, 56));
        tiles.set(63, new Ladder(63, 82));
        tiles.set(67, new Ladder(67, 88));

        // ---- SNAKES ----
        tiles.set(26, new Snake(26, 6));
        tiles.set(43, new Snake(43, 23));       
        tiles.set(77, new Snake(77, 58));
        tiles.set(93, new Snake(93, 75));


        // ---- BLESSINGS ----
        tiles.set(3,  new Blessing(3));
        tiles.set(11, new Blessing(11));
        tiles.set(14, new Blessing(14));
        tiles.set(29, new Blessing(29));        
        tiles.set(30, new Blessing(30));
        tiles.set(35, new Blessing(35));        
        tiles.set(40, new Blessing(40));
        tiles.set(49, new Blessing(49));
        tiles.set(53, new Blessing(53));        
        tiles.set(57, new Blessing(57));
        tiles.set(61, new Blessing(61));
        tiles.set(69, new Blessing(69));
        tiles.set(70, new Blessing(70));        
        tiles.set(76, new Blessing(76));
        tiles.set(79, new Blessing(79));
        tiles.set(84, new Blessing(84));
        tiles.set(85, new Blessing(85));
        tiles.set(92, new Blessing(92));
        tiles.set(96, new Blessing(96));

        // ---- CURSES ----
        tiles.set(9, new Curse(9));
        tiles.set(16, new Curse(16));        
        tiles.set(21, new Curse(21));
        tiles.set(28, new Curse(28));        
        tiles.set(34, new Curse(34));
        tiles.set(42, new Curse(42)); 
        tiles.set(50, new Curse(50));
        tiles.set(64, new Curse(64)); 
        tiles.set(72, new Curse(72));
        tiles.set(81, new Curse(81));
        tiles.set(87, new Curse(87));
        tiles.set(97, new Curse(97));
    }
    
    // Accessors 
    public Tile getTile(int index) { return tiles.get(index); }
    public int getSize() { return size; }
}