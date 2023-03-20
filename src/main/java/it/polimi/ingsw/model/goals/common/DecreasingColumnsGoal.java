package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class DecreasingColumnsGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Five columns of increasing or decreasing height, with tiles of any type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
