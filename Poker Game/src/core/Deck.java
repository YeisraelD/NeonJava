package com.poker.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    public List<Card> deal(int num) {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Card card = deal();
            if (card != null) {
                hand.add(card);
            }
        }
        return hand;
    }

    public int size() {
        return cards.size();
    }
}
