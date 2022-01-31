package rug.astro.control;

import rug.astro.model.Game;
import rug.astro.view.AstroFrame;
import rug.astro.view.MainMenuFrame;
import rug.astro.view.MainMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenuSelector extends MouseAdapter {
    /**
     * frame of the mainmenu
     */
    private MainMenuFrame mf;

    /**
     * Constructor
     * @param mp mp that listens to this class
     * @param mf mf so we can dispose the mainframe on click
     */
    public MainMenuSelector(MainMenuPanel mp, MainMenuFrame mf) {
        this.mf = mf;
        mp.addMouseListener(this);
        mp.addMouseMotionListener(this);
    }

    /**
     * Selects a button on the panel when to mouse is pressed on it and performs the action that belongs to the button.
     * @param event
     */
    @Override
    public void mousePressed(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();
        if (x >= 100 && x <= 300) {
            if (y >= 50 && y <= 125) { // button for singleplayer
                handleSinglePlayer();
            }
        }
    }

    /**
     * Starts a single player game.
     */
    private void handleSinglePlayer() {
        mf.dispose();
        // Create the game model and display frame.
        Game game = new Game();
        game.getSpaceship().setColor(JColorChooser.showDialog(new JFrame(),"Select a color", Color.BLACK));
        AstroFrame frame = new AstroFrame(game);
        game.start();
    }
}
