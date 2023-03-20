package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

public class FourCornersGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Four tiles of the same type in the four corners of the shelf.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return false;
    }
    
}
