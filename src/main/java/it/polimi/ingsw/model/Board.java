package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Board representation for the model
 */
public class Board {
    
    private final Map<Coordinate, Tile> tileOccupancy;
    
    /**
     * Initialize board for the given number of players
     * The player number cannot change dynamically throughout the game
     *
     * @param numPlayers Total number of players for the game
     */
    protected Board(int numPlayers) {
        this.tileOccupancy = new HashMap<>();
        
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
        for( Coordinate coordinate : commonCoordinates ) {
            this.tileOccupancy.put(coordinate, Tile.NOTILE);
        }
        
        if( numPlayers > 2 ) {
            Coordinate[] threePlayerCoordinates = {
                    new Coordinate(0, 3), new Coordinate(2, 2), new Coordinate(2, 6),
                    new Coordinate(3, 8), new Coordinate(5, 0), new Coordinate(6, 2),
                    new Coordinate(6, 6), new Coordinate(8, 5),
            };
            for( Coordinate coordinate : threePlayerCoordinates ) {
                this.tileOccupancy.put(coordinate, Tile.NOTILE);
            }
        }
        
        if( numPlayers > 3 ) {
            Coordinate[] fourPlayerCoordinates = {
                    new Coordinate(0, 4), new Coordinate(1, 5), new Coordinate(3, 1),
                    new Coordinate(4, 0), new Coordinate(4, 8), new Coordinate(5, 7),
                    new Coordinate(7, 3), new Coordinate(8, 4),
            };
            for( Coordinate coordinate : fourPlayerCoordinates ) {
                this.tileOccupancy.put(coordinate, Tile.NOTILE);
            }
        }
    }
    
    /**
     * Get the tile on the board at the given coordinate
     *
     * @param coordinate Coordinate to search for a tile
     *
     * @return Tile at coordinate, null if coordinate is not in the board
     */
    public Tile getTile(Coordinate coordinate) {
        return this.tileOccupancy.get(coordinate);
    }
    
    /**
     * Get Coordinate to File mapping for the current board
     * The map only contains "legal" coordinates, meaning coordinates usable when playing with the number of players
     * the board has been initialized with.
     *
     * @return Copy of the Coordinate->Tile map
     */
    public Map<Coordinate, Tile> getTiles() {
        return new HashMap<>(this.tileOccupancy);
    }
    
    /**
     * Remove the tiles the list of selected coordinates.
     * Coordinates must be already present in the board.
     *
     * @param selection List of coordinates to be emptied
     */
    public void removeSelection(List<Coordinate> selection) {
        for( Coordinate coordinate : selection ) {
            this.tileOccupancy.put(coordinate, Tile.NOTILE);
        }
    }
    
    /**
     * Insert tile at given coordinate.
     * Coordinate must be already present in the board.
     *
     * @param coordinate Coordinate to set the tile at
     * @param tile       Tile to set at given coordinate
     *
     * @throws OutOfBoundCoordinateException If the coordinate doesn't belong to the board
     */
    public void insertTile(Coordinate coordinate, Tile tile) throws OutOfBoundCoordinateException, OccupiedTileException {
        if( tileOccupancy.containsKey(coordinate) )
            if( tileOccupancy.get(coordinate) == Tile.NOTILE )
                this.tileOccupancy.replace(coordinate, tile);
            else
                throw new OccupiedTileException();
        else
            throw new OutOfBoundCoordinateException(coordinate);
    }
}
