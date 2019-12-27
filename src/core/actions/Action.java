package core.actions;

import main.GameController;

/**
 * This class represents a players action
 */
public interface Action {
    String execute(GameController gameController);
}
