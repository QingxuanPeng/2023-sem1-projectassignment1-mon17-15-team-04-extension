package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.util.ArrayList;
import java.util.Properties;

public class GameVisuals {
    private final GameVisualsDelegate game;

    private GameState getGameState() {
        return game.getGameState();
    }
    private Properties getProperties(){
        return game.getProperties();
    };

    private final ArrayList<Actor> iceCubes = new ArrayList<Actor>();
    private final ArrayList<Actor> goldPieces = new ArrayList<Actor>();

    public GameVisuals(GameVisualsDelegate game) {
        this.game = game;
    }

    public void drawGrid(GGBackground bg) {
        bg.clear(Color.gray);
        bg.setPaintColor(Color.white);
        for (int y = 0; y < game.getGameState().getNumVertCells(); y++) {
            for (int x = 0; x < game.getGameState().getNumHorzCells(); x++) {
                bg.setPaintColor(Color.white);
                Location location = new Location(x, y);
                int a = game.getGameState().getGrid().getCell(location);
                if (a > 0)
                    bg.fillCell(location, Color.lightGray);
                if (a == 1 && game.getGameState().getPropertyPillLocations().size() == 0) { // Pill
                    putPill(bg, location);
                } else if (a == 3 && game.getGameState().getPropertyGoldLocations().size() == 0) { // Gold
                    putGold(bg, location);
                } else if (a == 4) {
                    putIce(bg, location);
                }
            }
        }

        for (Location location : game.getGameState().getPropertyPillLocations()) {
            putPill(bg, location);
        }

        for (Location location : game.getGameState().getPropertyGoldLocations()) {
            putGold(bg, location);
        }
    }

    private void putPill(GGBackground bg, Location location) {
        bg.fillCircle(game.toPoint(location), 5);
    }

    private void putGold(GGBackground bg, Location location) {
        bg.setPaintColor(Color.yellow);
        bg.fillCircle(game.toPoint(location), 5);
        Actor gold = new Actor("sprites/gold.png");
        this.goldPieces.add(gold);
        game.addActor(gold, location);
    }

    private void putIce(GGBackground bg, Location location) {
        bg.setPaintColor(Color.blue);
        bg.fillCircle(game.toPoint(location), 5);
        Actor ice = new Actor("sprites/ice.png");
        this.iceCubes.add(ice);
        game.addActor(ice, location);
    }

    public void removeItem(String type, Location location) {
        if (type.equals("gold")) {
            for (Actor item : this.goldPieces) {
                if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
                    item.hide();
                    // every time the gold be eaten, the monster will be furious, check it is not frozen
                    if (!getGameState().getTroll().isFrozen){
                        getGameState().getTx5().Furious(3);
                        getGameState().getTroll().Furious(3);
                        if (game.getProperties().getProperty("version").equals("multiverse")){
                            getGameState().getOrion().Furious(3);
                            getGameState().getAlien().Furious(3);
                            getGameState().getWizard().Furious(3);
                        }
                    }
                }
            }
        } else if (type.equals("ice")) {
            for (Actor item : this.iceCubes) {
                if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
                    item.hide();
                     // when pacActor eat ice, monster will be frozen 3 seconds
                    getGameState().getTx5().Frozen(3);
                    getGameState().getTroll().Frozen(3);
                    if (game.getProperties().getProperty("version").equals("multiverse")){
                        getGameState().getOrion().Frozen(3);
                        getGameState().getAlien().Frozen(3);
                        getGameState().getWizard().Frozen(3);
                    }
                }
            }
        }
    }
}
