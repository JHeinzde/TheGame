package core.actions;

import core.CardStack;
import core.exceptions.InvalidActionException;
import main.GameController;

import java.util.List;

/**
 * This class encapsulates the logic to stop a game.
 * It returns simple string messages. They are terminated by a SINGLE \n. So this program is linefield only.
 */
public class StopAction implements Action {

    private static final String GAME_WON_MESSAGE = "Congratulations you beat The Game!\n";
    private static final String GAME_LOST_MESSAGE = "Sorry you lost against The Game :( !\n";
    private static final String INVALID_ACTION_MESSAGE = "You can't make this action, please try again!\n";

    private final GameController gameController;

    public StopAction(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public String execute(GameController gameController) {
        try {
            var state = gameController.endTurn();
            return returnStateMessage(state);
        } catch (InvalidActionException e) {
            return INVALID_ACTION_MESSAGE;
        }
    }

    private String returnStateMessage(ActionResult state) {
        if (state == ActionResult.GAME_WON) {
            return GAME_WON_MESSAGE;
        } else if (state == ActionResult.GAME_LOST) {
            return GAME_LOST_MESSAGE;
        } else if (state == ActionResult.TURN_END) {
            return turnEndMessage();
        }
        return ""; // This can never happen.
    }


    private String turnEndMessage() {
        var state = gameController.getGameState();
        StringBuilder message = new StringBuilder();

        appendDownCards(message, state.getDownStacks());
        appendUpStacks(message, state.getUpStacks());
        appendRemainingCars(message, state.remainingCards());

        return message.toString();
    }

    private void appendDownCards(StringBuilder message, List<CardStack> downStacks) {
        message.append("Down stacks top cards: ");
        appendStacks(message, downStacks);
    }

    private void appendUpStacks(StringBuilder message, List<CardStack> upStacks) {
        message.append("Up stacks top cards: ");
        appendStacks(message, upStacks);
    }

    private void appendStacks(StringBuilder message, List<CardStack> stacks) {
        stacks.forEach(stack -> message
                .append(stack.peekTop().getValue())
                .append(" "));
        message.append("\n");
    }

    private void appendRemainingCars(StringBuilder message, Integer remainingCards) {
        message.append("The amount of cards remaining in the deck is: ")
                .append(remainingCards)
                .append("\n");
    }
}
