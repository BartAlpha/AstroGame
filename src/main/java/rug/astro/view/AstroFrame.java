package rug.astro.view;

import rug.astro.control.NewGameAction;
import rug.astro.control.PlayerKeyListener;
import rug.astro.control.QuitAction;
import rug.astro.model.Game;

import javax.swing.*;
import java.awt.*;

public class AstroFrame extends JFrame {
    /**
     * The title which appears in the upper border of the window.
     */
    private static final String WINDOW_TITLE = "Astro";

    /**
     * The size that the window should be.
     */
    public static final Dimension WINDOW_SIZE = new Dimension(800, 800);

    /**
     * The game model.
     */
    private Game game;

    /**
     * Constructs the game's main window.
     *
     * @param game The game model that this window will show.
     */
    public AstroFrame (Game game) {
        this.game = game;
        this.initSwingUI();
    }

    /**
     * A helper method to do the tedious task of initializing the Swing UI components.
     */
    private void initSwingUI() {
        // Basic frame properties.
        this.setTitle(WINDOW_TITLE);
        this.setSize(WINDOW_SIZE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add a key listener that can control the game's spaceship.
        this.addKeyListener(new PlayerKeyListener(this.game.getSpaceship()));

        // Add a menu bar with some simple actions.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menuBar.add(menu);
        menu.add(new QuitAction(this.game));
        menu.add(new NewGameAction(this.game));
        this.setJMenuBar(menuBar);

        // Add the custom panel that the game will be drawn to.
        this.add(new AstroPanel(this.game));
        this.setVisible(true);
    }
}
