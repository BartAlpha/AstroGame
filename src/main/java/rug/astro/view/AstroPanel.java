package rug.astro.view;

import javafx.geometry.Point3D;
import rug.astro.game_observer.GameUpdateListener;
import rug.astro.model.Game;
import rug.astro.model.Planet;
import rug.astro.view.view_models.SpaceshipViewModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class AstroPanel extends JPanel implements GameUpdateListener {

    private Image paper;
    /**
     * The game model that this panel will draw to the screen.
     */
    private final Game game;

    /**
     * Number of milliseconds since the last time the game's physics were updated. This is used to continue drawing all
     * game objects as if they have kept moving, even in between game ticks.
     */
    private long timeSinceLastTick = 0L;

    /**
     * Constructs a new game panel, based on the given model. Also starts listening to the game to check for updates, so
     * that it can repaint itself if necessary.
     *
     * @param game The model which will be drawn in this panel.
     */
    AstroPanel(Game game) {
        this.game = game;
        this.game.addListener(this);
        try {
            this.paper = ImageIO.read(getClass().getResource("/paper.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method provided by JPanel for 'painting' this component. It is overridden here so that this panel can define
     * some custom drawing. By default, a JPanel is just an empty rectangle.
     *
     * @param graphics The graphics object that exposes various drawing methods to use.
     */
    @Override
    public void paintComponent(Graphics graphics) {
		/* The parent method is first called. Here's an excerpt from the documentation stating why we do this:
		"...if you do not invoke super's implementation you must honor the opaque property, that is if this component is
		opaque, you must completely fill in the background in an opaque color. If you do not honor the opaque property
		you will likely see visual artifacts." Just a little FYI.
		 */
        super.paintComponent(graphics);

        // The Graphics2D class offers some more advanced options when drawing, so before doing any drawing, this is obtained simply by casting.
        Graphics2D graphics2D = (Graphics2D) graphics;
        // Set some key-value options for the graphics object. In this case, this just sets antialiasing to true.
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Since the game takes place in space, it is efficient to just lazily make the background black.
        this.setBackground(Color.BLACK);
        Font f = new Font("big", Font.CENTER_BASELINE, 15);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(f);
        this.drawStars(graphics2D);
        this.drawGameObjects(graphics2D);
        this.drawDead(graphics2D);
        double x = game.getSpaceship().getLocation().x;
        double y = game.getSpaceship().getLocation().y;
        this.drawBorderX(graphics2D);
        this.drawBorderY(graphics2D);
        this.drawPlanets(graphics2D);
        this.drawMinimap(graphics2D);
        this.drawInfo(graphics2D);
    }

    public void drawPlanets(Graphics2D g) {
        g.setColor(Color.WHITE);
        double x = game.getSpaceship().getLocation().x;
        double y = game.getSpaceship().getLocation().y;
        if (game.isRunning()) {
            for (Planet planet : game.getPlanets()) {
                Point2D.Double p = planet.getLocation();
                if (p.getX() >= x-460 && p.getX() <= x+460 && p.getY() >= y-460 && p.getY() <= y+460) {
                    g.drawImage(planet.getImage(), (int) ((int)p.getX() - x + 375 - planet.getRadius()), (int) ((int) p.getY() - y + 375 - planet.getRadius()), (int) ((int) planet.getRadius()*2.9), (int) ((int) planet.getRadius()*2.9), null);
                }
            }
        }
    }

    public void drawBorderX(Graphics2D g) {
        g.setColor(Color.WHITE);
        double x = game.getSpaceship().getLocation().x;
        double y = game.getSpaceship().getLocation().y;
        int x1 = 0;
        int x2 = 800;
        int y1 = 0;
        int y2 = 800;
        boolean draw = false;
        if (x-400 < 0) {
            x1 = (int) (400-x);
            x2 = (int) (400-x);
            draw = true;
        } else if (x+400 > Game.SPACESIZE) {
            x1 = (int) (Game.SPACESIZE-x+400);
            x2 = (int) (Game.SPACESIZE-x+400);
            draw = true;
        }
        if (y-400 < 0) {
            y1 = (int) (400-y);
            y2 = game.SPACESIZE;
        } else if (y+400 > game.SPACESIZE) {
            y1 = 0;
            y2 = (int) (Game.SPACESIZE-y+400);
        }

        if (draw) {
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public void drawBorderY(Graphics2D g) {
        g.setColor(Color.WHITE);
        double x = game.getSpaceship().getLocation().x;
        double y = game.getSpaceship().getLocation().y;
        int x1 = 0;
        int x2 = 800;
        int y1 = 0;
        int y2 = 800;
        boolean draw = false;
        if (x-400 < 0) {
            x1 = (int) (400-x);
            x2 = 800;
        } else if (x+400 > Game.SPACESIZE) {
            x1 = 0;
            x2 = (int) (Game.SPACESIZE-x+400);
        }
        if (y-400 < 0) {
            y1 = (int) (400-y);
            y2 = (int) (400-y);
            draw = true;
        } else if (y+400 > Game.SPACESIZE) {
            y1 = (int) (Game.SPACESIZE-y+400);
            y2 = (int) (Game.SPACESIZE-y+400);
            draw = true;
        }

        if (draw) {
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public void drawMinimap(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(650, 50, 101, 101);
        g.setColor(Color.WHITE);
        g.drawRect(650, 50, 101, 101);
        double x = game.getSpaceship().getLocation().x;
        double y = game.getSpaceship().getLocation().y;
        double xmini = x/Game.SPACESIZE*100;
        double ymini = y/Game.SPACESIZE*100;
        g.setColor(Color.CYAN);
        g.fillRect((int)xmini+650,(int)ymini+50,2,2);
        for (Planet p : game.getPlanets()) {
            if (!p.isVisited()) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }
            xmini = p.getLocation().x/Game.SPACESIZE*100;
            ymini = p.getLocation().y/Game.SPACESIZE*100;
            g.fillRect((int)xmini+650,(int)ymini+50,2,2);
        }
        Font f = new Font("big", Font.CENTER_BASELINE, 10);
        g.setColor(Color.WHITE);
        g.setFont(f);
        g.drawString("Visited planets: " + this.game.getDiscovered() + "/" + this.game.getPlanets().size(), 650, 30);
    }

    public void drawInfo(Graphics2D g) {
        if (game.getCurrentPlanet() != null) {
            g.drawImage(paper, 100, 100, 600, 550, null);
            String info = "Press x to Leave the planet";
            String end = "You have discovered all planets";
            Font f = new Font("big", Font.CENTER_BASELINE, 25);
            g.setColor(Color.WHITE);
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            if (this.game.getDiscovered() == this.game.getPlanets().size()) {
                g.drawString(end , AstroFrame.WINDOW_SIZE.width/2 - fm.stringWidth(end)/2, 80);
            }

            g.drawString(info, AstroFrame.WINDOW_SIZE.width/2 - fm.stringWidth(info)/2, 50);
            g.setColor(Color.BLACK);
            g.drawString(game.getCurrentPlanet().getName(), AstroFrame.WINDOW_SIZE.width/2 - fm.stringWidth(game.getCurrentPlanet().getName())/2, 150);
            drawStringMultiLine(g, game.getCurrentPlanet().getDescription(), 450, 160, 190);
        }
    }


    public void drawStars(Graphics2D g) {
        g.setColor(Color.YELLOW);
        double x = game.getSpaceship().getLocation().x;
        double y = game.getSpaceship().getLocation().y;
        if (game.isRunning()) {
            for (Point3D p : game.getStars()) {
                if (p.getX() >= x-400 && p.getX() <= x+400 && p.getY() >= y-400 && p.getY() <= y+400) {
                    Path2D star = createStar(p.getX() - x + 400, p.getY() - y + 400, 2, 5, 5, Math.toRadians(p.getZ()));
                    g.fill(star);
                }
            }
        }
    }

    private static Path2D createStar(double centerX, double centerY,
                                    double innerRadius, double outerRadius, int numRays,
                                    double startAngleRad)
    {
        Path2D path = new Path2D.Double();
        double deltaAngleRad = Math.PI / numRays;
        for (int i = 0; i < numRays * 2; i++)
        {
            double angleRad = startAngleRad + i * deltaAngleRad;
            double ca = Math.cos(angleRad);
            double sa = Math.sin(angleRad);
            double relX = ca;
            double relY = sa;
            if ((i & 1) == 0)
            {
                relX *= outerRadius;
                relY *= outerRadius;
            }
            else
            {
                relX *= innerRadius;
                relY *= innerRadius;
            }
            if (i == 0)
            {
                path.moveTo(centerX + relX, centerY + relY);
            }
            else
            {
                path.lineTo(centerX + relX, centerY + relY);
            }
        }
        path.closePath();
        return path;
    }

    /**
     * Draws a string that will say that it is game over for the player
     *
     * @param g
     */
    public void drawDead(Graphics2D g) {
        if (game.isRunning() && game.getSpaceship().isDestroyed()) {
            g.drawString("Your spaceship is destroyed", 300, 30);
        }
    }

    public static void drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
        FontMetrics m = g.getFontMetrics();
        if(m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y);
        } else {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for(int i = 1; i < words.length; i++) {
                if(m.stringWidth(currentLine+words[i]) < lineWidth) {
                    currentLine += " "+words[i];
                } else {
                    g.drawString(currentLine, x, y);
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            if(currentLine.trim().length() > 0) {
                g.drawString(currentLine, x, y);
            }
        }
    }


    /**
     * Draws all of the game's objects. Wraps each object in a view model, then uses that to draw the object.
     *
     * @param graphics2D The graphics object that provides the drawing methods.
     */
    private void drawGameObjects(Graphics2D graphics2D) {
        /*
         * Because the game engine is running concurrently in its own thread, we must obtain a lock for the game model
         * while drawing to ensure that we don't encounter a concurrentModificationException, which would happen if we
         * were in the middle of drawing while the game engine starts a new physics update.
         */
        synchronized (this.game) {
            if (this.game.getSpaceship() != null && !this.game.getSpaceship().isDestroyed()) {
                new SpaceshipViewModel(this.game.getSpaceship()).drawObject(graphics2D, this.timeSinceLastTick);
            }
        }
    }

    /**
     * Do something when the game has indicated that it is updated. For this panel, that means redrawing.
     *
     * @param timeSinceLastTick The number of milliseconds since the game's physics were updated. This is used to allow
     *                          objects to continue to appear animated between each game tick.
     *
     * Note for your information: when repaint() is called, Swing does some internal stuff, and then paintComponent()
     * is called.
     */
    @Override
    public void onGameUpdated(long timeSinceLastTick) {
        this.timeSinceLastTick = timeSinceLastTick;
        this.repaint();
    }
}
