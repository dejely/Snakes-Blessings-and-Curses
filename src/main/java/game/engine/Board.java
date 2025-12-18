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
    
    // Accessors 
    public Tile getTile(int index) { return tiles.get(index); }
    public int getSize() { return size; }
}