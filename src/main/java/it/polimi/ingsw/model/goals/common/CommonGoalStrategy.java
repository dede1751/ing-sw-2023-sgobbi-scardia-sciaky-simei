package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

import java.util.List;

/**
 * Interface implemented by each specific type of {@link CommonGoal}
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

    /**
     * Utility inner record representing a coordinate (r, c)
     *
     * @param r row of the coordinate
     * @param c column of the coordinate
     */
    record Coord(int r, int c) {
        Coord sum(Coord offset) {
            return new Coord(r + offset.r, c + offset.c);
        }
        
        Coord sub(Coord offset) {
            return new Coord(r - offset.r, c - offset.c);
        }
        
        List<Coord> sumList(List<Coord> offset) {
            return offset.stream().map((x) -> x.sum(this)).toList();
        }
        
        List<Coord> sumDir() {
            return this.sumList(List.of(new Coord(-1, 0), new Coord(1, 0), new Coord(0, -1), new Coord(0, 1)));
        }
        
    }

}
