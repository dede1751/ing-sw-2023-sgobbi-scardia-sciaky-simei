package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class FourGroupFourTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Four groups, each containing at least 4 tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
