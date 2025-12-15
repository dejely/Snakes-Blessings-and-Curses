package game.engine;

import java.util.Random;
import java.util.Scanner;

public class Dice {
    private final Random random = new Random();

    public int roll() {
        return random.nextInt(6) + 1;
    }
}