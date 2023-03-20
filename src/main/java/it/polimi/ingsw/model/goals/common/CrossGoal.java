package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class CrossGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Five tiles of the same type forming an X shape.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                if ((shelf.getTile(i, j) != Tile.NOTILE) &&
                    (shelf.getTile(i, j) == shelf.getTile(i-1, j-1)) &&
                    (shelf.getTile(i, j) == shelf.getTile(i, j+2)) &&
                    (shelf.getTile(i, j) == shelf.getTile(i-2, j)) &&
                    (shelf.getTile(i, j) == shelf.getTile(i-2, j+2)))
                    return true;
            }
        }
        return false;
    }
}
