package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.exceptions.CommonException;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;

import java.util.*;
import java.util.function.Predicate;

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
    
    public void refill(TileBag tileBag) {
        var coord_list = tileOccupancy.keySet().stream().toList();
        this.removeSelection(coord_list);
        var selected_matrix = new boolean[9][9];
        
        var engine = new Random();
        var initial = coord_list.get(engine.nextInt(0, coord_list.size()));
        var selected = new ArrayList<Coordinate>();
        Predicate<Coordinate> is_valid = (x) -> this.tileOccupancy.containsKey(x) &&
                                                !selected_matrix[x.getRow()][x.getCol()];
        
        int nTiles = Math.min(tileBag.currentTileNumber(), tileOccupancy.size());
        Queue<Coordinate> e = new LinkedList<>();
        e.add(initial);
        int i = 1;
        
        while( !e.isEmpty() ) {
            var current = e.peek();
            selected.add(e.remove());
            selected_matrix[current.getRow()][current.getCol()] = true;
            if( is_valid.test(current.getDown()) && i < nTiles ) {
                e.add(current.getDown());
                selected_matrix[current.getDown().getRow()][current.getDown().getCol()] = true;
                i++;
            }
            if( is_valid.test(current.getLeft()) && i < nTiles ) {
                e.add(current.getLeft());
                selected_matrix[current.getLeft().getRow()][current.getLeft().getCol()] = true;
                i++;
            }
            if( is_valid.test(current.getRight()) && i < nTiles ) {
                e.add(current.getRight());
                selected_matrix[current.getRight().getRow()][current.getRight().getCol()] = true;
                i++;
            }
            if( is_valid.test(current.getUp()) && i < nTiles ) {
                e.add(current.getUp());
                selected_matrix[current.getUp().getRow()][current.getUp().getCol()] = true;
                i++;
            }
        }
        var map = tileBag.getAllBag();
        var it = Tile.values();
        try {
            for( var x : selected ) {
                var randomTile = 0;
                do {
                    randomTile = engine.nextInt(0, 6); //git blame someone-else
                }
                while( map.get(it[randomTile]) == 0 );
                
                insertTile(x, it[randomTile]);
            }
        }
        catch( CommonException ex ) {
            System.err.println("there shouldn't be an error in there...");
        }
    }
    
}
