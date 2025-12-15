package game.engine;

import game.config.BoardConfig;
import game.tiles.*;

import java.util.Random;

public final class BoardFactory {

    private BoardFactory() {}

    public static Board createRandomBoard(long seed, BoardConfig config) {
        Random rng = new Random(seed);
        Tile[] tiles = new Tile[100];

        for (int i = 0; i < 100; i++) {
            int index = i + 1;

            // 🔒 Protected tiles
            if (index == 1 || index == 100) {
                tiles[i] = new NormalTile(index);
                continue;
            }

            double roll = rng.nextDouble();

            if (roll < config.normalProb) {
                tiles[i] = new NormalTile(index);
            } else if (roll < config.normalProb + config.boonProb) {
                tiles[i] = new BoonTile(index);
            } else {
                tiles[i] = new CurseTile(index);
            }
        }

        // Snakes & vines can still be layered afterward if desired
        return new Board(tiles);
    }
}
