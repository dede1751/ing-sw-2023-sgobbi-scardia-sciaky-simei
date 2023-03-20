package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class TwoGroupSquareGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Two 2x2 squares of tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
