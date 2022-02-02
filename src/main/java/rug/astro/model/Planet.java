package rug.astro.model;

import java.awt.*;
import java.awt.geom.Point2D;

public class Planet extends GameObject {

    private String name;

    private Image image;

    private String description;

    private boolean visited;

    public Planet(Point.Double location, Point.Double velocity, double radius, String name, Image image, String description) {
        super(location, velocity, radius);
        this.name = name;
        this.image = image;
        this.description = description;
        this.destroyed = false;
        this.visited = false;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected int getDefaultStepsUntilCollisionPossible() {
        return 30;
    }
}
