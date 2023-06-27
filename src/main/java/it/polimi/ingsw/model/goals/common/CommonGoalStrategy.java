package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

/**
 * Interface implemented by each specific type of {@link CommonGoal} instance
 */
public interface CommonGoalStrategy {
    
    /**
     * Get a text description of the goal to achieve
     *
     * @return Goal description string
     */
    String getDescription();
    
    /**
     * Checks if the given goal has been achieved on the shelf
     *
     * @param shelf Shelf to check for the common goal
     *
     * @return true if common goal is achieved, else false
     */
    boolean checkShelf(Shelf shelf);
    
}
