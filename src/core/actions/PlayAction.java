package core.actions;

import core.Card;
import core.exceptions.InvalidActionException;
import core.exceptions.MalformedActionException;
import main.GameController;

public class PlayAction implements Action {

    private Card card;
    private Integer cardStack;

    public PlayAction(String[] action) {
        if (action.length != 2)
            throw new MalformedActionException("The array supplied to PlayAction was not size 2");

        try {
            parseCard(action[0]);
            parseCardStack(action[1]);
        } catch (RuntimeException rex) {
            throw new MalformedActionException("Could not parse action properly.");
        }
    }

    private void parseCard(String rawCard) {
        var sanitizedCard = rawCard.replace("C", "");
        card = new Card(Integer.parseInt(sanitizedCard));
        if (card.getValue() > 98 || card.getValue() < 2)
            throw new MalformedActionException("Card value needs to be between 98 and 2");
    }

    private void parseCardStack(String rawCardStack) {
        this.cardStack = Integer.parseInt(rawCardStack);
        if (cardStack < 1 || cardStack > 4)
            throw new MalformedActionException("Invalid card stack");
    }

    @Override
    public String execute(GameController gameController) {
        try {
            return playCard(gameController);
        } catch (InvalidActionException exception) {
            return StaticMessages.INVALID_ACTION_MESSAGE;
        }
    }

    private String playCard(GameController gameController) throws InvalidActionException {
        var gameState = gameController.getGameState();
        var hand = gameState.getHandCards();
        if (!hand.contains(card))
            throw new InvalidActionException("Your hand does not contain this card");

        gameController.playCard(card, cardStack);
        return "Played card: " + card.getValue() + " onto card stack: " + cardStack + "\n";
    }
}
