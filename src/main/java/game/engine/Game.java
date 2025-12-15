package game.engine;

import java.util.*;
import java.util.List;
import java.util.Random;

public class Game {
    private final List<Player> players;
    private final Board board;
    private final Dice dice = new Dice();
    private int currentIndex = 0;
    private boolean finished = false;

    public Game(List<Player> players, Board board) {
        if (players.size() < 2 || players.size() > 4) {
            throw new IllegalArgumentException("Game supports 2–4 players");
        }
        this.players = players;
        this.board = board;
    }

    public void startGame() {
        while (!finished) {
            nextTurn();
        }
    }

    public void nextTurn() {
        Player current = players.get(currentIndex);

        current.startTurn(this);

        int roll = dice.roll();
        roll = current.applyDiceModifiers(roll);

        current.move(roll);

        Tile tile = board.getTile(current.getPosition());
        tile.onLand(current, this);

        resolveBattles();

        current.endTurn(this);

        checkWinCondition(current);

        advanceTurn();
    }

    private void resolveBattles() {
        Map<Integer, List<Player>> byPosition = new HashMap<>();

        for (Player p : players) {
            byPosition.computeIfAbsent(p.getPosition(), k -> new ArrayList<>()).add(p);
        }

        for (List<Player> group : byPosition.values()) {
            if (group.size() > 1) {
                resolveBattle(group);
            }
        }
    }

    private void resolveBattle(List<Player> contenders) {
        Map<Player, Integer> rolls = new HashMap<>();
        for (Player p : contenders) {
            rolls.put(p, dice.roll());
        }

        int max = Collections.max(rolls.values());

        for (Map.Entry<Player, Integer> e : rolls.entrySet()) {
            int diff = max - e.getValue();
            if (diff > 0) {
                e.getKey().move(-diff);
            }
        }
    }

    private void checkWinCondition(Player p) {
        if (p.getPosition() >= 100) {
            finished = true;
        }
    }

    private void advanceTurn() {
        currentIndex = (currentIndex + 1) % players.size();
    }
}
