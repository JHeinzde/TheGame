package core;

import java.util.Optional;
import java.util.Random;

/**
 * Represents a deck of cards for the game
 */
public class Deck {

    private final Card[] cards;
    private int index = 0;

    public Deck() {
        cards = new Card[97];
        init();
    }


    private void init() {

        for (int i = 2; i < 99; i++) {
            cards[i - 2] = new Card(i);
        }

        shuffleDeck(180);
    }

    /**
     * Shuffle the deck for the amount of rounds given
     * @param rounds The amount of shuffle operations
     */
    private void shuffleDeck(int rounds) {
        Random rand = new Random();
        for(int i = 0 ; i < rounds; i++){
            int x = rand.nextInt(96);
            int y = rand.nextInt(96);

            swap(x, y);
        }
    }

    private void swap(int i, int j){
        var tmp = cards[i];
        cards[i] = cards[j];
        cards[j] = tmp;
    }

    public Optional<Card> draw() {
        if (index < 97) {
            return Optional.of(cards[index++]);
        } else {
            return Optional.empty();
        }
    }

    public Integer remaining() {
        return 97 - index;
    }
}
