package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class ThreeColumnSixTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Three columns, each formed by 6 tiles of maximum 3 different types.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
