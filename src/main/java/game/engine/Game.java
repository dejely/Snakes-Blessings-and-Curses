package game.engine;

import game.character.Skill;
import game.effects.*;

import java.util.*;

public class Game {

    private final List<Player> players;
    private final Board board;
    private final Dice dice = new Dice();
    private boolean hasRolledThisTurn = false;


    private final List<GameListener> listeners = new ArrayList<>();

    private int currentIndex = 0;
    private TurnPhase phase = TurnPhase.START;
    private boolean finished = false;

    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
    }

    /* =========================
       Read-only accessors
       ========================= */

    public Player getCurrentPlayer() {
        return players.get(currentIndex);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public TurnPhase getPhase() {
        return phase;
    }
    
    public Tile getTileAt(int position) {
        return board.getTile(position);
    }
    
    public Board getBoard() {
        return board;
    }


    /* =========================
       Observer support
       ========================= */

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }


    /* =========================
       Turn lifecycle
       ========================= */

    public void startTurn() {
        if (finished) return;

        hasRolledThisTurn = false;

        phase = TurnPhase.START;

        Player current = getCurrentPlayer();
        current.startTurn(this);

        phase = TurnPhase.SKILL;
        notifyTurnStarted(current);
    }



    


    public void activateSkill() {
        if (phase != TurnPhase.SKILL) return;

        Player current = getCurrentPlayer();
        Skill skill = current.getCharacter().getActiveSkill();

        if (!skill.canActivate(current, this)) return;

        skill.activate(current, this);

        // skill usage immediately moves to ROLL phase
        phase = TurnPhase.ROLL;
    }


    public void rollDice() {
        if (hasRolledThisTurn) return;
        if (phase != TurnPhase.ROLL && phase != TurnPhase.SKILL) return;

        hasRolledThisTurn = true;
        phase = TurnPhase.ROLL;

        Player current = getCurrentPlayer();
        int baseRoll = dice.roll();

        notifyDiceRolled(current, baseRoll);

        RollContext ctx = current.buildRollContext(this, baseRoll);

        phase = TurnPhase.MOVE;

        int from = current.getPosition();
        int steps = ctx.canMoveForward ? ctx.rollValue : -ctx.rollValue;
        current.move(steps);
        int to = current.getPosition();

        notifyPlayerMoved(current, from, to);

        board.getTile(current.getPosition()).onLand(current, this);

        resolveBattles();
        endTurn();
    }





    private void endTurn() {
        Player current = getCurrentPlayer();

        phase = TurnPhase.END;
        current.endTurn(this);

        if (current.getPosition() >= 100) {
            finished = true;
            notifyGameEnded(current);
            return;
        }

        currentIndex = (currentIndex + 1) % players.size();
        startTurn();
    }


    /* =========================
       PvP battle logic
       ========================= */

    private void resolveBattles() {
        Map<Integer, List<Player>> byPosition = new HashMap<>();

        for (Player p : players) {
            byPosition
                .computeIfAbsent(p.getPosition(), k -> new ArrayList<>())
                .add(p);
        }

        for (List<Player> group : byPosition.values()) {
        	
            if (group.size() > 1) {
                int maxRoll = 0;
                Map<Player, Integer> rolls = new HashMap<>();

                for (Player p : group) {
                    int roll = dice.roll();
                    rolls.put(p, roll);
                    maxRoll = Math.max(maxRoll, roll);
                }

                for (var entry : rolls.entrySet()) {
                	int diff = maxRoll - entry.getValue();
                	int from = entry.getKey().getPosition();
                	entry.getKey().move(-diff);
                	int to = entry.getKey().getPosition();

                	notifyPlayerMoved(entry.getKey(), from, to);

                    
                    if (diff > 0) {
                        entry.getKey().move(-diff);
                    }
                }
            }
        }
        
    }
    
    /* =========================
    	facade reader
    ========================= */
    
    public Map<Integer, Integer> getSnakes() {
        return board.getSnakes();
    }

    public Map<Integer, Integer> getVines() {
        return board.getVines();
    }
    
    
    
    
    private void notifyTurnStarted(Player p) {
        listeners.forEach(l -> l.onTurnStarted(p));
    }

    private void notifyDiceRolled(Player p, int value) {
        listeners.forEach(l -> l.onDiceRolled(p, value));
    }

    private void notifyPlayerMoved(Player p, int from, int to) {
        listeners.forEach(l -> l.onPlayerMoved(p, from, to));
    }

    private void notifyGameEnded(Player winner) {
        listeners.forEach(l -> l.onGameEnded(winner));
    }

}
