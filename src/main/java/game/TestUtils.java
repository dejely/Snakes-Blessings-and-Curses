package game;
import game.engine.*;
import game.tiles.NormalTile;

import java.util.List;

public final class TestUtils {

    private TestUtils() {}

    public static Board emptyBoard() {
        Tile[] tiles = new Tile[100];
        for (int i = 0; i < 100; i++) {
            tiles[i] = new NormalTile(i + 1);
        }
        return new Board(tiles);
    }

    public static Game twoPlayerGame(Player p1, Player p2) {
        return new Game(List.of(p1, p2), emptyBoard());
    }
}
