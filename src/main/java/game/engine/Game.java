package game.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final Board board;
    private final List<Player> players;
    private int currentPlayerIndex;

    public Game() {
        this.board = new Board(100, 2, 5, 5, 5);
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    // called by UI when adding players
    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    public void clearPlayers() {
        players.clear();
        currentPlayerIndex = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Rolls 2 dice and applies player modifiers.
     * Returns: {die1, die2, modifiedTotal}
     */
    public int[] rollForCurrentPlayer() {
        if (players.isEmpty()) return new int[]{1,1,0};

        Player p = getCurrentPlayer();
        Random rnd = new Random();

        int d1 = rnd.nextInt(6) + 1;
        int d2 = rnd.nextInt(6) + 1;
        int total = d1 + d2;

        total = Dice.CheckProperties(total, p); // keep your special rules

        return new int[] { d1, d2, total };
    }

    public void processCurrentPlayerTurn(int steps) {
        if (players.isEmpty()) return;

        Player p = getCurrentPlayer();
        p.move(steps, board);

        // advance turn
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean isGameOver() {
        int last = board.getSize() - 1;
        for (Player p : players) {
            if (p.getPosition() >= last) return true;
        }
        return false;
    }
}