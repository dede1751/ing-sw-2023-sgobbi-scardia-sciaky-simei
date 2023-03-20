package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class TwoRowDistinctGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Two rows each formed by 5 distinct types of tiles.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
