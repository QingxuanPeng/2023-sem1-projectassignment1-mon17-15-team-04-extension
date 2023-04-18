// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import src.utility.GameCallback;

import java.awt.*;
import java.util.Properties;

public class Game extends GameGrid implements GameVisualsDelegate {
    private final static int nbHorzCells = 20;
    private final static int nbVertCells = 11;
    private static final int cellSize = 20;

    private final GameState gameState;
    private final GameVisuals gameVisuals;
    public final Properties properties;

    public Game(GameCallback gameCallback, Properties properties) {
        //Setup game
        super(nbHorzCells, nbVertCells, cellSize, false);
        this.gameState = new GameState(gameCallback, nbHorzCells, nbVertCells, properties);
        this.gameVisuals = new GameVisuals(this);
        gameState.getPacActor().setGameVisuals(gameVisuals);
        this.properties = properties;
        setSimulationPeriod(100);
        setTitle("[PacMan in the Multiverse]");

        //Setup for auto test
        gameState.getPacActor().setPropertyMoves(properties.getProperty("PacMan.move"));
        gameState.getPacActor().setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
        gameState.loadPillAndItemsLocations();
        toPoint(new Location());
        GGBackground bg = getBg();
        gameVisuals.drawGrid(bg);

        //Setup Random seeds
        int seed = Integer.parseInt(properties.getProperty("seed"));
        gameState.setSeed(seed);
        gameState.getPacActor().setSeed(seed);
        gameState.getTroll().setSeed(seed);
        gameState.getTx5().setSeed(seed);
        gameState.getOrion().setSeed(seed);
        gameState.getAlien().setSeed(seed);
        gameState.getWizard().setSeed(seed);
        addKeyRepeatListener(gameState.getPacActor());
        setKeyRepeatPeriod(150);
        gameState.getTroll().setSlowDown(3);
        gameState.getTx5().setSlowDown(3);
        gameState.getPacActor().setSlowDown(3);
        gameState.getOrion().setSlowDown(3);
        gameState.getAlien().setSlowDown(3);
        gameState.getWizard().setSlowDown(3);
        if (!gameState.getTx5().isFrozen){
            gameState.getTx5().stopMoving(5);
        }
        setupActorLocations();

        //Run the game
        doRun();
        show();
        // Loop to look for collision in the application thread
        // This makes it improbable that we miss a hit
        boolean hasPacmanBeenHit;
        boolean hasPacmanEatAllPills;
        gameState.setupPillAndItemsLocations();
        int maxPillsAndItems = gameState.countPillsAndItems();

        do {
            if (properties.getProperty("version").equals("multiverse")){
                // check new monsters collision
                hasPacmanBeenHit =
                        getGameState().getTroll().getLocation().equals(getGameState().getPacActor().getLocation()) ||
                        getGameState().getTx5().getLocation().equals(getGameState().getPacActor().getLocation()) ||
                        getGameState().getOrion().getLocation().equals(getGameState().getPacActor().getLocation()) ||
                        getGameState().getAlien().getLocation().equals(getGameState().getPacActor().getLocation()) ||
                        getGameState().getWizard().getLocation().equals(getGameState().getPacActor().getLocation());
            }else{
                hasPacmanBeenHit =
                        gameState.getTroll().getLocation().equals(gameState.getPacActor().getLocation()) || gameState.getTx5().getLocation().equals(gameState.getPacActor().getLocation());
            }

            hasPacmanEatAllPills = gameState.getPacActor().getNbPills() >= maxPillsAndItems;
            delay(10);
        } while (!hasPacmanBeenHit && !hasPacmanEatAllPills);
        delay(120);

        Location loc = gameState.getPacActor().getLocation();
        gameState.getTroll().setStopMoving(true);
        gameState.getTx5().setStopMoving(true);
        gameState.getPacActor().removeSelf();

        String title = "";
        if (hasPacmanBeenHit) {
            bg.setPaintColor(Color.red);
            title = "GAME OVER";
            addActor(new Actor("sprites/explosion3.gif"), loc);
        } else if (hasPacmanEatAllPills) {
            bg.setPaintColor(Color.yellow);
            title = "YOU WIN";
        }
        setTitle(title);
        gameState.getGameCallback().endOfGame(title);

        doPause();
    }

    private void setupActorLocations() {
        String[] trollLocations = this.properties.getProperty("Troll.location").split(",");
        String[] tx5Locations = this.properties.getProperty("TX5.location").split(",");
        if (properties.getProperty("version").equals("multiverse")){
            String[] orionLocations = this.properties.getProperty("Orion.location").split(",");
            String[] alienLocations = this.properties.getProperty("Alien.location").split(",");
            String[] wizardLocations = this.properties.getProperty("Wizard.location").split(",");
            int orionX = Integer.parseInt(orionLocations[0]);
            int orionY = Integer.parseInt(orionLocations[1]);

            int alienX = Integer.parseInt(alienLocations[0]);
            int alienY = Integer.parseInt(alienLocations[1]);

            int wizardX = Integer.parseInt(wizardLocations[0]);
            int wizardY = Integer.parseInt(wizardLocations[1]);

            addActor(gameState.getOrion(), new Location(orionX, orionY), Location.NORTH);
            addActor(gameState.getAlien(), new Location(alienX, alienY), Location.NORTH);
            addActor(gameState.getWizard(), new Location(wizardX, wizardY), Location.NORTH);
        }
        String[] pacManLocations = this.properties.getProperty("PacMan.location").split(",");
        int trollX = Integer.parseInt(trollLocations[0]);
        int trollY = Integer.parseInt(trollLocations[1]);

        int tx5X = Integer.parseInt(tx5Locations[0]);
        int tx5Y = Integer.parseInt(tx5Locations[1]);

        int pacManX = Integer.parseInt(pacManLocations[0]);
        int pacManY = Integer.parseInt(pacManLocations[1]);


        addActor(gameState.getTroll(), new Location(trollX, trollY), Location.NORTH);
        addActor(gameState.getPacActor(), new Location(pacManX, pacManY));
        addActor(gameState.getTx5(), new Location(tx5X, tx5Y), Location.NORTH);
    }

    public GameState getGameState() {
        return gameState;
    }

    public Properties getProperties(){
        return properties;
    }
}
