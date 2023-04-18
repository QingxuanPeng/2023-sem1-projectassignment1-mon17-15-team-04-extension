package src;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Orion extends Monster {
    private int begin = 1; // check if it is the beginning location
    private Location pos = new Location();
    public Orion(GameState gameState) {
        super(gameState, MonsterType.Orion);
    }

    @Override
    public void walkApproach() {
        // TODO: Add updated walk approach
        // CAN USE SAME WALK APPROACH
        // Find targeted gold piece and go towards that instead of pacman
        // Store selected gold piece as class variable and continue to walk towards it till we reach it
        // Then choose new location and keep walking towards it
        Location next = new Location();
        ArrayList<Location> gold_pos = gameState.getPropertyGoldLocations();
        ArrayList<Location> eatenGold = gameState.getPacActor().eatenGold;
        // refresh the pos everytime the orion reach the previous pos
        if (getLocation().equals(pos)){
            // follow the priority to pick one position randomly
            pos = getGold(gold_pos, eatenGold);
        }else if (begin == 1){ // special situation for first time
            // randomly pick
            Collections.shuffle(gold_pos);
            for (Location gold : gold_pos){
                pos = gold;
                break;
            }
            begin = 0;
        }
        // find direction to gold
        Location.CompassDirection dir = getLocation().get4CompassDirectionTo(pos);
        next = getLocation().getNeighbourLocation(dir);
        setDirection(dir);
        if (canMove(next) && !isVisited(next)){// can move
            if (isFurious){
                Location probcell = findNext(next, getLocation());
                if (canMove(probcell)){
                    next = probcell;
                }
            }
            setLocation(next);
        }
        else{ // cannot move, find other way, maybe not close to gold
            double oldDirection = getDirection();
            int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
            setDirection(oldDirection);
            turn(sign * 90);  // Try to turn left/right
            next = getNextMoveLocation();
            if (canMove(next) && !isVisited(next)) {
                if (isFurious){
                    Location probcell = findNext(next, getLocation());
                    if (canMove(probcell)){
                        next = probcell;
                    }
                }
                setLocation(next);
            } else {
                setDirection(oldDirection);
                next = getNextMoveLocation();
                if (canMove(next) && !isVisited(next)) // Try to move forward
                {   if (isFurious){
                    Location probcell = findNext(next, getLocation());
                    if (canMove(probcell)){
                        next = probcell;
                    }
                }
                    setLocation(next);
                } else {
                    setDirection(oldDirection);
                    turn(-sign * 90);  // Try to turn right/left
                    next = getNextMoveLocation();
                    if (canMove(next) && !isVisited(next)) {
                        if (isFurious){
                            Location probcell = findNext(next, getLocation());
                            if (canMove(probcell)){
                                next = probcell;
                            }
                        }
                        setLocation(next);
                    } else {

                        setDirection(oldDirection);
                        turn(180);  // Turn backward
                        next = getNextMoveLocation();
                        if (isFurious){
                            Location probcell = findNext(next, getLocation());
                            if (canMove(probcell)){
                                next = probcell;
                            }
                        }
                        setLocation(next);
                    }
                }
            }
        }
        gameState.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }

    // randomly get a gold position
    private Location getGold(ArrayList<Location> arr1, ArrayList<Location> arr2){
        ArrayList<Location> aliveGold = new ArrayList<>();
        // initialize list of alive gold
        aliveGold.clear();
        Location next = new Location();
        // find alive gold
        for (Location gold : arr1){
            if (!arr2.contains(gold)){
                aliveGold.add(gold);
            }
        }
        // random
        Collections.shuffle(aliveGold);
        Collections.shuffle(arr2);
        // look through alive gold first, then eaten golds
        if (!aliveGold.isEmpty()){
            for (Location gold : aliveGold){
                next = gold;
                break;
            }
        }else{
            for (Location gold : arr2){
                next = gold;
                break;
            }
        }
        return next;
    }
}
