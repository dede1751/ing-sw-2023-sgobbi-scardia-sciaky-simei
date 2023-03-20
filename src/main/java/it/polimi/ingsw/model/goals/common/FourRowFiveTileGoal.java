package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class FourRowFiveTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Four rows, each formed by 5 tiles of maximum 3 different types.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
}
