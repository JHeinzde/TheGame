package main;

import core.CardStack;
import core.Hand;

import java.util.List;

/**
 * Read only view on the game state.
 */
public class State {
    private final List<CardStack> upStacks;
    private final List<CardStack> downStacks;
    private final Hand handCards;
    private final Integer remainingCards;


    public State(List<CardStack> upStacks, List<CardStack> downStacks, Hand handCards, Integer remainingCards) {
        this.upStacks = upStacks;
        this.downStacks = downStacks;
        this.handCards = handCards;
        this.remainingCards = remainingCards;
    }

    public List<CardStack> getUpStacks() {
        return upStacks;
    }

    public List<CardStack> getDownStacks() {
        return downStacks;
    }

    public Hand getHandCards() {
        return handCards;
    }

    public Integer remainingCards() {
        return remainingCards;
    }
}
