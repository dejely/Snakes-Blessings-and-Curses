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

        if (currentPlayer.skipNextTurn) {
            currentPlayer.skipNextTurn = false;
            log.append("Frozen! Skipping turn.");
            nextTurn();
            return log.toString();
        }

        try {
            // --- VALIDATION ---
            if (currentPlayer.hasForetoldFate && currentPlayer.barredHeavenTurns > 0 && forcedRoll > 10) {
                throw new InvalidMoveException("BARRED HEAVEN: You cannot jump more than 10 tiles!");
            }

            // 2. Shackled: Limit choice
            if (currentPlayer.isShackled && forcedRoll > 6) {
                throw new InvalidMoveException("SHACKLED: Your chains are too heavy to choose a destiny that far!");
            }
            if (!gameRunning) {
                throw new InvalidMoveException("THE CHRONICLE IS CLOSED.");
            }

            // --- END VALIDATION ---
            int lastDie1 = 1;
            int lastDie2 = 1;
            int roll;
            if (currentPlayer.hasForetoldFate = true) {
                roll = forcedRoll;
                lastDie1 = forcedRoll / 2;
                lastDie2 = forcedRoll - lastDie1;
                currentPlayer.hasForetoldFate = false;
                log.append("Invoked Foretold Fate: [ ").append(Dice.getDicePips(roll)).append(" ] (").append(roll).append(" steps)");
            } else {
                // Fallback for logic-driven turns
                int d1 = Dice.rollSingleDie();
                int d2 = Dice.rollSingleDie();
                roll = Dice.applyModifiers(d1 + d2, currentPlayer);
                log.append("Rolled ").append(d1 + d2);
            }

            if (roll < 0) log.append(" (Cursed Reverse!)");
            
            decrementStatusEffects(currentPlayer);

            int targetPos = currentPlayer.getPosition() + roll;

            // Win-check boundary
            if (targetPos > 100) {
                throw new OutofBoundsException("The gates of Heaven are closed to this roll! Stay at " + currentPlayer.getPosition());
            }
            if (targetPos < 1) targetPos = 1;

            currentPlayer.setPosition(targetPos);
            log.append("\nMoved to tile ").append(currentPlayer.getPosition());

            // --- TILE EFFECTS ---
            Tile landedTile = board.getTile(currentPlayer.getPosition() - 1);
            String effectMsg = landedTile.applyEffect(currentPlayer, this);
            log.append(effectMsg);

            // --- SPECIAL ABILITIES ---
            handleSementedEffect(currentPlayer, roll, log);
            if (currentPlayer.hasSwitcheroo) handleSwitcheroo(currentPlayer, log);

            // --- FINAL WIN CHECK ---
            if (currentPlayer.getPosition() == 100) {
                winner = currentPlayer;
                gameRunning = false;
                log.append("\n*** ").append(currentPlayer.getName()).append(" HAS REACHED GLORY! ***");
            }

        } catch (InvalidMoveException e) {
            log.append("\n[!] INVALID ACTION: ").append(e.getMessage());
            log.append("\nProphecy failed. Rolling naturally instead...");
            return processTurn(-1, false); // Recursive call for a natural roll
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