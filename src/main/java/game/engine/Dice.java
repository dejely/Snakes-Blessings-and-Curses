package game.engine;

import java.util.Random;
import java.util.Scanner;

public class Dice {
	
    private static final Random rand = new Random();
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Roll the dice for a given player
     * @param player The player rolling the dice
     * @return modified roll based on curses/blessings
     */
    public static int DiceRollRNG(Player player) {
        int roll = rand.nextInt(6) + 1; // 1–6
		roll = player  != null ? CheckProperties(roll, player) : roll; //If player innit null, run condition
        return roll;
    }

    public static int CheckProperties(int roll, Player player) {     // player effects
    	
    	if (player == null) return roll; //returns if player is null

        if (player.hasWhatAreTheOdds) { // What Are The Odds?
            if (roll % 2 == 0) {
                System.out.println(player.getName() + " rolled " + roll + " (even) → move forward");
                return roll;      // even -> move forward
            } else {
                System.out.println(player.getName() + " rolled " + roll + " (odd) → move backward");
                return -roll;     // odd -> move backward
            }
        }
        if (player.hasForetoldFate) { // Foretold Fate
            int steps;
            while (true) {
                System.out.print("Enter a number from 1–10: ");
                try {
                    steps = Integer.parseInt(sc.nextLine().trim());
                    if (steps < 1 || steps > 10) {
                        System.out.println("Number must be 1–10!");
                        continue;
                    }
                    return steps;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number!");
                }
            }
        }
        if (player.hasShackled) {// Shackled
            int newRoll = Math.max(0, roll - 2);
            System.out.println(player.getName() + " is shackled! Roll reduced from " + roll + " → " + newRoll);
        }
        return roll;
    }
}