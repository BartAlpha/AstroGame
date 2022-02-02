package rug.astro.view;

import rug.astro.control.MainMenuSelector;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {

    /**
     * The title which appears in the upper border of the window.
     */
    private static final String WINDOW_TITLE = "Main menu";

    /**
     * The size that the window should be.
     */
    public static final Dimension WINDOW_SIZE = new Dimension(416, 200);

    /**
     * Constructs the game's main menu.
     */
    public MainMenuFrame () {
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
        setBackground(Color.BLACK);
        // Add the custom panel that the game will be drawn to.
        setLocationRelativeTo(null);
        MainMenuPanel mp = new MainMenuPanel();
        MainMenuSelector ms = new MainMenuSelector(mp, this);
        this.add(mp);
        this.setVisible(true);
    }
}
