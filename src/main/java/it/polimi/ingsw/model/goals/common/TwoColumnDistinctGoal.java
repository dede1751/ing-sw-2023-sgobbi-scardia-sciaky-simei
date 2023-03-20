package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class TwoColumnDistinctGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Two columns each formed by 6 distinct types of tiles.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
