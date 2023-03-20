package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class DiagonalFiveTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Five tiles of the same type forming a diagonal.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
