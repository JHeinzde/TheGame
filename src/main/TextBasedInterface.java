package main;

import core.CardStack;
import core.Hand;
import core.actions.Action;
import core.actions.PlayAction;
import core.actions.StaticMessages;
import core.actions.StopAction;
import core.exceptions.MalformedActionException;
import fnlib.Maybe;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class encapsulates the ability to input moves etc into the game engine.
 * By default it uses System.in/System.out to read and write to a console window. These
 * can be redirected since the Constructor takes two optional parameters to achieve this
 */
public class TextBasedInterface {

    /**
     * This regex defines the actions. It is either S for stopping and initiating a new turn or C followed by the cards value
     * (2-98) and then follow by a hypen the number of the stack the card should be pushed on.
     */
    private static final Pattern ACTION_TEMPLATE = Pattern.compile("(C[0-9]{1,2}-[1-4])|S");

    // Defined input and output for the cli interface to the game.
    private final Scanner scanner;
    private final PrintWriter writer;
    private GameController gameController;

    /**
     * This constructor uses the inputStream/outputStream that is given by the caller
     *
     * @param inputStream  The inputStream to be used. Should not be null
     * @param outputStream The outputStream to be used. Should not be null.
     */
    public TextBasedInterface(Optional<InputStream> inputStream, Optional<OutputStream> outputStream) {
        scanner = inputStream.map(Scanner::new).orElseGet(() -> new Scanner(System.in));
        writer = outputStream.map(PrintWriter::new).orElseGet(() -> new PrintWriter(System.out));

        gameController = new GameController();
    }

    /**
     * Default constructor. Creates an interface for a CLI-Application
     */
    public TextBasedInterface() {
        scanner = new Scanner(System.in);
        writer = new PrintWriter(System.out);
        gameController = new GameController();
    }

    /**
     * Returns a maybe containing either a player action or an error. For this this method reads
     * checks if the scanner has a new line. If not an error is thrown if yes the line is converted to an player action.
     * Any error while converting leads to this method returning an error.
     *
     * @return Maybe containing either a player action or on error
     */
    public Maybe<Action, Error> getInput() {
        var lol = scanner.next(ACTION_TEMPLATE);
        Action action = convertStringToAction(lol);
        return new Maybe<>(action, null);
    }

    private Action convertStringToAction(String input) {
        var cardAndCardStack = input.split("-");
        if (cardAndCardStack.length == 1)
            return new StopAction(gameController);
        else if (cardAndCardStack.length == 2)
            return new PlayAction(cardAndCardStack);
        else
            return action -> {
                throw new MalformedActionException("Action was somehow malformed");
            };
    }

    public void startGame() {
        while (!gameController.gameIsLost() || !gameController.gameIsWon()) {
            printGameState();
            var maybe = getInput();
            if (maybe.isErroneous()) {
                writer.println("Could not get input please try again");
            } else if (maybe.isPresent()) {
                var action = maybe.getValue();
                String message = action.execute(gameController);
                gameController.addActionToHistory(action);
                writer.print(message);
            }
        }

        printGameEndMessage();
    }

    public static void main(String[] args) {
        TextBasedInterface main = new TextBasedInterface();
        main.startGame();
    }


    private void printGameState() {
        var stateMessage = new StringBuilder();
        var state = gameController.getGameState();
        appendDownStacks(stateMessage, state.getDownStacks());
        appendUpStacks(stateMessage, state.getUpStacks());
        appendRemainingCars(stateMessage, state.remainingCards());
        appendHandCards(stateMessage, state.getHandCards());

        writer.print(stateMessage.toString());
        writer.flush();
    }


    private void appendDownStacks(StringBuilder message, List<CardStack> downStacks) {
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

    private void appendHandCards(StringBuilder message, Hand handCards) {
        message.append("Your hand contains the following cards: ");
        handCards.getHandCards()
                .forEach(card -> message.append(card.getValue())
                        .append(" "));
        message.append("\n");
    }

    private void printGameEndMessage() {
        if (gameController.gameIsWon())
            writer.write(StaticMessages.GAME_WON_MESSAGE);
        if (gameController.gameIsLost())
            writer.write(StaticMessages.GAME_LOST_MESSAGE);

        writer.flush();
    }
}
