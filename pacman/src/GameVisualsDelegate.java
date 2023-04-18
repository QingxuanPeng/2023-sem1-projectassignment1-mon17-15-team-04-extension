package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
import java.util.Properties;

public interface GameVisualsDelegate {
    void addActor(Actor actor, Location location);

    Point toPoint(Location location);

    GameState getGameState();
    Properties getProperties();
}
