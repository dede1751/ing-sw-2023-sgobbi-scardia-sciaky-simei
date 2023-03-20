package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class SixGroupTwoTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Six groups, each containing at least 2 tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
