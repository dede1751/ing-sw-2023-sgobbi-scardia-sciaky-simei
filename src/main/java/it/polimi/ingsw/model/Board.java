package it.polimi.ingsw.model;

import java.util.*;

public class Board {

    private final Map<Coordinate, Tile> tileOccupancy;

    protected Board(int numPlayers) {
        this.tileOccupancy = new HashMap<Coordinate, Tile>();

        Coordinate[] commonCoordinates = {
                new Coordinate(1, 3), new Coordinate(1, 4), new Coordinate(2, 3),
                new Coordinate(2, 4), new Coordinate(2, 5), new Coordinate(3, 2),
                new Coordinate(3, 3), new Coordinate(3, 4), new Coordinate(3, 5),
                new Coordinate(3, 6), new Coordinate(3, 7), new Coordinate(4, 1),
                new Coordinate(4, 2), new Coordinate(4, 3), new Coordinate(4, 4),
                new Coordinate(4, 5), new Coordinate(4, 6), new Coordinate(4, 7),
                new Coordinate(5, 1), new Coordinate(5, 2), new Coordinate(5, 3),
                new Coordinate(5, 4), new Coordinate(5, 5), new Coordinate(5, 6),
                new Coordinate(6, 3), new Coordinate(6, 4), new Coordinate(6, 5),
                new Coordinate(7, 4), new Coordinate(7, 5),
        };
        for (Coordinate coordinate: commonCoordinates) {
            this.tileOccupancy.put(coordinate, Tile.NOTILE);
        }

        if (numPlayers > 2) {
            Coordinate[] threePlayerCoordinates = {
                    new Coordinate(0, 3), new Coordinate(2, 2), new Coordinate(2, 6),
                    new Coordinate(3, 8), new Coordinate(5, 0), new Coordinate(6, 2),
                    new Coordinate(6, 6), new Coordinate(8, 5),
            };
            for (Coordinate coordinate: threePlayerCoordinates) {
                this.tileOccupancy.put(coordinate, Tile.NOTILE);
            }
        }

        if (numPlayers > 3) {
            Coordinate[] fourPlayerCoordinates = {
                    new Coordinate(0, 4), new Coordinate(1, 5), new Coordinate(3, 1),
                    new Coordinate(4, 0), new Coordinate(4, 8), new Coordinate(5, 7),
                    new Coordinate(7, 3), new Coordinate(8, 4),
            };
            for (Coordinate coordinate: fourPlayerCoordinates) {
                this.tileOccupancy.put(coordinate, Tile.NOTILE);
            }
        }
    };

    public Tile getTile(Coordinate coordinate) {
        return this.tileOccupancy.get(coordinate);
    };

    public Map<Coordinate, Tile> getTiles() {
        return new HashMap<Coordinate, Tile>(this.tileOccupancy);
    };

    public void removeSelection(List<Coordinate> selection) {
        for (Coordinate coordinate: selection) {
            this.tileOccupancy.put(coordinate, Tile.NOTILE);
        }
    };

    public void insertTile(Coordinate coordinate, Tile tile) {
        this.tileOccupancy.put(coordinate, tile);
    };
}
