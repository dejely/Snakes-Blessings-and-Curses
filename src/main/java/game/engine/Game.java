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
        this.board = new Board(10, 2, 5, 5, 5);
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameRunning = false;

        // Initialize players
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
    }

<<<<<<< Updated upstream
    // ---------------- Roll dice for GUI button ----------------
    public void rollDiceForCurrentPlayer() {
        Player currentPlayer = players.get(currentPlayerIndex);

        // Skip turn if Unmovable Man curse
        if (currentPlayer.skipNextTurn) {
            System.out.println(currentPlayer.getName() + " skips this turn!");
            currentPlayer.skipNextTurn = false;
=======
    public List<Player> getPlayers() { return players; }
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public boolean isGameOver() { return !gameRunning; }
    public Player getWinner() { return winner; }

    public String processTurn(int forcedRoll) {
        Player currentPlayer = getCurrentPlayer();
        StringBuilder log = new StringBuilder(currentPlayer.getName().toUpperCase() + ": ");

        if (currentPlayer.skipNextTurn) {
            currentPlayer.skipNextTurn = false;
            log.append("Frozen! Skipping turn.");
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
            nextTurn();
            return;
        }

<<<<<<< Updated upstream
<<<<<<< Updated upstream
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
=======
        try {
            // --- VALIDATION LOGIC (Where InvalidMoveException is thrown) ---
            
            // 1. Barred Heaven: Restricted movement
            if (currentPlayer.barredHeavenTurns > 0 && forcedRoll > 10) {
                throw new InvalidMoveException("BARRED HEAVEN: You cannot use Foretold Fate to jump more than 10 tiles while cursed!");
            }

            // 2. Shackled: Limit choice
            if (currentPlayer.isShackled && forcedRoll > 6) {
                throw new InvalidMoveException("SHACKLED: Your chains are too heavy to choose a destiny that far!");
            }

            // 3. Game State: Cannot move if game is over
            if (!gameRunning) {
                throw new InvalidMoveException("THE CHRONICLE IS CLOSED: The game has already ended.");
            }

            // --- END VALIDATION ---

            int roll;
            if (forcedRoll > 0 && currentPlayer.hasForetoldFate) {
                roll = forcedRoll;
                lastDie1 = forcedRoll / 2;
                lastDie2 = forcedRoll - lastDie1;
                currentPlayer.hasForetoldFate = false;
                log.append("Invoked Foretold Fate: [ ").append(getDicePips(roll)).append(" ] (").append(roll).append(" steps)");
            } else {
                lastDie1 = Dice.rollSingleDie();
                lastDie2 = Dice.rollSingleDie();
                roll = Dice.applyModifiers(lastDie1 + lastDie2, currentPlayer);
                log.append("Rolled ").append(Math.abs(roll));
                if (roll < 0) log.append(" (Cursed Reverse!)");
            }
            decrementStatusEffects(currentPlayer);

            int targetPos = currentPlayer.getPosition() + roll;

            // OutofBoundsException trigger
            if (targetPos > 100) {
                throw new OutofBoundsException("The gates of Heaven are closed to this roll! Stay at " + currentPlayer.getPosition());
            }
            if (targetPos < 1) targetPos = 1;

            currentPlayer.setPosition(targetPos);
            log.append("\nMoved to tile ").append(currentPlayer.getPosition());

            Tile landedTile = board.getTile(currentPlayer.getPosition() - 1);
            String effectMsg = landedTile.applyEffect(currentPlayer, this);
            log.append(effectMsg);

            handleSementedEffect(currentPlayer, roll, log);

            if (currentPlayer.hasSwitcheroo) {
                handleSwitcheroo(currentPlayer, log);
            }

            if (currentPlayer.getPosition() == 100) {
                winner = currentPlayer;
                gameRunning = false;
                log.append("\n*** ").append(currentPlayer.getName()).append(" HAS REACHED GLORY! ***");
            }

        } catch (InvalidMoveException e) {
            // This is now reachable!
            log.append("\n[!] INVALID ACTION: ").append(e.getMessage());
            // We do NOT call nextTurn() here if you want them to try again, 
            // OR we call it to punish the wasted attempt. Usually, in Foretold Fate, 
            // we let them roll normally instead:
            log.append("\nProphecy failed. Rolling naturally instead...");
            return processTurn(-1); 

        } catch (OutofBoundsException e) {
            log.append("\n").append(e.getMessage());
        }

=======
        try {
            // --- VALIDATION LOGIC (Where InvalidMoveException is thrown) ---
            
            // 1. Barred Heaven: Restricted movement
            if (currentPlayer.barredHeavenTurns > 0 && forcedRoll > 10) {
                throw new InvalidMoveException("BARRED HEAVEN: You cannot use Foretold Fate to jump more than 10 tiles while cursed!");
            }

            // 2. Shackled: Limit choice
            if (currentPlayer.isShackled && forcedRoll > 6) {
                throw new InvalidMoveException("SHACKLED: Your chains are too heavy to choose a destiny that far!");
            }

            // 3. Game State: Cannot move if game is over
            if (!gameRunning) {
                throw new InvalidMoveException("THE CHRONICLE IS CLOSED: The game has already ended.");
            }

            // --- END VALIDATION ---

            int roll;
            if (forcedRoll > 0 && currentPlayer.hasForetoldFate) {
                roll = forcedRoll;
                lastDie1 = forcedRoll / 2;
                lastDie2 = forcedRoll - lastDie1;
                currentPlayer.hasForetoldFate = false;
                log.append("Invoked Foretold Fate: [ ").append(getDicePips(roll)).append(" ] (").append(roll).append(" steps)");
            } else {
                lastDie1 = Dice.rollSingleDie();
                lastDie2 = Dice.rollSingleDie();
                roll = Dice.applyModifiers(lastDie1 + lastDie2, currentPlayer);
                log.append("Rolled ").append(Math.abs(roll));
                if (roll < 0) log.append(" (Cursed Reverse!)");
            }
            decrementStatusEffects(currentPlayer);

            int targetPos = currentPlayer.getPosition() + roll;

            // OutofBoundsException trigger
            if (targetPos > 100) {
                throw new OutofBoundsException("The gates of Heaven are closed to this roll! Stay at " + currentPlayer.getPosition());
            }
            if (targetPos < 1) targetPos = 1;

            currentPlayer.setPosition(targetPos);
            log.append("\nMoved to tile ").append(currentPlayer.getPosition());

            Tile landedTile = board.getTile(currentPlayer.getPosition() - 1);
            String effectMsg = landedTile.applyEffect(currentPlayer, this);
            log.append(effectMsg);

            handleSementedEffect(currentPlayer, roll, log);

            if (currentPlayer.hasSwitcheroo) {
                handleSwitcheroo(currentPlayer, log);
            }

            if (currentPlayer.getPosition() == 100) {
                winner = currentPlayer;
                gameRunning = false;
                log.append("\n*** ").append(currentPlayer.getName()).append(" HAS REACHED GLORY! ***");
            }

        } catch (InvalidMoveException e) {
            // This is now reachable!
            log.append("\n[!] INVALID ACTION: ").append(e.getMessage());
            // We do NOT call nextTurn() here if you want them to try again, 
            // OR we call it to punish the wasted attempt. Usually, in Foretold Fate, 
            // we let them roll normally instead:
            log.append("\nProphecy failed. Rolling naturally instead...");
            return processTurn(-1); 

        } catch (OutofBoundsException e) {
            log.append("\n").append(e.getMessage());
        }

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes

        p.setPosition(theirOldPos);
        target.setPosition(myOldPos);

<<<<<<< Updated upstream
<<<<<<< Updated upstream
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
=======
        log.append("\n(Switcheroo) Fate intervened! You swapped places with ")
           .append(target.getName())
           .append(" (Tile ").append(theirOldPos).append(" <-> ").append(myOldPos).append(").");
    } else {
        log.append("\n(Switcheroo) You are already leading the pack; there is no one ahead to swap with.");
    }

=======
        log.append("\n(Switcheroo) Fate intervened! You swapped places with ")
           .append(target.getName())
           .append(" (Tile ").append(theirOldPos).append(" <-> ").append(myOldPos).append(").");
    } else {
        log.append("\n(Switcheroo) You are already leading the pack; there is no one ahead to swap with.");
    }

>>>>>>> Stashed changes
    // 3. Always consume the effect
    p.hasSwitcheroo = false;
}

<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println("Next turn: " + players.get(currentPlayerIndex).getName());
    }

<<<<<<< Updated upstream
<<<<<<< Updated upstream
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
=======
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
}
>>>>>>> Stashed changes
=======
}
>>>>>>> Stashed changes
