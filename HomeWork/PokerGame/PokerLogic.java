
package HomeWork.PokerGame;

import java.util.*;

public class PokerLogic {

    public List<Card> generateDeck() { // creat and shuffle deck of 52cards
        List<Card> deck = new ArrayList<>(); // to store the deck
        for (int i = 0; i < 52; i++) {
            deck.add(new Card(i));
        }
        Collections.shuffle(deck);// randomly
        return deck;
    }

    public int evaluateHand(List<Card> hand) {
        HashMap<Integer, Integer> rankCounts = new HashMap<>();
        int highCard = 0;
        for (Card card : hand) {
            int rank = card.getRank();
            // update the cunt for this rank in the map
            rankCounts.put(rank, rankCounts.getOrDefault(rank, 0) + 1);
            if (rank > highCard) {
                highCard = rank;
            }
        }

        int score = highCard;
        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 3) {
                score = Math.max(score, 200 + entry.getKey());
            } else if (entry.getValue() == 2) {

                score = Math.max(score, 100 + entry.getKey());
            }
        }
        return score;
    }
}
