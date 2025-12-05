package game.engine;

import java.util.Random;
import java.util.Scanner;

public class Dice {
	
	private static Player player;
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int roll = DiceRollRNG();
        System.out.println("Final roll: " + roll);
    }

    // Dice Roller
    public static int DiceRollRNG() {
        Random rand = new Random();
        int roll = rand.nextInt(6) + 1; // 1–6
        
		roll = player  != null ? CheckProperties(roll, player) : roll; //If player innit null, run condition
        return roll;
    }

    public static int CheckProperties(int roll, Player player) {     // player effects
    	
    	if (player == null) return roll; //returns if player is null

        if (player.hasWhatAreTheOdds) { // What Are The Odds?
            if (roll % 2 == 0) {
                return roll;      // even -> move forward
            } else {
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
            roll = Math.max(0, roll - 2);
        }
        return roll;
    }
}

