package game.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.exceptions.InvalidMoveException;
import game.exceptions.OutofBoundsException;

public class Game {
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean gameRunning;
    private Player winner;

    public Game(int numPlayers) {
        this.board = new Board(100); 
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameRunning = true;

        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
    }

    public List<Player> getPlayers() { return players; }
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public boolean isGameOver() { return !gameRunning; }
    public Player getWinner() { return winner; }

   public String processTurn(int forcedRoll, boolean isFate) {
    Player currentPlayer = getCurrentPlayer();
    StringBuilder log = new StringBuilder(currentPlayer.getName().toUpperCase() + ": ");

    // 1. Check for Skip Turn (Frozen / Pillar of Salt)
    if (currentPlayer.skipNextTurn) {
        currentPlayer.skipNextTurn = false;
        log.append("Frozen! Skipping turn.");
        if (gameRunning) nextTurn();
        return log.toString();
    }

    try {
        // --- VALIDATION ---
        if (isFate && currentPlayer.barredHeavenTurns > 0 && forcedRoll > 10) {
            throw new InvalidMoveException("BARRED HEAVEN: Your spirit is too heavy to jump that far!");
        }
        if (isFate && currentPlayer.isShackled && forcedRoll > 6) {
            throw new InvalidMoveException("SHACKLED: Your chains limit your prophecy!");
        }
        if (!gameRunning) throw new InvalidMoveException("THE CHRONICLE IS CLOSED.");

        // --- MOVEMENT ---
        int roll;
        // FIX: Use '==' for comparison, not '=' which was an assignment
        if (isFate && currentPlayer.hasForetoldFate) {
            roll = forcedRoll;
            currentPlayer.hasForetoldFate = false; // Consume the blessing
            log.append("Invoked Foretold Fate: (").append(roll).append(" steps)");
        } else {
            // Use the roll sent by the UI dice
            roll = Dice.applyModifiers(forcedRoll, currentPlayer);
            log.append("Rolled ").append(forcedRoll);
        }

        if (roll < 0) log.append(" (Cursed Reverse!)");
        
        // Effects wear off after the roll is finalized
        decrementStatusEffects(currentPlayer);

        int targetPos = currentPlayer.getPosition() + roll;

        // Boundary Check
        if (targetPos > 100) {
            throw new OutofBoundsException("The gates are closed to this roll! Stay at " + currentPlayer.getPosition());
        }
        if (targetPos < 1) targetPos = 1;

        // Move the player to the landing tile
        currentPlayer.setPosition(targetPos);
        log.append("\nMoved to tile ").append(currentPlayer.getPosition());

        // --- TILE EFFECTS (Snakes, Ladders, etc.) ---
        // landedTile is 0-indexed, so Tile 1 is index 0
        Tile landedTile = board.getTile(currentPlayer.getPosition() - 1);
        if (landedTile != null) {
            String effectMsg = landedTile.applyEffect(currentPlayer, this);
            log.append(effectMsg);
        }

        // --- ABILITIES & WIN CHECK ---
        handleSementedEffect(currentPlayer, roll, log);
        if (currentPlayer.hasSwitcheroo) handleSwitcheroo(currentPlayer, log);

        if (currentPlayer.getPosition() == 100) {
            winner = currentPlayer;
            gameRunning = false;
            log.append("\n*** ").append(currentPlayer.getName()).append(" HAS REACHED GLORY! ***");
        }

    } catch (InvalidMoveException e) {
        log.append("\n[!] INVALID: ").append(e.getMessage());
        log.append("\nFate rejected. Rolling naturally instead...");
        // Re-roll without the Fate flag
        return processTurn(Dice.rollSingleDie() + Dice.rollSingleDie(), false); 
    } catch (OutofBoundsException e) {
        log.append("\n").append(e.getMessage());
    }

    if (gameRunning) nextTurn();
    return log.toString();

}
    // ... (rest of helper methods remain the same)
    private void decrementStatusEffects(Player p) {
        if (p.whatAreTheOddsTurns > 0) p.whatAreTheOddsTurns--;
        if (p.barredHeavenTurns > 0) p.barredHeavenTurns--;
        if (p.danielBlessingTurns > 0) p.danielBlessingTurns--;
        if (p.sementedTurns > 0) p.sementedTurns--;
    }

    private void handleSementedEffect(Player source, int steps, StringBuilder log) {
        if (source.hasSemented && source.sementedTarget != null && steps > 0) {
            Player target = source.sementedTarget;
            target.setPosition(target.getPosition() + steps);
            log.append("\n(Bond) Pulling ").append(target.getName()).append(" to ").append(target.getPosition());
        }
    }

   private void handleSwitcheroo(Player p, StringBuilder log) {
    // 1. Collect all players strictly ahead of the current player
    List<Player> ahead = new ArrayList<>();
    for (Player other : players) {
        // Only add if they are further along AND not the current player
        // Optional: Add '&& other.getPosition() < 100' if you don't want to swap with a winner
        if (other != p && other.getPosition() > p.getPosition()) {
            ahead.add(other);
        }
    }

    // 2. If there are people ahead, pick ONE at random and swap
    if (!ahead.isEmpty()) {
        Player target = ahead.get(new Random().nextInt(ahead.size()));
        
        int myOldPos = p.getPosition();
        int theirOldPos = target.getPosition();

        p.setPosition(theirOldPos);
        target.setPosition(myOldPos);

        log.append("\n(Switcheroo) Fate intervened! You swapped places with ")
           .append(target.getName())
           .append(" (Tile ").append(theirOldPos).append(" <-> ").append(myOldPos).append(").");
    } else {
        log.append("\n(Switcheroo) You are already leading the pack; there is no one ahead to swap with.");
    }

    // 3. Always consume the effect
    p.hasSwitcheroo = false;
}

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}