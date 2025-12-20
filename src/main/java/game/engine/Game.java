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
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public boolean isGameOver() { return !gameRunning; }
    public Player getWinner() { return winner; }
    public Board getBoard() {return board;}

    public String processTurn(int forcedRoll) {
        Player currentPlayer = getCurrentPlayer();
        StringBuilder log = new StringBuilder(currentPlayer.getName().toUpperCase() + ": ");

        if (currentPlayer.skipNextTurn) {
            currentPlayer.skipNextTurn = false;
            log.append("Frozen! Skipping turn.");
            currentPlayer.onTurnEnd(); 
            nextTurn();
            return log.toString();
        }

        // --- DICE ROLL ---
        int d1, d2;
        
        if (currentPlayer.hasForetoldFate) {
            // Player chooses the outcome (passed via forcedRoll)
            // We split it visually for the UI, though logic just uses the sum
            if (forcedRoll < 1) forcedRoll = 1; 
            if (forcedRoll > 12) forcedRoll = 12;
            
            d1 = forcedRoll / 2;
            d2 = forcedRoll - d1;
            
            log.append("(Foretold Fate) You chose to move ").append(forcedRoll).append(" steps. ");
            currentPlayer.hasForetoldFate = false; // Consume the buff
        } else {
            // Normal Roll
            d1 = Dice.rollSingleDie();
            d2 = Dice.rollSingleDie();
        }

        lastDie1 = d1;
        lastDie2 = d2;
        int rollSum = d1 + d2;
        
        // Apply Modifiers (Shackled, What Are The Odds, etc.)
        int finalMove = Dice.applyModifiers(rollSum, currentPlayer);
        
        log.append("Rolled ").append(getDicePips(d1)).append(" ").append(getDicePips(d2))
           .append(" (").append(rollSum).append("). ");
        
        if (finalMove != rollSum) {
            log.append("Modified to ").append(finalMove).append(". ");
        }

        
        try {
            Tile landedTile = currentPlayer.move(finalMove, board);
            
            if (landedTile != null) {
                // Apply Tile Effect (Snake, Ladder, Blessing, Curse)
                String effectMsg = landedTile.applyEffect(currentPlayer, this);
                log.append(effectMsg);
            }
            
            // Check Win Condition
            if (currentPlayer.getPosition() == 100) {
                gameRunning = false;
                winner = currentPlayer;
                log.append("\nVICTORY! ").append(currentPlayer.getName()).append(" has reached the end!");
                return log.toString();
            }

        } catch (OutofBoundsException | InvalidMoveException e) {
            log.append("Move blocked! ").append(e.getMessage());
        }

        // --- END OF TURN UPDATES ---
        currentPlayer.onTurnEnd();
        
        // Semented Check (Link movement)
     // --- SEMENTED LOGIC START ---
        if (currentPlayer.hasSemented && currentPlayer.sementedTarget != null) {
            Player target = currentPlayer.sementedTarget;
            
            // Check if the link is still valid (target exists and hasn't won yet)
            if (target.getPosition() < 100) {
                log.append("\n(Semented) The mystic bond tugs at ").append(target.getName()).append("...");

                
                try {
                    // 1. Move the target by the same amount the current player moved
                    Tile targetLandedTile = target.move(finalMove, board);
                    log.append("\n   -> ").append(target.getName()).append(" was pulled ").append(finalMove).append(" steps!");
    
                    // 2. Apply effects for the target (e.g., if they are pulled onto a Snake)
                    String effectMsg = targetLandedTile.applyEffect(target, this);
                    if (!effectMsg.isEmpty()) {
                        log.append(effectMsg);
                    }
    
                } catch (OutofBoundsException | InvalidMoveException e) {
                    log.append("\n   -> But ").append(target.getName()).append(" hit the wall!");
                }
            }
        }
        // --- SEMENTED LOGIC END ---

        nextTurn();
        return log.toString();
    }

    public int getLastDie1() { return lastDie1; }
    public int getLastDie2() { return lastDie2; }

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

     private String getDicePips(int v) {
                return switch (v) {
                    case 1 -> "\u2680";
                    case 2 -> "\u2681";
                    case 3 -> "\u2682";
                    case 4 -> "\u2683";
                    case 5 -> "\u2684";
                    case 6 -> "\u2685";
                    default -> "?";
                };
    }
}