package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

/**
 * Common goal strategy for the FourCorners goal, involving 4 tiles of the same type in the four corners of the shelf.
 */
public class FourCornersGoal implements CommonGoalStrategy {
    
    /**
     * Default constructor to appease Javadoc.
     */
    public FourCornersGoal() {
    }
    
    @Override
    public String getDescription() {
        return "Four tiles of the same type in the four corners of the shelf.";
    }
    
    @Override
    public boolean checkShelf(Shelf shelf) {
        return shelf.getTile(0, 0) != Tile.NOTILE
               && shelf.getTile(0, 0).type() == shelf.getTile(0, 4).type()
               && shelf.getTile(0, 4).type() == shelf.getTile(5, 0).type()
               && shelf.getTile(5, 0).type() == shelf.getTile(5, 4).type();
    }
    
}