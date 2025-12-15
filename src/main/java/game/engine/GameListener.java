package game.engine;

/**
 * Observer interface for UI or logging layers.
 * All methods are OPTIONAL via default implementations.
 */
public interface GameListener {

    /* -------- TURN FLOW -------- */

    default void onTurnStarted(Player player) {}

    default void onDiceRolled(Player player, int value) {}

    void onPlayerMoved(Player p, int from, int to);



    default void onTurnEnded(Player player) {}


    /* -------- EFFECTS -------- */

    default void onEffectsUpdated(Player player) {}


    /* -------- GAME END -------- */

    default void onGameEnded(Player winner) {}
}
