// Monster.java
// Used for PacMan
package src;

import ch.aplu.jgamegrid.Location;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Monster extends GameCharacter {
    protected GameState gameState;
    protected MonsterType type;
    private boolean stopMoving = false;
    protected boolean isFurious = false;
    protected boolean isFrozen = false;

    public Monster(GameState gameState, MonsterType type) {
        super("sprites/" + type.getImageName(), gameState);
        this.gameState = gameState;
        this.type = type;
    }

    public void stopMoving(int seconds) {
        this.stopMoving = true;
        Timer timer = new Timer(); // Instantiate Timer Object
        int SECOND_TO_MILLISECONDS = 1000;
        final Monster monster = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                monster.stopMoving = false;
            }
        }, seconds * SECOND_TO_MILLISECONDS);
    }

    // make monster be furious state and end furious in seconds
    public void Furious(int seconds) {
        this.isFurious = true;
        Timer timer = new Timer(); // Instantiate Timer Object
        int SECOND_TO_MILLISECONDS = 1000;
        final Monster monster = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                monster.isFurious = false;
            }
        }, seconds * SECOND_TO_MILLISECONDS);
    }

    // make monster frozen for seconds
    public void Frozen(int seconds) {
        this.isFrozen = true;
        Timer timer = new Timer(); // Instantiate Timer Object
        int SECOND_TO_MILLISECONDS = 1000;
        final Monster monster = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                monster.isFrozen = false;
            }
        }, seconds * SECOND_TO_MILLISECONDS);
        this.stopMoving(seconds);
    }

    // use for wizard to skip the wall or move faster when monster is furious
    protected Location findNext(Location cell, Location loc){
        Location next = new Location();
        if (cell.getX() - loc.getX() == 1){// move right
            next.x = cell.getX() + 1;
        }else if (loc.getX() - cell.getX() == 1){// move left
            next.x = cell.getX() - 1;
        }else{
            next.x = cell.getX();// stay same x axis
        }
        if (cell.getY() - loc.getY() == 1){// move down
            next.y = cell.getY() + 1;
        }else if (loc.getY() - cell.getY() == 1){// move up
            next.y = cell.getY() - 1;
        }else{
            next.y = cell.getY();// stay same y axis
        }
        return next;
    }

    public void setStopMoving(boolean stopMoving) {
        this.stopMoving = stopMoving;
    }

    public void act() {
        if (stopMoving) {
            return;
        }
        // TODO: Check that this works to update them each frame
        //updateFurious();
        //updateFrozen();
        walkApproach();
        setHorzMirror(!(getDirection() > 150) || !(getDirection() < 210));
    }

    public abstract void walkApproach();

    public MonsterType getType() {
        return type;
    }

    public void updateFurious() {
        if (gameState.timeSinceGoldEaten() < GameState.TIME_FURIOUS && !isFrozen && !isFurious) {
            this.Furious(3);
        }
        if (gameState.timeSinceGoldEaten() >= GameState.TIME_FURIOUS && isFurious) {
            isFurious = false;
        }
    }

    public void updateFrozen() {
        if (gameState.timeSinceGoldEaten() < GameState.TIME_FURIOUS && !isFrozen && !isFurious) {
            isFurious = true;
        }
        if (gameState.timeSinceGoldEaten() >= GameState.TIME_FURIOUS && isFurious) {
            isFurious = false;
        }
    }
}
