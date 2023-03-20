package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class EightUniqueGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Eight tiles of the same type, in any position.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
