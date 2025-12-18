package game.engine;

public enum BlessingType {
    FORETOLD_FATE,       // Choose 1–10 steps on next turn
    DANIELS_BLESSING,    // Shuts all snakes’ mouths for 2 turns
    SWITCHEROO,          // Swap places with random player ahead
    JACOBS_LADDER,       // Nullifies next 2 cursed effects
    SHACKLED,            // Subtract 2 pips from roll (min 0)
    SEMENTED             // Link movement with a player ahead
}
