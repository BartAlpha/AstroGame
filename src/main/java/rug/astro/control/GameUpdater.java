package rug.astro.control;

import rug.astro.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GameUpdater implements Runnable {
    /**
     * The refresh rate of the display, in frames per second. Increasing this number makes the game look smoother, up to
     * a certain point where it's no longer noticeable.
     */
    private static final int DISPLAY_FPS = 144;

    /**
     * The rate at which the game ticks (how often physics updates are applied), in frames per second. Increasing this
     * number speeds up everything in the game. Ships react faster to input, bullets fly faster, etc.
     */
    private static final int PHYSICS_FPS = 30;

    /**
     * The number of milliseconds in a game tick.
     */
    public static final double MILLISECONDS_PER_TICK = 1000.0 / PHYSICS_FPS;


    /**
     * Set this to true to allow asteroids to collide with each other, potentially causing chain reactions of asteroid
     * collisions.
     */
    private static final boolean KESSLER_SYNDROME = false;

    /**
     * The game that this updater works for.
     */
    private Game game;

    /**
     * Counts the number of times the game has updated.
     */
    private int updateCounter;

    /**
     * The limit to the number of asteroids that may be present. If the current number of asteroids exceeds this amount,
     * no new asteroids will spawn.
     */
    private int asteroidsLimit;

    /**
     * Constructs a new game updater with the given game.
     *
     * @param game The game that this updater will update when it's running.
     */
    public GameUpdater(Game game) {
        this.game = game;
        this.updateCounter = 0;
    }

    /**
     * The main game loop.
     *
     * Starts the game updater thread. This will run until the quit() method is called on this updater's game object.
     * This will also send the game to all clients if the game runs on a server.
     */
    @Override
    public void run() {
        long previousTime = System.currentTimeMillis();
        long timeSinceLastTick = 0L;
        long timeSinceLastDisplayFrame = 0L;

        final double millisecondsPerDisplayFrame = 1000.0 / DISPLAY_FPS;

        while (this.game.isRunning() && !this.game.isGameOver()) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - previousTime;
            timeSinceLastTick += elapsedTime;
            timeSinceLastDisplayFrame += elapsedTime;

            if (timeSinceLastTick >= MILLISECONDS_PER_TICK) { // Check if enough time has passed to update the physics.
                this.updatePhysics(); // Perform one 'step' in the game.
                timeSinceLastTick = 0L;
            }
            if (timeSinceLastDisplayFrame >= millisecondsPerDisplayFrame) { // Check if enough time has passed to refresh the display.
                this.game.notifyListeners(timeSinceLastTick); // Tell the asteroids panel that it should refresh.
                timeSinceLastDisplayFrame = 0L;
            }

            previousTime = currentTime;
        }
    }

    /**
     * Called every game tick, to update all of the game's model objects.
     *
     * First, each object's movement is updated by calling nextStep() on it.
     * Then, if the player is pressing the key to fire the ship's weapon, a new bullet should spawn.
     * Then, once all objects' positions are updated, we check for any collisions between them.
     * And finally, any objects which are destroyed by collisions are removed from the game.
     *
     * Also, every 200 game ticks, if possible, a new random asteroid is added to the game.
     */
    private void updatePhysics()
    {
        Spaceship ship = this.game.getSpaceship();
        ship.nextStep();
        this.checkCollisions();
        this.updateCounter++;
    }

    /**
     * Checks all objects for collisions and marks them as destroyed upon collision. All objects can collide with
     * objects of a different type, but not with objects of the same type. I.e. bullets cannot collide with bullets etc.
     */
    private void checkCollisions() {
    }


    /**
     * Removes all destroyed objects (those which have collided with another object).
     *
     * When an asteroid is destroyed, it may spawn some smaller successor asteroids, and these are added to the game's
     * list of asteroids.
     */
    private void removeDestroyedObjects () {
    }
}
