package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class CrossGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Five tiles of the same type forming an X shape.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        for( int i = 2; i < 6; i++ ) {
            for( int j = 0; j < 3; j++ ) {
                Tile tile = shelf.getTile(i, j);
                
                if( tile != Tile.NOTILE
                    && tile.type() == shelf.getTile(i - 1, j + 1).type()
                    && tile.type() == shelf.getTile(i, j + 2).type()
                    && tile.type() == shelf.getTile(i - 2, j).type()
                    && tile.type() == shelf.getTile(i - 2, j + 2).type() ) {
                    return true;
                }
            }
        }
        return false;
    }
    
}
