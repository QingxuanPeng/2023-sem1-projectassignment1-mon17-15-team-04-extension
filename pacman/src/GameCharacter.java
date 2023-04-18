package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public abstract class GameCharacter extends Actor {
    protected final Random randomiser = new Random();
    protected final ArrayList<Location> visitedList = new ArrayList<Location>();
    private final int listLength = 10;
    protected GameState gameState;
    protected int seed = 0;

    public GameCharacter(boolean isRotatable, String filename, int nbSprites, GameState gameState) {
        super(isRotatable, filename, nbSprites);
        this.gameState = gameState;
    }

    public GameCharacter(String filename, GameState gameState) {
        super(filename);
        this.gameState = gameState;
    }

    public void setSeed(int seed) {
        this.seed = seed;
        randomiser.setSeed(seed);
    }

    public abstract void act();

    protected void addVisitedList(Location location) {
        visitedList.add(location);
        if (visitedList.size() == listLength)
            visitedList.remove(0);
    }

    protected boolean isVisited(Location location) {
        for (Location loc : visitedList)
            if (loc.equals(location))
                return true;
        return false;
    }

    protected boolean canMove(Location location) {
        Color c = getBackground().getColor(location);
        return !c.equals(Color.gray) && location.getX() < gameState.getNumHorzCells() && location.getX() >= 0 && location.getY() < gameState.getNumVertCells() && location.getY() >= 0;
    }
}
