package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.HashSet;

/**
 * Implements the FourRowFiveTileGoal goal, involving four rows of five tiles of maximum three different types.
 */
public class FourRowFiveTileGoal implements CommonGoalStrategy {
    
    /**
     * Default constructor to appease Javadoc.
     */
    public FourRowFiveTileGoal(){}
    
    @Override
    public String getDescription() {
        return "Four rows, each formed by 5 tiles of maximum 3 different types.";
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
            
            if( !skipRow && uniqueTiles.size() <= 3 )
                rowCounter++;
        }
        return rowCounter >= 4;
    }
    
}
