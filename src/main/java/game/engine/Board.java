package game.engine;

import game.tiles.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Board {

    private final Tile[] tiles;

    public Board(Tile[] tiles) {
        if (tiles.length != 100) {
            throw new IllegalArgumentException("Board must have exactly 100 tiles");
        }
        this.tiles = tiles;
    }

    public Tile getTile(int position) {
        if (position < 1 || position > 100) {
            throw new IllegalArgumentException("Invalid board position");
        }
        return tiles[position - 1];
    }

    /**
     * @return immutable map of snake start -> snake end
     */
    public Map<Integer, Integer> getSnakes() {
        Map<Integer, Integer> snakes = new HashMap<>();

        for (Tile tile : tiles) {
            if (tile instanceof SnakeTile snake) {
                snakes.put(
                        snake.getIndex(),
                        snake.getTarget()
                );
            }
        }
        return Collections.unmodifiableMap(snakes);
    }

    /**
     * @return immutable map of vine start -> vine end
     */
    public Map<Integer, Integer> getVines() {
        Map<Integer, Integer> vines = new HashMap<>();

        for (Tile tile : tiles) {
            if (tile instanceof VineTile vine) {
                vines.put(
                        vine.getIndex(),
                        vine.getTarget()
                );
            }
        }
        return Collections.unmodifiableMap(vines);
    }
}
