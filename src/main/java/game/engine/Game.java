package game.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean gameRunning;
    private Player winner;

    public Game(int numPlayers) {
        this.board = new Board(10, 2, 5, 5, 5);
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameRunning = false;

        // Initialize players
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
    }

    // ---------------- Roll dice for GUI button ----------------
    public void rollDiceForCurrentPlayer() {
        Player currentPlayer = players.get(currentPlayerIndex);

        // Skip turn if Unmovable Man curse
        if (currentPlayer.skipNextTurn) {
            System.out.println(currentPlayer.getName() + " skips this turn!");
            currentPlayer.skipNextTurn = false;
            nextTurn();
            return;
        }

        // Roll dice using player effects
        int roll = Dice.DiceRollRNG(currentPlayer); // Dice controlled by Game
        currentPlayer.move(roll, board);

        // Handle Switcheroo immediately after movement
        handleSwitcheroo(currentPlayer);

        // Decrement timers
        if (currentPlayer.blackoutTurns > 0) currentPlayer.blackoutTurns--;
        if (currentPlayer.barredHeavenTurns > 0) currentPlayer.barredHeavenTurns--;
        if (currentPlayer.danielBlessingTurns > 0) currentPlayer.danielBlessingTurns--;

        // Shuffle snakes if Blackout active
        if (currentPlayer.blackoutTurns > 0) {
            board.shuffleSnakes();
            System.out.println(currentPlayer.getName() + " triggered Blackout! Snakes shuffled.");
        }

        // Check for game over
        if (isGameOver()) {
            gameRunning = false;
            System.out.println("Winner: " + winner.getName());
            return;
        }

        nextTurn();
    }

    // ---------------- Handle Switcheroo ----------------
    private void handleSwitcheroo(Player player) {
        if (!player.hasSwitcheroo) return;

        List<Player> aheadPlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.getPosition() > player.getPosition()) aheadPlayers.add(p);
        }

        if (!aheadPlayers.isEmpty()) {
            Random rand = new Random();
            Player target = aheadPlayers.get(rand.nextInt(aheadPlayers.size()));

            int tempPos = player.getPosition();
            player.setPosition(target.getPosition());
            target.setPosition(tempPos);

            System.out.println(player.getName() + " swapped with " + target.getName() + "!");
        } else {
            System.out.println(player.getName() + " tried Switcheroo but no one is ahead!");
        }

        player.hasSwitcheroo = false; // clear flag
    }

    // ---------------- Turn order ----------------
    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println("Next turn: " + players.get(currentPlayerIndex).getName());
    }

    // ---------------- Game over check ----------------
    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getPosition() == board.getSize() - 1) {
                winner = player;
                return true;
            }
        }
        return false;
    }
}
