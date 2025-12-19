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