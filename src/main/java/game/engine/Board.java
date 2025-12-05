package game.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class Board {

    private final List<Tile> tiles;
    private final int size;
    private final Random random = new Random();

    public Board(int size, int snakeCount, int ladderCount, int curseCount, int boonCount) {
        this.size = size;
        this.tiles = new ArrayList<>(size);

        generateDefaultTiles();
        placeRandomSnakes(snakeCount);
        placeRandomLadders(ladderCount); 
        placeRandomCurses(curseCount);
        placeRandomBoons(boonCount);
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
            int dropTo = random.nextInt(index - 1) + 1;

            // Assuming you have a SnakeTile class
           // tiles.set(index, new SnakeTile(index, dropTo)); 
        }
    }

    // New Method for Ladders/Vines
    private void placeRandomLadders(int count) {
        int rowSize = (int) Math.sqrt(size);
        List<Integer> validIndexes = new ArrayList<>();
        
        // Start from tile 2, end before the last row (to ensure there is space to climb up)
        for (int i = 2; i < size - rowSize; i++) {
            validIndexes.add(i);
        }
        
        Collections.shuffle(validIndexes);
        
        for(int i = 0; i < count && i < validIndexes.size(); i++) {
            int index = validIndexes.get(i);
            if(tiles.get(index).getType() != TileType.NORMAL) continue;
            
            // Climb logic, must be higher than current, but not higher than size-1
            int maxClimb = size - 2; 
            int climbTo = (maxClimb > index) ? random.nextInt(maxClimb - index) + index + 1 : size - 1;
            
            // Assuming you create a LadderTile class similar to SnakeTile
            //tiles.set(index, new LadderTile(index, climbTo));
        }
    }

    private void placeRandomCurses(int count) {
        List<Integer> validIndexes = getRandomTileSlots(count);
        for (int index : validIndexes) {
            //tiles.set(index, new CurseTile(index));
        }
    }

    private void placeRandomBoons(int count) {
        List<Integer> validIndexes = getRandomTileSlots(count);
        for (int index : validIndexes) {
            //tiles.set(index, new BoonTile(index));
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