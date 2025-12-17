package game.engine;

import java.util.*;

public class Game {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        // Create players
        List<Player> players = new ArrayList<>();
        System.out.print("Enter number of players: ");
        int playerCount = Integer.parseInt(sc.nextLine());
        for (int i = 1; i <= playerCount; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            players.add(new Player(sc.nextLine().trim()));
        }

        // Create board
        Board board = new Board(50, 5, 5, 5, 5); // size=50, 5 snakes, 5 ladders, 5 curses, 5 blessings

        boolean gameOver = false;
        while (!gameOver) {
            for (Player player : players) {

                // Skip turn if Unmovable Man
                if (player.skipNextTurn) {
                    System.out.println(player.getName() + " skips this turn due to a curse!");
                    player.skipNextTurn = false; // consume
                    continue;
                }

                // Roll dice
                int roll = random.nextInt(6) + 1;

                // Apply curse/blessing effects via CheckProperties
                roll = Dice.CheckProperties(roll, player);

                // Move player
                player.move(board);

                // Handle end-of-turn counters
                if (player.barredHeavenTurns > 0) player.barredHeavenTurns--;
                if (player.blackoutTurns > 0) player.blackoutTurns--;
                if (player.snakesMouthShutTurns > 0) player.snakesMouthShutTurns--;

                // Check winning condition
                if (player.getPosition() == board.getSize() - 1) {
                    System.out.println(player.getName() + " wins!");
                    gameOver = true;
                    break;
                }
            }
        }

        sc.close();
    }
}
