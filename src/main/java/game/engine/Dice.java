package game.engine;

import java.util.Random;

public class Dice {
    
    private static final Random rand = new Random();

    public static int rollSingleDie() {
        return rand.nextInt(6) + 1;
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
        if (player.hasShackled) {
            int newRoll = Math.max(0, rollSum - 2);
            player.hasShackled = false; 
            return newRoll;
        }

        return rollSum;
    }
}