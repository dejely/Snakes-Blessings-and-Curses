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
            tiles.add(new Tile(i, TileType.NORMAL));
        }
    }

    private void placeRandomSnakes(int count) {
        int rowSize = (int) Math.sqrt(size);

        List<Integer> validIndexes = new ArrayList<>();
        // Safety Fix, i < size - 1 (Protects the Winning Tile)
        for (int i = rowSize; i < size - 1; i++) { 
            validIndexes.add(i);
        }

        Collections.shuffle(validIndexes);

        for (int i = 0; i < count && i < validIndexes.size(); i++) {
            int index = validIndexes.get(i);
            
            // Ensure we don't overwrite an existing special tile
            if(tiles.get(index).getType() != TileType.NORMAL) continue;

            // Snake drop location: 1 to index-1
            int dropTo = index > 1 ? random.nextInt(index - 1) + 1 : 1; //Make sure if index is 0 return 1 up

            // Assuming you have a SnakeTile class
           // tiles.set(index, new SnakeTile(index, dropTo)); 
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