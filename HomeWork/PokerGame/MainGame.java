
import java.util.*;

public class MainGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("=== Poker Game Menu ===");
            System.out.println("1. Play a Round");
            System.out.println("2. Exit");
            System.out.print("Select an option: ");
            String menuChoice = scanner.next();
            if (menuChoice.equals("1")) {
                PokerLogic engine = new PokerLogic();
                List<Card> deck = engine.generateDeck();
                List<Card> playerHand = new ArrayList<>();
                List<Card> aiHand = new ArrayList<>();
                playerHand.add(deck.remove(0));
                playerHand.add(deck.remove(0));
                aiHand.add(deck.remove(0));
                aiHand.add(deck.remove(0));
                System.out.println("Your hand: " + playerHand.get(0) + ", " + playerHand.get(1));
                System.out.print("Do you want to 'Call' or 'Fold'? ");
                String gameChoice = scanner.next().toLowerCase();
                if (gameChoice.equals("fold")) {
                    System.out.println("You folded. AI wins!");
                } else if (gameChoice.equals("call")) {
                    int playerScore = engine.evaluateHand(playerHand);
                    int aiScore = engine.evaluateHand(aiHand);
                    System.out.println("AI hand: " + aiHand.get(0) + ", " + aiHand.get(1));
                    if (playerScore > aiScore) {
                        System.out.println("You win!");
                    } else if (aiScore > playerScore) {
                        System.out.println("AI wins!");
                    } else {
                        System.out.println("It's a tie!");
                    }
                } else {
                    System.out.println("Invalid input. AI wins by default!");
                }
            } else if (menuChoice.equals("2")) {
                running = false;
                System.out.println("Thanks for playing!");
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
        scanner.close();
    }
}
