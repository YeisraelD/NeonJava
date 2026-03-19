package com.poker.ui;

import com.poker.core.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokerGUI extends JFrame {
    private Deck deck;
    private JPanel dealerPanel;
    private JPanel playerPanel;
    private JLabel dealerStrength;
    private JLabel playerStrength;
    private JLabel resultLabel;
    private JLabel chipsLabel;
    private JButton mainButton;

    private List<Card> playerCards;
    private List<Card> dealerCards;
    private boolean[] heldCards = new boolean[5];
    private boolean isDrawPhase = false;
    private int chips = 1000;
    private static final int BET_SIZE = 10;

    public PokerGUI() {
        setTitle("Java Poker Game - Strategy & Chips");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        deck = new Deck();
        deck.shuffle();

        // Top Info Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(50, 50, 50));
        chipsLabel = new JLabel("Chips: " + chips, SwingConstants.LEFT);
        chipsLabel.setForeground(Color.YELLOW);
        chipsLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        chipsLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topBar.add(chipsLabel, BorderLayout.WEST);

        JLabel helpText = new JLabel("Strategy: Click cards to HOLD the best ones before DRAWING!", SwingConstants.RIGHT);
        helpText.setForeground(Color.WHITE);
        helpText.setFont(new Font("SansSerif", Font.ITALIC, 14));
        helpText.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topBar.add(helpText, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Main Container
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        add(mainPanel, BorderLayout.CENTER);

        // Dealer Section
        JPanel dealerContainer = new JPanel(new BorderLayout());
        dealerContainer.setBorder(BorderFactory.createTitledBorder("Dealer's Hand"));
        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        dealerStrength = new JLabel(" ", SwingConstants.CENTER);
        dealerContainer.add(dealerPanel, BorderLayout.CENTER);
        dealerContainer.add(dealerStrength, BorderLayout.SOUTH);
        mainPanel.add(dealerContainer);

        // Player Section
        JPanel playerContainer = new JPanel(new BorderLayout());
        playerContainer.setBorder(BorderFactory.createTitledBorder("Your Hand - Use Auto-Hold or Click to Choose Strategy"));
        playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        playerStrength = new JLabel(" ", SwingConstants.CENTER);
        playerContainer.add(playerPanel, BorderLayout.CENTER);
        playerContainer.add(playerStrength, BorderLayout.SOUTH);
        mainPanel.add(playerContainer);

        // Controls
        JPanel controlPanel = new JPanel(new BorderLayout());
        resultLabel = new JLabel("Ready? -10 chips per deal", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Serif", Font.BOLD, 32));
        resultLabel.setForeground(new Color(0, 102, 204));
        controlPanel.add(resultLabel, BorderLayout.NORTH);

        mainButton = new JButton("Deal Hand (" + BET_SIZE + " chips)");
        mainButton.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainButton.addActionListener(e -> handleButtonClick());
        controlPanel.add(mainButton, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void handleButtonClick() {
        if (!isDrawPhase) {
            if (chips < BET_SIZE) {
                JOptionPane.showMessageDialog(this, "You're out of chips! Resetting to 1000.");
                chips = 1000;
                chipsLabel.setText("Chips: " + chips);
            }
            startNewGame();
        } else {
            performDraw();
        }
    }

    private void startNewGame() {
        chips -= BET_SIZE;
        chipsLabel.setText("Chips: " + chips);

        if (deck.size() < 10) {
            deck = new Deck();
            deck.shuffle();
        }

        playerCards = deck.deal(5);
        dealerCards = deck.deal(5);
        
        // Auto-Hold Logic: Keep pairs or better
        applyAutoHold();

        updatePlayerUI();
        dealerPanel.removeAll();
        for (int i = 0; i < 5; i++) {
            dealerPanel.add(createBackCard());
        }
        dealerStrength.setText("Dealer is evaluating...");
        resultLabel.setText("Look! We AUTO-HELD your pairs. Adjust if needed!");
        resultLabel.setForeground(Color.DARK_GRAY);
        
        mainButton.setText("Draw Cards (Final Chance)");
        isDrawPhase = true;
        
        revalidate();
        repaint();
    }

    private void applyAutoHold() {
        Map<Rank, Integer> counts = new HashMap<>();
        for (Card c : playerCards) {
            counts.put(c.getRank(), counts.getOrDefault(c.getRank(), 0) + 1);
        }
        
        for (int i = 0; i < 5; i++) {
            Rank r = playerCards.get(i).getRank();
            heldCards[i] = counts.get(r) >= 2;
        }
    }

    private void performDraw() {
        for (int i = 0; i < 5; i++) {
            if (!heldCards[i]) {
                playerCards.set(i, deck.deal());
            }
        }

        updatePlayerUI();
        
        Hand dealerHand = new Hand(dealerCards);
        Hand playerHand = new Hand(playerCards);

        dealerPanel.removeAll();
        for (Card card : dealerCards) {
            dealerPanel.add(createCardLabel(card, -1));
        }
        dealerStrength.setText(dealerHand.getStrengthLabel());
        playerStrength.setText(playerHand.getStrengthLabel());

        int result = playerHand.compareTo(dealerHand);
        int reward = calculateReward(playerHand, result);
        chips += reward;
        chipsLabel.setText("Chips: " + chips);

        // Clear held status for final display
        for (int i = 0; i < 5; i++) heldCards[i] = false;
        updatePlayerUI();
        
        if (result > 0) {
            resultLabel.setText("YOU WIN! +" + reward + " chips");
            resultLabel.setForeground(new Color(0, 153, 0));
        } else if (result < 0) {
            resultLabel.setText("DEALER WINS!");
            resultLabel.setForeground(Color.RED);
        } else {
            chips += BET_SIZE; // Refund bet
            chipsLabel.setText("Chips: " + chips);
            resultLabel.setText("IT'S A TIE! Bet returned.");
            resultLabel.setForeground(Color.BLACK);
        }

        mainButton.setText("Deal Again (Next Strategy)");
        isDrawPhase = false;
        
        revalidate();
        repaint();
    }

    private int calculateReward(Hand playerHand, int outcome) {
        if (outcome <= 0) return 0;
        
        // Multipliers based on hand strength
        switch (playerHand.getCategory()) {
            case ROYAL_FLUSH: return 500;
            case STRAIGHT_FLUSH: return 200;
            case FOUR_OF_A_KIND: return 100;
            case FULL_HOUSE: return 50;
            case FLUSH: return 40;
            case STRAIGHT: return 30;
            case THREE_OF_A_KIND: return 20;
            case TWO_PAIR: return 15;
            case PAIR: return 10;
            default: return 5;
        }
    }

    private void updatePlayerUI() {
        playerPanel.removeAll();
        for (int i = 0; i < playerCards.size(); i++) {
            playerPanel.add(createCardLabel(playerCards.get(i), i));
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    private JLabel createCardLabel(Card card, int index) {
        JLabel label = new JLabel(card.toString());
        label.setFont(new Font("Monospaced", Font.BOLD, 48));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setPreferredSize(new Dimension(100, 140));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (index != -1 && heldCards[index]) {
            label.setBorder(BorderFactory.createLineBorder(new Color(0, 204, 0), 6));
            label.setBackground(new Color(230, 255, 230));
        } else {
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }

        if (card.getSuit() == Suit.HEARTS || card.getSuit() == Suit.DIAMONDS) {
            label.setForeground(Color.RED);
        } else {
            label.setForeground(Color.BLACK);
        }

        if (index != -1 && isDrawPhase) {
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    heldCards[index] = !heldCards[index];
                    updatePlayerUI();
                }
            });
        }

        return label;
    }

    private JLabel createBackCard() {
        JLabel label = new JLabel("?");
        label.setFont(new Font("Monospaced", Font.BOLD, 48));
        label.setOpaque(true);
        label.setBackground(new Color(50, 80, 200));
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(100, 140));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PokerGUI().setVisible(true));
    }
}
