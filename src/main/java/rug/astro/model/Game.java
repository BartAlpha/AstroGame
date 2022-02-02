package rug.astro.model;

import javafx.geometry.Point3D;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rug.astro.control.GameUpdater;
import rug.astro.game_observer.ObservableGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends ObservableGame {

    /**
     * The spaceship object that the player is in control of.
     */
    private Spaceship ship;

    private Planet currentPlanet;

    /**
     * Indicates whether or not the game is running. Setting this to false causes the game to exit its loop and quit.
     */
    protected volatile boolean running = false;

    /**
     * The game updater thread, which is responsible for updating the game's state as time goes on.
     */
    protected transient Thread gameUpdaterThread;

    public static final int SPACESIZE = 2000;

    private Collection<Point3D> stars;

    private Collection<Planet> planets;

    private int discovered;

    /**
     * Constructs a new game, with a new spaceship and all other model data in its default starting state.
     */
    public Game() {
        this.ship = new Spaceship();
        this.initializeGameData();
    }

    public int getDiscovered() {
        return discovered;
    }

    public void setDiscovered(int discovered) {
        this.discovered = discovered;
    }

    /**
     * Sets running
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    public Collection<Planet> getPlanets() {
        return planets;
    }

    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public void setCurrentPlanet(Planet currentPlanet) {
        this.currentPlanet = currentPlanet;
    }

    /**
     * Sets the main ship to an other ship
     * @param ship
     */
    public void setShip(Spaceship ship) {
        this.ship = ship;
    }

    /**
     * Initializes all of the model objects used by the game. Can also be used to reset the game's state back to a
     * default starting state before beginning a new game.
     */
    public void initializeGameData() {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        this.stars = new ArrayList<>();
        for (int i = 0; i < SPACESIZE/50; i++) {
            int x = rng.nextInt(20, Game.SPACESIZE - 20);
            int y = rng.nextInt(20, Game.SPACESIZE - 20);
            int z = rng.nextInt(0, 90);
            stars.add(new Point3D(x, y, z));
        }
        this.currentPlanet = null;
        this.generatePlanets();
        this.ship.reset();
        this.running = false;
        this.discovered = 0;
    }

    private void generatePlanets() {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        this.planets = new ArrayList<>();
        Image im = null;
        JSONParser parser = new JSONParser();
        JSONArray ja = null;
        try {
            ja = (JSONArray) parser.parse(new FileReader("generated.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = 1;
        Point2D.Double l = null;
        for (Object o : ja) {
            JSONObject info = (JSONObject) o;
            boolean stacked = true;
            while (stacked) {
                l = new Point2D.Double(rng.nextDouble(70.0, SPACESIZE - 80), rng.nextDouble(100.0, SPACESIZE - 80));
                stacked = false;
                for (Planet planet : planets) {
                    if (stackedPlanets(l, planet.getLocation())) {
                        stacked = true;
                        System.out.println("STACKED: "+ l.getX() + " " + l.getY() + " AND " + planet.getLocation().getX() + " " + planet.getLocation().getY());
                        break;
                    }
                }
            }
            try {
                im = ImageIO.read(getClass().getResource("/planets/planet" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Planet p = (new Planet(l, new Point2D.Double(0.0,0.0), 60, (String) info.get("name"), im, (String) info.get("description")));
            planets.add(p);
            i++;
        }
    }

    public Collection<Point3D> getStars() {
        return stars;
    }

    /**
     * @return The game's spaceship.
     */
    public Spaceship getSpaceship() {
        return this.ship;
    }

    /**
     * @return Whether or not the game is running.
     */
    public synchronized boolean isRunning() {
        return this.running;
    }

    /**
     * @return True if the player's ship has been destroyed, or false otherwise.
     */
    public boolean isGameOver() {
        if (!ship.isDestroyed()) {
            return false;
        }
        return true;
    }

    public static boolean stackedPlanets(Point2D.Double p1, Point2D.Double p2) {
        if (Math.abs(p1.getX()-p2.getX()) < 160 && Math.abs(p1.getY()-p2.getY()) < 160) {
            return true;
        }
        return false;
    }

    /**
     * Using this game's current model, spools up a new game updater thread to begin a game loop and start processing
     * user input and physics updates. Only if the game isn't currently running, that is.
     */
    public void start() {
        if (!this.running) {
            this.running = true;
            this.gameUpdaterThread = new Thread(new GameUpdater(this));
            this.gameUpdaterThread.start();
        }
    }

    /**
     * Tries to quit the game, if it is running.
     */
    public void quit() {
        if (this.running) {
            try { // Attempt to wait for the game updater to exit its game loop.
                this.gameUpdaterThread.join(100);
            } catch (InterruptedException exception) {
                System.err.println("Interrupted while waiting for the game updater thread to finish execution.");
            } finally {
                this.running = false;
                this.gameUpdaterThread = null; // Throw away the game updater thread and let the GC remove it.
            }
        }
    }
}
