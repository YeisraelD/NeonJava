
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class pokerGUI extends Application {
    private PokerLogic engine = new PokerLogic();
    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> machineHand;

    private HBox machineCardsBox;
    private HBox playerCardsBox;
    private Label statusLabel;
    private Button callButton;
    private Button foldButton;
    private Button nextButton;

    @Override
    public void start(Stage window) {
        window.setTitle("Poker: Heads-Up Showdown");

        // Title
        Label titleLabel = new Label("POKER SHOWDOWN");
        titleLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px; -fx-font-weight: bold; -fx-letter-spacing: 2px;");

        // Machine Cards Section
        Label machineLabel = new Label("MACHINE'S HAND");
        machineLabel.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 12px; -fx-font-weight: bold;");
        machineCardsBox = new HBox(15);
        machineCardsBox.setAlignment(Pos.CENTER);

        VBox machineArea = new VBox(8, machineLabel, machineCardsBox);
        machineArea.setAlignment(Pos.CENTER);

        // Player Cards Section
        Label playerLabel = new Label("YOUR HAND");
        playerLabel.setStyle("-fx-text-fill: #a0a0a0; -fx-font-size: 12px; -fx-font-weight: bold;");
        playerCardsBox = new HBox(15);
        playerCardsBox.setAlignment(Pos.CENTER);

        VBox playerArea = new VBox(8, playerLabel, playerCardsBox);
        playerArea.setAlignment(Pos.CENTER);

        // Status Banner
        statusLabel = new Label("Place your choice: Call or Fold!");
        statusLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10;");

        // Control Buttons
        callButton = new Button("CALL");
        callButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 5; -fx-cursor: hand;");
        callButton.setOnAction(e -> handleCall());

        foldButton = new Button("FOLD");
        foldButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 5; -fx-cursor: hand;");
        foldButton.setOnAction(e -> handleFold());

        nextButton = new Button("DEAL NEXT HAND");
        nextButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand;");
        nextButton.setOnAction(e -> startNewRound());
        nextButton.setDisable(true);

        HBox actionBox = new HBox(15, callButton, foldButton, nextButton);
        actionBox.setAlignment(Pos.CENTER);

        // Layout Container
        VBox root = new VBox(25, titleLabel, machineArea, statusLabel, playerArea, actionBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1b4d3e;"); // Felt green table background

        startNewRound();

        Scene scene = new Scene(root, 500, 550);
        window.setScene(scene);
        window.setResizable(false);
        window.show();
    }

    private void startNewRound() {
        deck = engine.generateDeck();
        playerHand = new ArrayList<>();
        machineHand = new ArrayList<>();

        playerHand.add(deck.remove(0));
        playerHand.add(deck.remove(0));
        machineHand.add(deck.remove(0));
        machineHand.add(deck.remove(0));

        // Render Cards
        updateCardDisplays(false);

        statusLabel.setText("Do you Call or Fold?");
        statusLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 16px; -fx-font-weight: bold;");

        callButton.setDisable(false);
        foldButton.setDisable(false);
        nextButton.setDisable(true);
    }

    private void updateCardDisplays(boolean revealMachine) {
        machineCardsBox.getChildren().clear();
        playerCardsBox.getChildren().clear();

        // Render Machine Cards (face down if not revealed)
        for (Card card : machineHand) {
            machineCardsBox.getChildren().add(createCardNode(card, revealMachine));
        }

        // Render Player Cards (always face up)
        for (Card card : playerHand) {
            playerCardsBox.getChildren().add(createCardNode(card, true));
        }
    }

    private void handleCall() {
        callButton.setDisable(true);
        foldButton.setDisable(true);
        nextButton.setDisable(false);

        // Reveal the machine's cards
        updateCardDisplays(true);

        int playerScore = engine.evaluateHand(playerHand);
        int machineScore = engine.evaluateHand(machineHand);

        String result;
        if (playerScore > machineScore) {
            result = "You Win! " + describeHandScore(playerScore) + " beats " + describeHandScore(machineScore);
            statusLabel.setStyle("-fx-text-fill: #81c784; -fx-font-size: 16px; -fx-font-weight: bold;");
        } else if (machineScore > playerScore) {
            result = "Machine Wins! " + describeHandScore(machineScore) + " beats " + describeHandScore(playerScore);
            statusLabel.setStyle("-fx-text-fill: #e57373; -fx-font-size: 16px; -fx-font-weight: bold;");
        } else {
            result = "It's a Tie! Both have " + describeHandScore(playerScore);
            statusLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 16px; -fx-font-weight: bold;");
        }

        statusLabel.setText(result);
    }

    private void handleFold() {
        callButton.setDisable(true);
        foldButton.setDisable(true);
        nextButton.setDisable(false);

        // Reveal Machine Cards
        updateCardDisplays(true);

        statusLabel.setText("You folded. Machine wins by default!");
        statusLabel.setStyle("-fx-text-fill: #ffb74d; -fx-font-size: 16px; -fx-font-weight: bold;");
    }

    private String describeHandScore(int score) {
        String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace" };
        if (score >= 100) {
            int rankIndex = score - 100;
            return "a Pair of " + ranks[rankIndex] + "s";
        } else {
            return ranks[score] + " High";
        }
    }

    private VBox createCardNode(Card card, boolean faceUp) {
        VBox cardBox = new VBox();
        cardBox.setPrefSize(90, 130);
        cardBox.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #b0bec5; -fx-border-radius: 8; -fx-border-width: 2; -fx-padding: 8;");
        cardBox.setAlignment(Pos.CENTER);

        if (!faceUp) {
            // Visual Card Back (red geometric gradient pattern)
            cardBox.setStyle("-fx-background-color: linear-gradient(to bottom, #d32f2f, #b71c1c); -fx-background-radius: 8; -fx-border-color: #ffffff; -fx-border-radius: 8; -fx-border-width: 2;");
            Label backLabel = new Label("♠\n♥\n♦\n♣");
            backLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.4); -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center;");
            cardBox.getChildren().add(backLabel);
            return cardBox;
        }

        String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
        String[] suitSymbols = { "♠", "♥", "♦", "♣" };
        String suitColor = (card.getSuit() == 1 || card.getSuit() == 2) ? "#d32f2f" : "#212121";

        Label rankLabel = new Label(ranks[card.getRank()]);
        rankLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + suitColor + ";");

        Label suitLabel = new Label(suitSymbols[card.getSuit()]);
        suitLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: " + suitColor + ";");

        HBox topRow = new HBox(rankLabel);
        topRow.setAlignment(Pos.TOP_LEFT);

        HBox centerRow = new HBox(suitLabel);
        centerRow.setAlignment(Pos.CENTER);
        VBox.setVgrow(centerRow, Priority.ALWAYS);

        cardBox.getChildren().addAll(topRow, centerRow);
        return cardBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
