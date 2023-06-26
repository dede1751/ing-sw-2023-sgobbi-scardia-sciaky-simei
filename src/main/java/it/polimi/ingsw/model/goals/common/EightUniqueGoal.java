package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.HashMap;

/**
 * Common goal strategy for the EightUnique goal, involving 8 tiles of the same type in any position.
 */
public class EightUniqueGoal implements CommonGoalStrategy {
    
    /**
     * Default constructor to appease Javadoc.
     */
    public EightUniqueGoal(){}
    
    @Override
    public String getDescription() {
        return "Eight tiles of the same type, in any position.";
    }
    
    @Override
    public boolean checkShelf(Shelf shelf) {
        HashMap<Tile.Type, Integer> tileCounter = new HashMap<>();
        
        for( int i = 0; i < 6; i++ ) {
            for( int j = 0; j < 5; j++ ) {
                Tile.Type type = shelf.getTile(i, j).type();
                
                if( type != Tile.Type.NOTILE ) {
                    tileCounter.put(type, tileCounter.getOrDefault(type, 0) + 1);
                }
            }
        }
        return tileCounter.values().stream().anyMatch(x -> x >= 8);
    }
    
}
