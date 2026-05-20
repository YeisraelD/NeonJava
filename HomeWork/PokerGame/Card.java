
public class Card { // single player card
    private int id; // from 0 to 51

    public Card(int id) {
        this.id = id;
    }

    public int getRank() { // 0-12
        return id % 13;
    }

    public int getSuit() { // 0-3
        return id / 13;
    }

    public String toString() { // kinda to make a readable card name
        String[] ranks = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace" };
        String[] suits = { "Spades", "Hearts", "Diamonds", "Clubs" };
        return ranks[getRank()] + "of " + suits[getSuit()];
    }
}
