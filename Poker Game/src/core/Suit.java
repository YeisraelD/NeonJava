package com.poker.core;

public enum Suit {
    CLUBS("Clubs", "♣"),
    DIAMONDS("Diamonds", "♦"),
    HEARTS("Hearts", "♥"),
    SPADES("Spades", "♠");

    private final String name;
    private final String symbol;

    Suit(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
