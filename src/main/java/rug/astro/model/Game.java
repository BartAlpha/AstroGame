package rug.astro.model;

import javafx.geometry.Point3D;
import rug.astro.control.GameUpdater;
import rug.astro.game_observer.ObservableGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Game extends ObservableGame {

    /**
     * The spaceship object that the player is in control of.
     */
    private Spaceship ship;

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

    /**
     * Constructs a new game, with a new spaceship and all other model data in its default starting state.
     */
    public Game() {
        this.ship = new Spaceship();
        this.initializeGameData();
    }

    /**
     * Sets running
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
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
        this.stars = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int x = ThreadLocalRandom.current().nextInt(20, Game.SPACESIZE - 20);
            int y = ThreadLocalRandom.current().nextInt(20, Game.SPACESIZE - 20);
            int z = ThreadLocalRandom.current().nextInt(0, 90);
            stars.add(new Point3D(x, y, z));
        }
        this.ship.reset();
        this.running = false;
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
