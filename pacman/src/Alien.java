package src;

import ch.aplu.jgamegrid.Location;

import java.util.*;

public class Alien extends Monster {
    public Alien(GameState gameState) {
        super(gameState, MonsterType.Alien);
    }

    @Override
    public void walkApproach() {
        // TODO: Add updated walk approach
        // Get 8 cells around us (that are valid places to move)
        // Calculate distance from each place to pacman (pacman available in GameState)
        // Move to the closest space
        Location loc = getLocation();
        int dis = loc.getDistanceTo(gameState.getPacActor().getLocation());
        ArrayList<Location> cellsAround = new ArrayList<>();
        ArrayList<Location> orderCells = new ArrayList<>();
        Map<Location, Integer> dict = new HashMap<Location, Integer>();
        ArrayList<Integer> disLst = new ArrayList<>();
        ArrayList<Location> subCells = new ArrayList<>();
        Location next = new Location();
        // initialize all lists
        cellsAround.clear();
        subCells.clear();
        disLst.clear();
        orderCells.clear();
        // get all neighbour locations and select the surrounding 8 cells
        cellsAround = loc.getNeighbourLocations(dis);
        //System.out.println(cellsAround);
        if (cellsAround.size() >= 8) {
            for (int i = 0; i < 8; i++) {
                subCells.add(cellsAround.get(i));
            }
        }
        //System.out.println(subCells);
        // prepare to make neighbour cells in order by distance
        for (Location cell : subCells) {
            int dis2 = cell.getDistanceTo(gameState.getPacActor().getLocation());
            dict.put(cell, dis2);
            disLst.add(dis2);
        }
        // sort the distance
        Collections.sort(disLst);
        // sort the cells by distance
        Iterator iter = dict.entrySet().iterator();
        for (int dis2 : disLst) {
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (val.equals(dis2)) {
                    orderCells.add((Location) key);
                }
            }
        }
        // move to the shortest distance cell
        for (Location cell : orderCells) {
            if (canMove(cell)) {
                if (isFurious){
                    Location probcell = findNext(next, getLocation());
                    if (canMove(probcell)){
                        next = probcell;
                    }
                }
                next = cell;
                setLocation(next);
                break;
            }
        }
        gameState.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }

}
