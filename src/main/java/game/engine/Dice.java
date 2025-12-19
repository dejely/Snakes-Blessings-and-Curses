package game.engine;

import java.util.Random;

/**
 * Utility class for handling all dice-related logic.
 */
public class Dice {
    private static final Random random = new Random();

    /**
     * Rolls a single 6-sided die.
     * Used by the engine for standard turns and the UI for animation blurs.
     * @return A random integer between 1 and 6.
     */
    public static int rollSingleDie() {
        return random.nextInt(6) + 1;
    }

    /**
     * Rolls two 6-sided dice and returns the total.
     * @return A random integer between 2 and 12.
     */
    public static int rollTwoDice() {
        return rollSingleDie() + rollSingleDie();
    }
    public static int applyModifiers(int rollSum, Player player) {
        // 1. What Are The Odds? (Active if counter > 0)
        if (player.whatAreTheOddsTurns > 0) { 
            if (rollSum % 2 == 0) {
                // Even = Forward
                return rollSum; 
            } else {
                // Odd = Backward
                return -rollSum;
            }
        }

        // 2. Shackled
        if (player.isShackled) {
            int newRoll = Math.max(0, rollSum - 2);
            player.isShackled = false; 
            return newRoll;
        }

        return rollSum;
    }
    public static String getDicePips(int value) {
        return switch (value) {
            case 1 -> "\u2680"; // ⚀
            case 2 -> "\u2681"; // ⚁
            case 3 -> "\u2682"; // ⚂
            case 4 -> "\u2683"; // ⚃
            case 5 -> "\u2684"; // ⚄
            case 6 -> "\u2685"; // ⚅
            default -> "?";
        };
    }
}