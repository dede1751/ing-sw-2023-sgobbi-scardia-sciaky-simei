package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

/**
 * Interface implemented by each specific type of {@link CommonGoal}
 */
public interface CommonGoalStrategy {
    
    /**
     * Get a text description of the goal to achieve
     * @return Goal description string
     */
    public String getDescription();
    
    /**
     * Checks if the given goal has been achieved on the shelf
     * @param shelf Shelf to check for the common goal
     * @return      true if common goal is achieved, else false
     */
    public boolean checkShelf(Shelf shelf);
    
}
