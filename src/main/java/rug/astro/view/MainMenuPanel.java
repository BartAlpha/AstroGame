package rug.astro.view;


import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/**
 * Decides what will be shown on the main menu
 */
public class MainMenuPanel extends JPanel {

    /**
     * Constructs the panel for the game's main menu
     */
    public MainMenuPanel() {
        setVisible(true);
        setOpaque(true);
        repaint();
    }

    /**
     * Makes the rectangular buttons for the different option visible
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Font f = new Font("big", Font.CENTER_BASELINE, 25);
        g.setColor(Color.WHITE);
        g.setFont(f);
        g.drawString("Astro Main Menu", 100, 30);
        drawRect(g,100, 50, 200, 75, "Single Player");
    }

    /**
     * Draws a single rectangle that should represent a button to one of the options of the menu
     * @param g
     * @param x Starting point on the frame on the x-axis
     * @param y Starting point on the frame on the y-axis
     * @param width The width of the rectangle to be drawn
     * @param height The height of the rectangle to be drawn
     * @param name The name of the option, representing what it will be
     */
    public void drawRect(Graphics g, int x, int y, int width, int height, String name) {
        int fsize = 25;
        FontRenderContext frc = new FontRenderContext(null, true, true);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
        int a = -5;
        int b = 0;
        while (x + a < x) {
            Font f = new Font("big", Font.BOLD, fsize);
            g.setFont(f);
            Rectangle2D r2d = f.getStringBounds(name, frc);
            int rWidth = (int) Math.round(r2d.getWidth());
            int rHeight = (int) Math.round(r2d.getHeight());
            int rX = (int) Math.round(r2d.getX());
            int rY = (int) Math.round(r2d.getY());
            a = (width / 2) - (rWidth / 2) - rX;
            b = (height / 2) - (rHeight / 2) - rY;
            fsize--;
        }
        g.drawString(name, x + a, y + b);
    }
}
