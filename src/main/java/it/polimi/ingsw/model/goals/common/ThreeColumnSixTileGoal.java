package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.HashSet;

public class ThreeColumnSixTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Three columns, each formed by 6 tiles of maximum 3 different types.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        int colCounter = 0;
        for (int j = 0; j < 5; j++) {
            HashSet<Tile.Type> uniqueTiles = new HashSet<>();
            boolean skipColumn = false;
    
            for (int i = 0; i < 6; i++) {
                Tile tile = shelf.getTile(i, j);
        
                if ( tile == Tile.NOTILE ) {
                    skipColumn = true;
                    break;
                }
                uniqueTiles.add(tile.type());
            }
    
            if ( !skipColumn && uniqueTiles.size() <= 3 ) colCounter++;
        }
        return colCounter >= 3;
    }
}
