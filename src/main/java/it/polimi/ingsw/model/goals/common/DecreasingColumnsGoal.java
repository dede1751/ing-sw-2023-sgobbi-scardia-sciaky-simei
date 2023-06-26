package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

/**
 * Common goal strategy for the DecreasingColumns goal, involving 5 columns of decreasing/increasing height.
 */
public class DecreasingColumnsGoal implements CommonGoalStrategy {
    
    /**
     * Default constructor to appease Javadoc.
     */
    public DecreasingColumnsGoal(){}
    
    @Override
    public String getDescription() {
        return "Five columns of increasing or decreasing height, with tiles of any type.";
    }
    
    @Override
    public boolean checkShelf(Shelf shelf) {
        return (((shelf.getTile(5, 0) == Tile.NOTILE && shelf.getTile(4, 0) != Tile.NOTILE) &&
                 (shelf.getTile(4, 1) == Tile.NOTILE && shelf.getTile(3, 1) != Tile.NOTILE) &&
                 (shelf.getTile(3, 2) == Tile.NOTILE && shelf.getTile(2, 2) != Tile.NOTILE) &&
                 (shelf.getTile(2, 3) == Tile.NOTILE && shelf.getTile(1, 3) != Tile.NOTILE) &&
                 (shelf.getTile(1, 4) == Tile.NOTILE && shelf.getTile(0, 4) != Tile.NOTILE)) ||
                
                ((shelf.getTile(5, 0) != Tile.NOTILE) &&
                 (shelf.getTile(5, 1) == Tile.NOTILE && shelf.getTile(4, 1) != Tile.NOTILE) &&
                 (shelf.getTile(4, 2) == Tile.NOTILE && shelf.getTile(3, 2) != Tile.NOTILE) &&
                 (shelf.getTile(3, 3) == Tile.NOTILE && shelf.getTile(2, 3) != Tile.NOTILE) &&
                 (shelf.getTile(2, 4) == Tile.NOTILE && shelf.getTile(1, 4) != Tile.NOTILE)) ||
                
                ((shelf.getTile(5, 4) == Tile.NOTILE && shelf.getTile(4, 4) != Tile.NOTILE) &&
                 (shelf.getTile(4, 3) == Tile.NOTILE && shelf.getTile(3, 3) != Tile.NOTILE) &&
                 (shelf.getTile(3, 2) == Tile.NOTILE && shelf.getTile(2, 2) != Tile.NOTILE) &&
                 (shelf.getTile(2, 1) == Tile.NOTILE && shelf.getTile(1, 1) != Tile.NOTILE) &&
                 (shelf.getTile(1, 0) == Tile.NOTILE && shelf.getTile(0, 0) != Tile.NOTILE)) ||
                
                ((shelf.getTile(5, 4) != Tile.NOTILE) &&
                 (shelf.getTile(5, 3) == Tile.NOTILE && shelf.getTile(4, 3) != Tile.NOTILE) &&
                 (shelf.getTile(4, 2) == Tile.NOTILE && shelf.getTile(3, 2) != Tile.NOTILE) &&
                 (shelf.getTile(3, 1) == Tile.NOTILE && shelf.getTile(2, 1) != Tile.NOTILE) &&
                 (shelf.getTile(2, 0) == Tile.NOTILE && shelf.getTile(1, 0) != Tile.NOTILE)));
    }
    
}
