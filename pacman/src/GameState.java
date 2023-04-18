package src;

import ch.aplu.jgamegrid.Location;
import src.utility.GameCallback;

import java.util.ArrayList;
import java.util.Properties;

public class GameState {
    private final GameCallback gameCallback;
    private final int nbHorzCells;
    private final int nbVertCells;
    private final PacManGameGrid grid;
    private final ArrayList<Location> propertyPillLocations = new ArrayList<>();
    protected final ArrayList<Location> propertyGoldLocations = new ArrayList<>();
    private final ArrayList<Location> pillAndItemLocations = new ArrayList<Location>();
    private final Properties properties;
    private final Monster troll;
    private final Monster tx5;
    private final Monster orion;
    private final Monster alien;
    private final Monster wizard;
    private int seed;
    private final PacActor pacActor;
    private long timeGoldEaten = Long.MIN_VALUE;
    private long timeIceEaten = Long.MIN_VALUE;
    public static final double NANO_IN_SEC = 1000000000.0;
    public static final int TIME_FURIOUS = 3;
    public static final int TIME_FROZEN = 3;

    public GameState(GameCallback gameCallback, int nbHorzCells, int nbVertCells, Properties properties) {
        this.gameCallback = gameCallback;
        this.nbHorzCells = nbHorzCells;
        this.nbVertCells = nbVertCells;
        this.properties = properties;

        this.timeGoldEaten = -TIME_FURIOUS * (long)NANO_IN_SEC;
        this.timeIceEaten = -TIME_FROZEN * (long)NANO_IN_SEC;

        this.pacActor = new PacActor(this);
        this.troll = new Troll(this);
        this.tx5 = new Tx5(this);
        this.orion = new Orion(this);
        this.alien = new Alien(this);
        this.wizard = new Wizard(this);
        grid = new PacManGameGrid(nbHorzCells, nbVertCells);
    }

    public GameCallback getGameCallback() {
        return gameCallback;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public PacManGameGrid getGrid() {
        return grid;
    }

    public ArrayList<Location> getPillAndItemLocations() {
        return pillAndItemLocations;
    }

    public ArrayList<Location> getPropertyPillLocations() {
        return propertyPillLocations;
    }

    public ArrayList<Location> getPropertyGoldLocations() {
        return propertyGoldLocations;
    }

    public int getNumHorzCells() {
        return nbHorzCells;
    }

    public int getNumVertCells() {
        return nbVertCells;
    }

    public PacActor getPacActor() {
        return pacActor;
    }

    public Monster getTroll() {
        return troll;
    }

    public Monster getTx5() {
        return tx5;
    }

    public Monster getOrion() {
        return orion;
    }

    public Monster getAlien() {
        return alien;
    }
    public Monster getWizard() {
        return wizard;
    }

    public long getTimeGoldEaten() {
        return timeGoldEaten;
    }

    public void setTimeGoldEaten(long timeGoldEaten) {
        this.timeGoldEaten = timeGoldEaten;
    }

    public long getTimeIceEaten() {
        return timeIceEaten;
    }

    public void setTimeIceEaten(long timeIceEaten) {
        this.timeIceEaten = timeIceEaten;
    }

    public int countPillsAndItems() {
        int pillsAndItemsCount = 0;
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) { // Pill
                    pillsAndItemsCount++;
                } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
                    pillsAndItemsCount++;
                }
            }
        }
        if (propertyPillLocations.size() != 0) {
            pillsAndItemsCount += propertyPillLocations.size();
        }

        if (propertyGoldLocations.size() != 0) {
            pillsAndItemsCount += propertyGoldLocations.size();
        }

        return pillsAndItemsCount;
    }

    public void loadPillAndItemsLocations() {
        String pillsLocationString = properties.getProperty("Pills.location");
        if (pillsLocationString != null) {
            String[] singlePillLocationStrings = pillsLocationString.split(";");
            for (String singlePillLocationString : singlePillLocationStrings) {
                String[] locationStrings = singlePillLocationString.split(",");
                propertyPillLocations.add(new Location(Integer.parseInt(locationStrings[0]),
                        Integer.parseInt(locationStrings[1])));
            }
        }

        String goldLocationString = properties.getProperty("Gold.location");
        if (goldLocationString != null) {
            String[] singleGoldLocationStrings = goldLocationString.split(";");
            for (String singleGoldLocationString : singleGoldLocationStrings) {
                String[] locationStrings = singleGoldLocationString.split(",");
                propertyGoldLocations.add(new Location(Integer.parseInt(locationStrings[0]),
                        Integer.parseInt(locationStrings[1])));
            }
        }
    }

    public void setupPillAndItemsLocations() {
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 3 && propertyGoldLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 4) {
                    pillAndItemLocations.add(location);
                }
            }
        }


        if (propertyPillLocations.size() > 0) {
            for (Location location : propertyPillLocations) {
                pillAndItemLocations.add(location);
            }
        }
        if (propertyGoldLocations.size() > 0) {
            for (Location location : propertyGoldLocations) {
                pillAndItemLocations.add(location);
            }
        }
    }

    public double timeSinceGoldEaten() {
        return (System.nanoTime() - timeGoldEaten) / NANO_IN_SEC;
    }

    public double timeSinceIceEaten() {
        return (System.nanoTime() - timeIceEaten) / NANO_IN_SEC;
    }
}
