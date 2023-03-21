package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class DecreasingColumnsGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Five columns of increasing or decreasing height, with tiles of any type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return (((shelf.getTile(5, 0) == null) &&
                 (shelf.getTile(4, 1) == null) &&
                 (shelf.getTile(3, 2) == null) &&
                 (shelf.getTile(2, 3) == null) &&
                 (shelf.getTile(1, 4) == null)) ||
                
                ((shelf.getTile(5, 0) != null) &&
                 (shelf.getTile(5, 1) == null) &&
                 (shelf.getTile(4, 2) == null) &&
                 (shelf.getTile(3, 3) == null) &&
                 (shelf.getTile(2, 4) == null)) ||

                ((shelf.getTile(5, 4) == null) &&
                 (shelf.getTile(4, 3) == null) &&
                 (shelf.getTile(3, 2) == null) &&
                 (shelf.getTile(2, 1) == null) &&
                 (shelf.getTile(1, 0) == null)) ||

                ((shelf.getTile(5, 4) != null) &&
                 (shelf.getTile(5, 3) == null) &&
                 (shelf.getTile(4, 2) == null) &&
                 (shelf.getTile(3, 1) == null) &&
                 (shelf.getTile(2, 0) == null)));
    }
}
