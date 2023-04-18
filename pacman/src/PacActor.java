// PacActor.java
// Used for PacMan
package src;

import ch.aplu.jgamegrid.GGKeyRepeatListener;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacActor extends GameCharacter implements GGKeyRepeatListener {
    private static final int nbSprites = 4;
    private final GameState gameState;
    private int idSprite = 0;
    private int nbPills = 0;
    private int score = 0;
    private GameVisuals gameVisuals;
    private List<String> propertyMoves = new ArrayList<>();
    private int propertyMoveIndex = 0;
    private boolean isAuto = false;
    protected final ArrayList<Location> eatenGold = new ArrayList<>();

    public PacActor(GameState gameState) {
        super(true, "sprites/pacpix.gif", nbSprites, gameState);  // Rotatable
        this.gameState = gameState;
    }

    public void setGameVisuals(GameVisuals gameVisuals) {
        this.gameVisuals = gameVisuals;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public void setPropertyMoves(String propertyMoveString) {
        if (propertyMoveString != null) {
            this.propertyMoves = Arrays.asList(propertyMoveString.split(","));
        }
    }

    public void keyRepeated(int keyCode) {
        if (isAuto) {
            return;
        }
        if (isRemoved())  // Already removed
            return;
        Location next = null;
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                next = getLocation().getNeighbourLocation(Location.WEST);
                setDirection(Location.WEST);
                break;
            case KeyEvent.VK_UP:
                next = getLocation().getNeighbourLocation(Location.NORTH);
                setDirection(Location.NORTH);
                break;
            case KeyEvent.VK_RIGHT:
                next = getLocation().getNeighbourLocation(Location.EAST);
                setDirection(Location.EAST);
                break;
            case KeyEvent.VK_DOWN:
                next = getLocation().getNeighbourLocation(Location.SOUTH);
                setDirection(Location.SOUTH);
                break;
        }
        if (next != null && canMove(next)) {
            setLocation(next);
            eatPill(next);
        }
    }

    public void act() {
        show(idSprite);
        idSprite++;
        if (idSprite == nbSprites)
            idSprite = 0;

        if (isAuto) {
            moveInAutoMode();
        }
        this.gameState.getGameCallback().pacManLocationChanged(getLocation(), score, nbPills);
    }

    private Location closestPillLocation() {
        int currentDistance = 1000;
        Location currentLocation = null;
        List<Location> pillAndItemLocations = gameState.getPillAndItemLocations();
        for (Location location : pillAndItemLocations) {
            int distanceToPill = location.getDistanceTo(getLocation());
            if (distanceToPill < currentDistance) {
                currentLocation = location;
                currentDistance = distanceToPill;
            }
        }

        return currentLocation;
    }

    private void followPropertyMoves() {
        String currentMove = propertyMoves.get(propertyMoveIndex);
        switch (currentMove) {
            case "R":
                turn(90);
                break;
            case "L":
                turn(-90);
                break;
            case "M":
                Location next = getNextMoveLocation();
                if (canMove(next)) {
                    setLocation(next);
                    eatPill(next);
                }
                break;
        }
        propertyMoveIndex++;
    }

    private void moveInAutoMode() {
        if (propertyMoves.size() > propertyMoveIndex) {
            followPropertyMoves();
            return;
        }
        Location closestPill = closestPillLocation();
        double oldDirection = getDirection();

        Location.CompassDirection compassDir = getLocation().get4CompassDirectionTo(closestPill);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);
        if (!isVisited(next) && canMove(next)) {
            setLocation(next);
        } else {
            // normal movement
            int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
            setDirection(oldDirection);
            turn(sign * 90);  // Try to turn left/right
            next = getNextMoveLocation();
            if (canMove(next)) {
                setLocation(next);
            } else {
                setDirection(oldDirection);
                next = getNextMoveLocation();
                if (canMove(next)) // Try to move forward
                {
                    setLocation(next);
                } else {
                    setDirection(oldDirection);
                    turn(-sign * 90);  // Try to turn right/left
                    next = getNextMoveLocation();
                    if (canMove(next)) {
                        setLocation(next);
                    } else {
                        setDirection(oldDirection);
                        turn(180);  // Turn backward
                        next = getNextMoveLocation();
                        setLocation(next);
                    }
                }
            }
        }
        eatPill(next);
        addVisitedList(next);
    }

    public int getNbPills() {
        return nbPills;
    }

    private void eatPill(Location location) {
        Color c = getBackground().getColor(location);
        if (c.equals(Color.white)) {
            nbPills++;
            score++;
            getBackground().fillCell(location, Color.lightGray);
            gameState.getGameCallback().pacManEatPillsAndItems(location, "pills");
        } else if (c.equals(Color.yellow)) {
            nbPills++;
            score += 5;
            getBackground().fillCell(location, Color.lightGray);

            // TODO: Update GameState after eating gold
            // If we have not frozen monster, we eat gold informing them
            // If we did eat ice and are frozen, we dont tell them we ate gold
            if (gameState.timeSinceIceEaten() >= GameState.TIME_FROZEN) {
                gameState.setTimeGoldEaten(System.nanoTime());
            }

            gameState.getGameCallback().pacManEatPillsAndItems(location, "gold");
            gameVisuals.removeItem("gold", location);
            eatenGold.add(location);
        } else if (c.equals(Color.blue)) {
            getBackground().fillCell(location, Color.lightGray);
            // TODO: Update GameState after eating ice
            gameState.setTimeIceEaten(System.nanoTime());
            gameState.getGameCallback().pacManEatPillsAndItems(location, "ice");
            gameVisuals.removeItem("ice", location);
        }
        String title = "[PacMan in the Multiverse] Current score: " + score;
        gameGrid.setTitle(title);
    }

}
