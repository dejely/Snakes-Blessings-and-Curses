package game.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class Board {
    private final Tile[] tiles = new Tile[100];

    public Board(TileFactory factory) {
        for (int i = 0; i < 100; i++) {
            tiles[i] = factory.createTile(i + 1);
        }
    }

    public Tile getTile(int position) {
        if (position < 1 || position > 100) {
            throw new IllegalArgumentException("Invalid board position");
        }
        return tiles[position - 1];
    }
}