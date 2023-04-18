package src;

import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Wizard extends Monster {
    public Wizard(GameState gameState) {
        super(gameState, MonsterType.Wizard);
    }

    @Override
    public void walkApproach() {
        // TODO: Add updated walk approach
        Location loc = getLocation();
        Location next = new Location();
        //Location cell = new Location();
        ArrayList<Location> cellaround = new ArrayList<>();
        // initialize the list
        cellaround.clear();
        // find the 8 cells around
        cellaround = getAround(loc, cellaround);
        // pick one cell randomly
        Collections.shuffle(cellaround);
        for (Location cell: cellaround){
            if (canMove(cell)){ // not maze wall, can move
                next = cell;
                if (isFurious){
                    Location probcell = findNext(next, getLocation());
                    if (canMove(probcell)){
                        next = probcell;
                    }
                }
                setLocation(next);
                break;
            }else{ // cannot move
                Color c = getBackground().getColor(cell);
                if (c.equals(Color.gray)){ // maze wall check
                    Location cell2 = new Location();
                    // find the adjacent cells around wall cell
                    cell2 = findNext(cell, loc);
                    // can move situation
                    if (canMove(cell2)){
                        next = cell2;
                        if (isFurious){
                            Location probcell = findNext(next, getLocation());
                            if (canMove(probcell)){
                                next = probcell;
                            }
                        }
                        setLocation(next);
                        //game.getGameCallback().monsterLocationChanged(this);
                        break;
                    }
                }
            }
        }

        gameState.getGameCallback().monsterLocationChanged(this);
    }

    private ArrayList<Location> getAround(Location loc, ArrayList<Location> arr){
        //arr.clear();
        // L
        Location cell1 = new Location();
        cell1.x = loc.getX()-1;
        cell1.y = loc.getY();
        arr.add(0, cell1);
        // R
        Location cell2 = new Location();
        cell2.x = loc.getX()+1;
        cell2.y = loc.getY();
        arr.add(cell2);
        // U
        Location cell3 = new Location();
        cell3.x = loc.getX();
        cell3.y = loc.getY()-1;
        arr.add(cell3);
        // D
        Location cell4 = new Location();
        cell4.x = loc.getX();
        cell4.y = loc.getY()+1;
        arr.add(cell4);
        // UL
        Location cell5 = new Location();
        cell5.x = loc.getX()-1;
        cell5.y = loc.getY()-1;
        arr.add(cell5);
        // DL
        Location cell6 = new Location();
        cell6.x = loc.getX()-1;
        cell6.y = loc.getY()+1;
        arr.add(cell6);
        // UR
        Location cell7 = new Location();
        cell7.x = loc.getX()+1;
        cell7.y = loc.getY()-1;
        arr.add(cell7);
        // DR
        Location cell8 = new Location();
        cell8.x = loc.getX()+1;
        cell8.y = loc.getX()+1;
        arr.add(cell8);
        return arr;
    }

}
