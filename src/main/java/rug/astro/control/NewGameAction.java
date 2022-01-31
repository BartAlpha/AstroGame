package rug.astro.control;

import rug.astro.model.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewGameAction extends AbstractAction {
    /**
     * A reference to the game that should be reset/initialized when the user does this action.
     */
    private Game game;

    /**
     * Constructs the action. Calls the parent constructor to set the name of this action.
     * @param game The game model that will be used.
     */
    public NewGameAction(Game game) {
        super("New Game");
        this.game = game;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param event The event to be processed. In this case, no information from the actual event is needed. Simply the
     *              knowledge that it occurred is enough.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        this.game.quit(); // Try to stop the game if it's currently running.
        this.game.initializeGameData(); // Resets the game's objects to their default state.
        this.game.setRunning(false);
        this.game.notifyListeners(0L);
        this.game.start(); // Spools up the game's engine and starts the main game loop.
    }
}
