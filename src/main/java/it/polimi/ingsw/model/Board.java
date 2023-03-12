package it.polimi.ingsw.model;

import java.util.*;

public class Board {

    private Map<Coordinate, Tile> TileOccupancy;

    protected Board() {};

    public Tile getTile(Coordinate coordinate) {};

    public Map<Coordinate, Tile> getTiles() {};

    public void removeSelection(List<Coordinate> selection) {};

    public void insertTile(Coordinate coordinate, Tile tile) {};
}
