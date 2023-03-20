package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class CrossGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Five tiles of the same type forming an X shape.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
