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
    
    private int lastDie1 = 1;
    private int lastDie2 = 1;

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
    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public Player getWinner() { return winner; }
    public boolean isGameOver() { return !gameRunning; }
    
    public int getLastDie1() { return lastDie1; }
    public int getLastDie2() { return lastDie2; }

    public String processTurn(int forcedRoll) {
        if (!gameRunning) return "Game Over!";

        Player currentPlayer = players.get(currentPlayerIndex);
        StringBuilder log = new StringBuilder();
        
        // --- UI: Add Status Effects to Log ---
        String status = currentPlayer.getStatusDisplay();
        log.append("[").append(currentPlayer.getName()).append("] ");
        if (!status.isEmpty()) {
            log.append(status).append(" ");
        }

        // 1. Check if Turn is Skipped
        if (currentPlayer.skipNextTurn) {
            log.append("is skipping this turn!");
            currentPlayer.skipNextTurn = false; 
            
            // Still need to decrement passive effects even if skipped?
            // Usually yes, time passes.
            decrementCounters(currentPlayer);
            
            nextTurn();
            return log.toString();
        }

        int steps;
        
        // 2. Determine Roll Steps
        if (forcedRoll > 0) {
            steps = forcedRoll; 
            this.lastDie1 = steps / 2;
            this.lastDie2 = steps - this.lastDie1;
            
            log.append("uses Foretold Fate: ").append(steps);
            currentPlayer.hasForetoldFate = false; 
        } else {
            // NORMAL ROLL
            this.lastDie1 = Dice.rollSingleDie();
            this.lastDie2 = Dice.rollSingleDie();
            int rawSum = lastDie1 + lastDie2;
            
            // Apply Modifiers
            steps = Dice.applyModifiers(rawSum, currentPlayer);
            
            // Log Logic
            if (currentPlayer.whatAreTheOddsTurns > 0) {
                 if (steps < 0) log.append("rolled Odd (").append(rawSum).append(") -> Backward ").append(Math.abs(steps));
                 else log.append("rolled Even (").append(rawSum).append(") -> Forward ").append(steps);
                 
                 // Decrement the "What Are The Odds" counter specifically here because it was used
                 currentPlayer.whatAreTheOddsTurns--;
            } else if (currentPlayer.isShackled) {
                 log.append("rolled ").append(rawSum).append(" (Shackled -2) -> ").append(steps);
            } else {
                 log.append("rolled ").append(rawSum);
            }
        }

        // 3. Move Player & Apply Tile Effect
        Tile landedTile = currentPlayer.move(steps, board);
        
        if (landedTile != null) {
            String effectMessage = landedTile.applyEffect(currentPlayer, this);
            log.append(effectMessage);
        }

        // 4. Handle Special Movement Effects
        handleSwitcheroo(currentPlayer, log);
        handleSemented(currentPlayer, steps, log); 

        // 5. Decrement Passive Duration Effects (Daniel's Blessing, Barred Heaven)
        decrementCounters(currentPlayer);

        // 6. Check Win Condition
        if (currentPlayer.getPosition() >= board.getSize()) { 
            log.append(" -> WINNER!!!");
            winner = currentPlayer;
            gameRunning = false;
        } else {
            nextTurn();
        }

        return log.toString();
    }

    // Helper to lower turn counters
    private void decrementCounters(Player player) {
        if (player.danielBlessingTurns > 0) {
            player.danielBlessingTurns--;
        }
        if (player.barredHeavenTurns > 0) {
            player.barredHeavenTurns--;
        }
    }

    private void handleSemented(Player currentPlayer, int steps, StringBuilder log) {
        // SCENARIO A: Back Player (Bonder)
        if (currentPlayer.hasSemented && currentPlayer.sementedTarget != null) {
            
            if (currentPlayer.sementedTurns == 0) currentPlayer.sementedTurns = 3;

            if (steps < 0) {
                Player target = currentPlayer.sementedTarget;
                int newTargetPos = target.getPosition() + steps; 
                target.setPosition(newTargetPos);
                log.append("\n(Semented) Stumbled back! Dragging ").append(target.getName())
                   .append(" back to ").append(target.getPosition());
            }

            currentPlayer.sementedTurns--;
            if (currentPlayer.sementedTurns <= 0) {
                log.append("\n(Semented) The bond has broken.");
                currentPlayer.hasSemented = false;
                currentPlayer.sementedTarget = null;
            }
        }

        // SCENARIO B: Ahead Player (Target)
        for (Player p : players) {
            if (p.hasSemented && p.sementedTarget == currentPlayer) {
                if (steps > 0) {
                    int newBackPos = p.getPosition() + steps;
                    p.setPosition(newBackPos);
                    log.append("\n(Semented) Surged forward! Pulling ").append(p.getName())
                       .append(" up to ").append(p.getPosition());
                }
            }
        }
    }

    private void handleSwitcheroo(Player player, StringBuilder log) {
        if (!player.hasSwitcheroo) return;

        List<Player> aheadPlayers = new ArrayList<>();
        for (Player p : players) {
            if (p.getPosition() > player.getPosition()) aheadPlayers.add(p);
        }

        if (!aheadPlayers.isEmpty()) {
            Player target = aheadPlayers.get(new Random().nextInt(aheadPlayers.size()));
            int tempPos = player.getPosition();
            player.setPosition(target.getPosition());
            target.setPosition(tempPos);
            log.append("\n(Switcheroo) Swapped with ").append(target.getName()).append("!");
        }
        player.hasSwitcheroo = false; 
    }

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}