package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.HashSet;

/**
 * Implements the TwoRowDistinctGoal goal, involving two rows of five tiles of five different types.
 */
public class TwoRowDistinctGoal implements CommonGoalStrategy {
    
    /**
     * Default constructor to appease Javadoc.
     */
    public TwoRowDistinctGoal(){}
    
    @Override
    public String getDescription() {
        return "Two rows each formed by 5 distinct types of tiles.";
    }
    
    @Override
    public boolean checkShelf(Shelf shelf) {
        int rowCounter = 0;
        for( int i = 0; i < 6; i++ ) {
            HashSet<Tile.Type> uniqueTiles = new HashSet<>();
            boolean skipRow = false;
            
            for( int j = 0; j < 5; j++ ) {
                Tile tile = shelf.getTile(i, j);
                
                if( tile == Tile.NOTILE ) {
                    skipRow = true;
                    break;
                }
                uniqueTiles.add(tile.type());
            }
            
            if( !skipRow && uniqueTiles.size() == 5 )
                rowCounter++;
        }
        return rowCounter >= 2;
    }
}
