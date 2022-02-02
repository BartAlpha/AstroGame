package rug.astro.model;

import rug.astro.view.AstroFrame;

import java.awt.*;

public class Spaceship extends GameObject {
    /**
     * The maximum speed that the spaceship is allowed to reach before extra acceleration will not do anything.
     */
    public static final double MAXIMUM_SPEED = 10.0;

    /**
     * The coefficient to multiply the ship's velocity by every tick, so that it slows down.
     */
    public static final double VELOCITY_DAMPENING_COEFFICIENT = 0.99;

    /**
     * The rate at which the spaceship will speed up, per axis, per tick.
     */
    public static final double ACCELERATION_PER_TICK = 0.4;

    /**
     * The amount in radians that the spaceship rotates per tick, if the player is rotating it.
     */
    public static final double ROTATION_PER_TICK = 0.04 * Math.PI;

    /**
     * Default cor for the ship
     */
    public static final Color DEFAULT_COLOR = Color.BLACK;

    /** Direction the spaceship is pointed in. */
    private double direction;

    /** Indicates whether the accelerate button is pressed. */
    private boolean accelerateKeyPressed;

    /** Indicates whether the turn right button is pressed. */
    private boolean turnRightKeyPressed;

    /** Indicates whether the turn left button is pressed. */
    private boolean turnLeftKeyPressed;

    private boolean departureKeyPressed;

    /**
     * Color of the ship.
     */
    private Color color;

    /**
     * Constructs a new spaceship with default values. It starts in the middle of the window, facing directly upwards,
     * with no velocity.
     */
    Spaceship() {
        super(Game.SPACESIZE / 2.0, Game.SPACESIZE / 2.0, 0, 0, 15);
        color = DEFAULT_COLOR;
        this.reset();
    }

    /**
     * @return returns the color of the ship
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter for color
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Resets all parameters to default values, so a new game can be started.
     */
    public void reset() {
        this.getLocation().x = AstroFrame.WINDOW_SIZE.width / 2;
        this.getLocation().y = AstroFrame.WINDOW_SIZE.height / 2;
        this.getVelocity().x = 0;
        this.getVelocity().y = 0;
        this.direction = 0;
        this.accelerateKeyPressed = false;
        this.turnLeftKeyPressed = false;
        this.turnRightKeyPressed = false;
        this.departureKeyPressed = false;
        this.destroyed = false;
    }

    public boolean isDepartureKeyPressed() {
        return departureKeyPressed;
    }

    public void setDepartureKeyPressed(boolean b) {
        this.departureKeyPressed = b;
    }

    /**
     *	Sets the left field to the specified value.
     *
     *	@param b new value of the field.
     */
    public void setTurnLeftKeyPressed(boolean b) {
        this.turnLeftKeyPressed = b;
    }

    /**
     *	Sets the right field to the specified value.
     *
     *	@param b new value of the field.
     */
    public void setTurnRightKeyPressed(boolean b) {
        this.turnRightKeyPressed = b;
    }

    /**
     *	Sets the up field to the specified value.
     *
     *	@param b new value of the field.
     */
    public void setAccelerateKeyPressed(boolean b) {
        this.accelerateKeyPressed = b;
    }

    /**
     * Defines how the spaceship moves. This includes rotating the ship if the user is pressing the key to turn the
     * ship, or accelerating the ship, or firing the weapon.
     */
    @Override
    public void nextStep() {
        super.nextStep();
        this.attemptToTurn();
        this.attemptToAccelerate();
        this.dampenVelocity();
    }

    /**
     * Dampens the ship's velocity, i.e. slows it down slightly, so that you don't drift endlessly across the screen.
     */
    private void dampenVelocity() {
        this.getVelocity().x *= VELOCITY_DAMPENING_COEFFICIENT;
        this.getVelocity().y *= VELOCITY_DAMPENING_COEFFICIENT;
    }

    /**
     * Attempts to accelerate the spaceship. If all of the criteria for accelerating the ship are met, then it will
     * accelerate. For a ship to be able to accelerate, the user must be pressing the key to do so, and the ship must
     * have enough energy, and finally, the ship must not exceed its maximum set speed.
     */
    private void attemptToAccelerate() {
        if (this.accelerateKeyPressed  && this.getSpeed() < MAXIMUM_SPEED) {
            this.getVelocity().x += Math.sin(direction) * ACCELERATION_PER_TICK;
            this.getVelocity().y -= Math.cos(direction) * ACCELERATION_PER_TICK; // Note that we subtract here, because the y-axis on the screen is flipped, compared to normal math.
        }
    }

    /**
     * Attempts to turn the spaceship. If all of the criteria for turning the ship are met, then it will rotate.
     * For a ship to be able to rotate, the user must be pressing the key to turn it either left or right, and the ship
     * must have enough energy to rotate.
     */
    private void attemptToTurn() {
        if (this.turnLeftKeyPressed) {
            this.direction -= ROTATION_PER_TICK;
        }
        if (this.turnRightKeyPressed) {
            this.direction += ROTATION_PER_TICK;
        }
    }

    /**
     * @return The number of steps, or game ticks, for which this object is immune from collisions.
     */
    @Override
    protected int getDefaultStepsUntilCollisionPossible() {
        return 10;
    }

    /**
     * @return the direction.
     */
    public double getDirection() {
        return this.direction;
    }

    /**
     * @return true if acceleration button is pressed, false otherwise.
     */
    public boolean isAccelerating()	{
        return this.accelerateKeyPressed;
    }
}
