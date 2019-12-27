package core;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Implementation of a The game hand.
 * This hand can only draw and show its contents
 */
public class Hand {

    private final Set<Card> handCards;
    private final int maxSize;

    /**
     * Initializes the hand with the given maxSize.
     *
     * @param maxSize
     */
    public Hand(int maxSize) {
        this.maxSize = maxSize;
        this.handCards = new HashSet<>();
    }

    private Hand(int maxSize, Set<Card> handCards) {
        this.maxSize = maxSize;
        this.handCards = handCards;
    }


    /**
     * Draws cards from a deck until the maxSize of the hand is reached.
     *
     * @param deck The dock object that provides the cards to be drawn
     */
    public void draw(Deck deck) {
        while (handCards.size() < maxSize) {
            Optional<Card> card;
            if ((card = deck.draw()).isPresent()) {
                handCards.add(card.get());
            } else {
                break;
            }
        }
    }

    public Set<Card> getHandCards() {
        return new HashSet<>(handCards);
    }

    /**
     * Removes a card from the hand
     *
     * @param toRemove The card to be removed from this hand
     */
    public void removeHandCard(Card toRemove) {
        handCards.remove(toRemove);
    }

    /**
     * Returns the amount of cards contained in the hand currently
     *
     * @return The hand size
     */
    public int getHandSize() {
        return handCards.size();
    }

    /**
     * Returns a new copy for the same hand.
     *
     * @return A shallow copy of the hand.
     */
    public Hand copyHand() {
        return new Hand(this.maxSize, new HashSet<>(this.handCards));
    }

    public boolean contains(Card card) {
        return this.handCards.contains(card);
    }

    public boolean canMakeMove(List<CardStack> upStacks, List<CardStack> downStacks) {
        return checkStack((handCard, deckCard) -> handCard.getValue() > deckCard.getValue(), upStacks)
                || checkStack((handCard, deckCard) -> handCard.getValue() < deckCard.getValue(), downStacks);
    }

    private boolean checkStack(BiFunction<Card, Card, Boolean> comparison, List<CardStack> cardStacks) {

        for (var cardStack : cardStacks) {
            for (var card : handCards) {
                if (comparison.apply(card, cardStack.peekTop())) {
                    return true;
                }
            }
        }

        return false;
    }
}
