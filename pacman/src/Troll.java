package src;

import ch.aplu.jgamegrid.Location;

public class Troll extends Monster {
    public Troll(GameState gameState) {
        super(gameState, MonsterType.Troll);
    }

    @Override
    public void walkApproach() {
        Location pacLocation = gameState.getPacActor().getLocation();
        double oldDirection = getDirection();

        // Walking approach:
        // TX5: Determine direction to pacActor and try to move in that direction. Otherwise, random walk.
        // Troll: Random walk.
        Location.CompassDirection compassDir = getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);

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
        gameState.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }
}
