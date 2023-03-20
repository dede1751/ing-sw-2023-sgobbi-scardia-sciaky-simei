package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class DiagonalFiveTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Five tiles of the same type forming a diagonal.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return ((shelf.getTile(5, 0) == (shelf.getTile(4, 1))) &&
                (shelf.getTile(4, 1) == (shelf.getTile(3, 2))) &&
                (shelf.getTile(3, 2) == (shelf.getTile(2, 3))) &&
                (shelf.getTile(2, 3) == (shelf.getTile(1, 4)))) ||

               ((shelf.getTile(4, 0) == (shelf.getTile(3, 1))) &&
                (shelf.getTile(3, 1) == (shelf.getTile(2, 2))) &&
                (shelf.getTile(2, 2) == (shelf.getTile(1, 3))) &&
                (shelf.getTile(1, 3) == (shelf.getTile(0, 4)))) ||

               ((shelf.getTile(5, 4) == (shelf.getTile(4, 3))) &&
                (shelf.getTile(4, 3) == (shelf.getTile(3, 2))) &&
                (shelf.getTile(3, 2) == (shelf.getTile(2, 1))) &&
                (shelf.getTile(2, 1) == (shelf.getTile(1, 0)))) ||

               ((shelf.getTile(4, 4) == (shelf.getTile(3, 3))) &&
                (shelf.getTile(3, 3) == (shelf.getTile(2, 2))) &&
                (shelf.getTile(2, 2) == (shelf.getTile(1, 1))) &&
                (shelf.getTile(0, 0) == (shelf.getTile(0, 0))));
    }
    
}
