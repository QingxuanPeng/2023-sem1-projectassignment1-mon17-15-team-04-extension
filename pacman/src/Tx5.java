package src;

import ch.aplu.jgamegrid.Location;

public class Tx5 extends Monster {
    public Tx5(GameState gameState) {
        super(gameState, MonsterType.TX5);
    }

    @Override
    public void walkApproach() {
        Location pacLocation = gameState.getPacActor().getLocation();
        double oldDirection = getDirection();

        Location.CompassDirection compassDir = getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);
        if (!isVisited(next) && canMove(next)) {
            setLocation(next);
        } else {
            // Random walk
            int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
            setDirection(oldDirection);
            turn(sign * 90);  // Try to turn left/right
            next = getNextMoveLocation();
            if (canMove(next)) {
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
                if (canMove(next)) // Try to move forward
                {if (isFurious){
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
                    if (canMove(next)) {
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
}
