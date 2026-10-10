import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int maxAttempts = 7;   // max 7 attempts per round
        int round = 0;         // rounds played
        int totalWins = 0;     // total wins
        String playAgain;

        System.out.println("=== Welcome to Number Guessing Game ===");

        do {
            round++;
            int number = random.nextInt(100) + 1;  // random number 1 to 100
            int attempts = 0;
            boolean won = false;

            System.out.println("\n--- Round " + round + " ---");
            System.out.println("I have picked a number between 1 and 100.");
            System.out.println("You have " + maxAttempts + " attempts.");

            while (attempts < maxAttempts) {
                System.out.print("Attempt " + (attempts + 1) + "/" + maxAttempts + " - Enter your guess: ");

                // ask again if input is not a number
                if (!sc.hasNextInt()) {
                    System.out.println("Please enter a valid number!");
                    sc.next();
                    continue;
                }

                int guess = sc.nextInt();
                attempts++;

                if (guess == number) {
                    System.out.println("Correct!");
                    won = true;
                    break;
                } else if (guess > number) {
                    System.out.println("Too High!");
                } else {
                    System.out.println("Too Low!");
                }
            }

            if (won) {
                totalWins++;
                System.out.println("Round " + round + " - guessed in " + attempts + " attempts.");
            } else {
                System.out.println("You Lost! The number was " + number);
            }

            System.out.println("Score: " + totalWins + " win(s) out of " + round + " round(s).");

            System.out.print("Play again? (yes/no): ");
            playAgain = sc.next();

        } while (playAgain.equalsIgnoreCase("yes"));

        System.out.println("Thanks for playing! Goodbye.");
        sc.close();
    }
}