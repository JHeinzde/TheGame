package main;

import core.*;
import core.actions.Action;
import core.actions.ActionResult;
import core.exceptions.InvalidActionException;

import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;

/**
 * Encapsulates bootstrapping etc.
 * TODO: Make multiplayer ready currently is only single player.
 */
public class GameController {

    private static final Integer UP_STACK_ONE = 1;
    private static final Integer UP_STACK_TWO = 2;
    private static final Integer DOWN_STACK_ONE = 3;
    private static final Integer DOWN_STACK_TWO = 4;

    private final List<CardStack> upStacks;
    private final List<CardStack> downStacks;
    private final Stack<Action> actionHistory = new Stack<>();
    private final Deck deck;
    private final Hand playerHand;
    private Integer cardsPlayed;

    public GameController() {
        upStacks = List.of(new CardStack(Direction.UP), new CardStack(Direction.UP));
        downStacks = List.of(new CardStack(Direction.DOWN), new CardStack(Direction.DOWN));

        deck = new Deck();
        playerHand = new Hand(8); // The maxSize depends on the amount of players

        // Draw the first hand
        playerHand.draw(deck);
        cardsPlayed = 0;
    }


    public ActionResult endTurn() throws InvalidActionException {
        if (cardsPlayed < 2 && playerHand.canMakeMove(upStacks, downStacks)) {
            throw new InvalidActionException("Illegal end of move!");
        } else if (gameIsWon()) {
            return ActionResult.GAME_WON;
        } else if (gameIsLost()) {
            return ActionResult.GAME_LOST;
        } else {
            playerHand.draw(deck);
            cardsPlayed = 0;
            return ActionResult.TURN_END;
        }
    }

    public boolean gameIsLost() {
        return cardsPlayed < 2 && !playerHand.canMakeMove(upStacks, downStacks);
    }

    public boolean gameIsWon() {
        return playerHand.getHandSize() == 0 && deck.remaining() == 0;
    }

    public void playCard(Card card, Integer targetStack) throws InvalidActionException {
        if (targetStack.equals(UP_STACK_ONE))
            pushOnStack(upStacks.get(0), card);
        else if (targetStack.equals(UP_STACK_TWO))
            pushOnStack(upStacks.get(1), card);
        else if (targetStack.equals(DOWN_STACK_ONE))
            pushOnStack(downStacks.get(0), card);
        else if (targetStack.equals(DOWN_STACK_TWO))
            pushOnStack(downStacks.get(1), card);
        else
            throw new InvalidActionException("No target stack matching the supplied integer");

    }

    public State getGameState() {
        return new State(List.copyOf(upStacks), List.copyOf(downStacks), playerHand.copyHand(), deck.remaining());
    }

    public void addActionToHistory(Action action) {
        this.actionHistory.push(action);
    }

    private void pushOnStack(CardStack cards, Card card) throws InvalidActionException {
        cards.push(card);
        playerHand.removeHandCard(card);
        cardsPlayed += 1;
    }

}


