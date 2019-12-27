package core;

import core.exceptions.InvalidActionException;

import java.util.Stack;

/**
 * Represents a stack of cards in the game
 */
public class CardStack {

    private final Direction direction;
    private final Stack<Card> cards;

    /**
     * Initializes the card stack with the given direction
     *
     * @param direction The direction to which the value of the cards should be going.
     */
    public CardStack(Direction direction) {
        this.direction = direction;
        this.cards = new Stack();

        switch (direction) {
            case UP:
                cards.push(new Card(1));
                break;
            case DOWN:
                cards.push(new Card(99));
                break;
        }
    }


    /**
     * Pushes a card onto the cardStack. The card must either be lower/greater depending on the direction(DOWN, UP).
     * If it goes into the opposite direction the distance needs to be exactly 10.
     *
     * @param card The card that should be pushed to the stack
     */
    public void push(Card card) throws InvalidActionException {
        if (direction == Direction.UP && cards.peek().getValue() - card.getValue() > 0) {
            if (Math.abs(cards.peek().getValue() - card.getValue()) == 10) {
                cards.push(card);
            } else {
                throw new InvalidActionException("Can't add card, distance needs to be exactly 10");
            }
        }

        if (direction == Direction.DOWN && cards.peek().getValue() - card.getValue() < 0) {
            if (Math.abs(cards.peek().getValue() - card.getValue()) == 10) {
                cards.push(card);
            } else {
                throw new InvalidActionException("Can't add card, distance needs to be exactly 10");
            }
        }

        cards.push(card);
    }

    /**
     * Calculates the distance to the top card.
     *
     * @param card The card the distance is calculated to
     * @return The distance of the top card.
     */
    public Integer getDistanceToTop(Card card) {
        return Math.abs(this.cards.peek().getValue() - card.getValue());
    }

    /**
     * Provides access to the top card of the stack.
     *
     * @return The top of the card stack
     */
    public Card peekTop() {
        return this.cards.peek();
    }
}
