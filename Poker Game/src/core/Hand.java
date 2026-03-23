package core;

import java.util.*;

public class Hand implements Comparable<Hand> {
    public enum HandCategory {
        HIGH_CARD(1), 
        PAIR(2), 
        TWO_PAIR(3), 
        THREE_OF_A_KIND(4), 
        STRAIGHT(5), 
        FLUSH(6), 
        FULL_HOUSE(7), 
        FOUR_OF_A_KIND(8), 
        STRAIGHT_FLUSH(9), 
        ROYAL_FLUSH(10);

        private final int value;
        HandCategory(int v) { this.value = v; }
        public int getValue() { return value; }
    }

    private final List<Card> cards;
    private String strengthLabel;
    private HandCategory category;

    public Hand(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        Collections.sort(this.cards);
        evaluate();
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getStrengthLabel() {
        return strengthLabel;
    }

    public HandCategory getCategory() {
        return category;
    }

    private void evaluate() {
        if (cards.size() != 5) {
            strengthLabel = "Incomplete Hand";
            category = HandCategory.HIGH_CARD;
            return;
        }

        Map<Rank, Integer> rankCounts = new HashMap<>();
        Map<Suit, Integer> suitCounts = new HashMap<>();

        for (Card card : cards) {
            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
            suitCounts.put(card.getSuit(), suitCounts.getOrDefault(card.getSuit(), 0) + 1);
        }

        boolean flush = suitCounts.size() == 1;
        boolean straight = isStraight();

        if (flush && straight) {
            if (cards.get(0).getRank() == Rank.TEN) {
                strengthLabel = "Royal Flush";
                category = HandCategory.ROYAL_FLUSH;
            } else {
                strengthLabel = "Straight Flush";
                category = HandCategory.STRAIGHT_FLUSH;
            }
        } else if (rankCounts.containsValue(4)) {
            strengthLabel = "Four of a Kind";
            category = HandCategory.FOUR_OF_A_KIND;
        } else if (rankCounts.containsValue(3) && rankCounts.containsValue(2)) {
            strengthLabel = "Full House";
            category = HandCategory.FULL_HOUSE;
        } else if (flush) {
            strengthLabel = "Flush";
            category = HandCategory.FLUSH;
        } else if (straight) {
            strengthLabel = "Straight";
            category = HandCategory.STRAIGHT;
        } else if (rankCounts.containsValue(3)) {
            strengthLabel = "Three of a Kind";
            category = HandCategory.THREE_OF_A_KIND;
        } else if (countPairs(rankCounts) == 2) {
            strengthLabel = "Two Pair";
            category = HandCategory.TWO_PAIR;
        } else if (countPairs(rankCounts) == 1) {
            strengthLabel = "Pair of " + getHighestPairRank(rankCounts).getDisplay();
            category = HandCategory.PAIR;
        } else {
            strengthLabel = "High Card: " + cards.get(cards.size() - 1).getRank();
            category = HandCategory.HIGH_CARD;
        }
    }

    @Override
    public int compareTo(Hand other) {
        if (this.category != other.category) {
            return Integer.compare(this.category.getValue(), other.category.getValue());
        }
        // Tie-breaker: compare ranks high-to-low
        for (int i = 4; i >= 0; i--) {
            int comp = Integer.compare(this.cards.get(i).getRank().getValue(), other.cards.get(i).getRank().getValue());
            if (comp != 0) return comp;
        }
        return 0;
    }

    private boolean isStraight() {
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i + 1).getRank().getValue() != cards.get(i).getRank().getValue() + 1) {
                // Check for low Ace straight (A-2-3-4-5)
                if (i == 3 && cards.get(4).getRank() == Rank.ACE && cards.get(0).getRank() == Rank.TWO &&
                    cards.get(1).getRank() == Rank.THREE && cards.get(2).getRank() == Rank.FOUR && 
                    cards.get(3).getRank() == Rank.FIVE) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    private int countPairs(Map<Rank, Integer> rankCounts) {
        int pairs = 0;
        for (int count : rankCounts.values()) {
            if (count == 2) pairs++;
        }
        return pairs;
    }

    private Rank getHighestPairRank(Map<Rank, Integer> rankCounts) {
        Rank highest = null;
        for (Map.Entry<Rank, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 2) {
                if (highest == null || entry.getKey().getValue() > highest.getValue()) {
                    highest = entry.getKey();
                }
            }
        }
        return highest;
    }

    @Override
    public String toString() {
        return cards.toString() + " -> " + strengthLabel;
    }
}
