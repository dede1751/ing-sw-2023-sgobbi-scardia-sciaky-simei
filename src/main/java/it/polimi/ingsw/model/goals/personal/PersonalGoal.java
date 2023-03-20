package it.polimi.ingsw.model.goals.personal;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the game's personal goals
 * This class is immutable
 */
public class PersonalGoal {
    
    private final int goalIndex;
    
    private final Map<Coordinate, Tile> goal;
    
    private final static PersonalGoal[] goalList = {
            new PersonalGoal(0), new PersonalGoal(1), new PersonalGoal(2),
            new PersonalGoal(3), new PersonalGoal(4), new PersonalGoal(5),
            new PersonalGoal(6), new PersonalGoal(7), new PersonalGoal(8),
            new PersonalGoal(9), new PersonalGoal(10), new PersonalGoal(11),
    };
    
    /**
     * Initialize personal goal from unique goal index
     * Goals can only be created internally, only pre-initialized ones can actually be used.
     * @param goalIndex Unique integer ID of the goal
     */
    private PersonalGoal(int goalIndex) {
        this.goalIndex = goalIndex;
        this.goal = new HashMap<>();
        
        switch (goalIndex) {
            case 0 -> {
                this.goal.put(new Coordinate(4, 4), Tile.CATS);
                this.goal.put(new Coordinate(3, 3), Tile.BOOKS);
                this.goal.put(new Coordinate(2, 1), Tile.GAMES);
                this.goal.put(new Coordinate(5, 2), Tile.FRAMES);
                this.goal.put(new Coordinate(0, 2), Tile.TROPHIES);
                this.goal.put(new Coordinate(5, 0), Tile.PLANTS);
            }
            case 1 -> {
                this.goal.put(new Coordinate(3, 0), Tile.CATS);
                this.goal.put(new Coordinate(4, 2), Tile.BOOKS);
                this.goal.put(new Coordinate(3, 2), Tile.GAMES);
                this.goal.put(new Coordinate(0, 4), Tile.FRAMES);
                this.goal.put(new Coordinate(1, 3), Tile.TROPHIES);
                this.goal.put(new Coordinate(4, 1), Tile.PLANTS);
            }
            case 2 -> {
                this.goal.put(new Coordinate(2, 1), Tile.CATS);
                this.goal.put(new Coordinate(0, 0), Tile.BOOKS);
                this.goal.put(new Coordinate(4, 3), Tile.GAMES);
                this.goal.put(new Coordinate(4, 0), Tile.FRAMES);
                this.goal.put(new Coordinate(2, 4), Tile.TROPHIES);
                this.goal.put(new Coordinate(3, 2), Tile.PLANTS);
            }
            case 3 -> {
                this.goal.put(new Coordinate(1, 2), Tile.CATS);
                this.goal.put(new Coordinate(1, 1), Tile.BOOKS);
                this.goal.put(new Coordinate(5, 4), Tile.GAMES);
                this.goal.put(new Coordinate(3, 2), Tile.FRAMES);
                this.goal.put(new Coordinate(3, 0), Tile.TROPHIES);
                this.goal.put(new Coordinate(2, 3), Tile.PLANTS);
            }
            case 4 -> {
                this.goal.put(new Coordinate(0, 3), Tile.CATS);
                this.goal.put(new Coordinate(2, 2), Tile.BOOKS);
                this.goal.put(new Coordinate(0, 0), Tile.GAMES);
                this.goal.put(new Coordinate(3, 1), Tile.FRAMES);
                this.goal.put(new Coordinate(4, 1), Tile.TROPHIES);
                this.goal.put(new Coordinate(1, 4), Tile.PLANTS);
            }
            case 5 -> {
                this.goal.put(new Coordinate(5, 4), Tile.CATS);
                this.goal.put(new Coordinate(3, 3), Tile.BOOKS);
                this.goal.put(new Coordinate(1, 1), Tile.GAMES);
                this.goal.put(new Coordinate(1, 3), Tile.FRAMES);
                this.goal.put(new Coordinate(5, 2), Tile.TROPHIES);
                this.goal.put(new Coordinate(0, 0), Tile.PLANTS);
            }
            case 6 -> {
                this.goal.put(new Coordinate(5, 0), Tile.CATS);
                this.goal.put(new Coordinate(0, 2), Tile.BOOKS);
                this.goal.put(new Coordinate(1, 4), Tile.GAMES);
                this.goal.put(new Coordinate(4, 4), Tile.FRAMES);
                this.goal.put(new Coordinate(2, 0), Tile.TROPHIES);
                this.goal.put(new Coordinate(3, 1), Tile.PLANTS);
            }
            case 7 -> {
                this.goal.put(new Coordinate(4, 1), Tile.CATS);
                this.goal.put(new Coordinate(1, 3), Tile.BOOKS);
                this.goal.put(new Coordinate(0, 3), Tile.GAMES);
                this.goal.put(new Coordinate(5, 4), Tile.FRAMES);
                this.goal.put(new Coordinate(3, 2), Tile.TROPHIES);
                this.goal.put(new Coordinate(2, 0), Tile.PLANTS);
            }
            case 8 -> {
                this.goal.put(new Coordinate(3, 2), Tile.CATS);
                this.goal.put(new Coordinate(2, 4), Tile.BOOKS);
                this.goal.put(new Coordinate(5, 2), Tile.GAMES);
                this.goal.put(new Coordinate(0, 0), Tile.FRAMES);
                this.goal.put(new Coordinate(1, 1), Tile.TROPHIES);
                this.goal.put(new Coordinate(1, 4), Tile.PLANTS);
            }
            case 9 -> {
                this.goal.put(new Coordinate(2, 3), Tile.CATS);
                this.goal.put(new Coordinate(3, 0), Tile.BOOKS);
                this.goal.put(new Coordinate(4, 1), Tile.GAMES);
                this.goal.put(new Coordinate(1, 1), Tile.FRAMES);
                this.goal.put(new Coordinate(5, 4), Tile.TROPHIES);
                this.goal.put(new Coordinate(0, 3), Tile.PLANTS);
            }
            case 10 -> {
                this.goal.put(new Coordinate(1, 4), Tile.CATS);
                this.goal.put(new Coordinate(4, 1), Tile.BOOKS);
                this.goal.put(new Coordinate(3, 0), Tile.GAMES);
                this.goal.put(new Coordinate(2, 2), Tile.FRAMES);
                this.goal.put(new Coordinate(0, 3), Tile.TROPHIES);
                this.goal.put(new Coordinate(5, 2), Tile.PLANTS);
            }
            case 11 -> {
                this.goal.put(new Coordinate(0, 0), Tile.CATS);
                this.goal.put(new Coordinate(5, 2), Tile.BOOKS);
                this.goal.put(new Coordinate(1, 4), Tile.GAMES);
                this.goal.put(new Coordinate(3, 2), Tile.FRAMES);
                this.goal.put(new Coordinate(2, 3), Tile.TROPHIES);
                this.goal.put(new Coordinate(4, 1), Tile.PLANTS);
            }
            default -> throw new IndexOutOfBoundsException("Goal index must be between 0 and 11, got: " + goalIndex);
        }
    }
    
    /**
     * Get instance of personal goal with given id
     * @param goalIndex Unique integer id of goal to get
     * @return          Goal with given id
     */
    public static PersonalGoal getPersonalGoal(int goalIndex) { return goalList[goalIndex]; }
    
    /**
     * Get the unique id of the goal
     * @return Unique integer id of goal
     */
    public int getGoalIndex() { return this.goalIndex; }
    
    /**
     * Get the contents of the goal as a map of coordinates and their contents.
     * @return Copy of coordinate->tile mapping
     */
    public Map<Coordinate, Tile> getGoal() { return new HashMap<>(this.goal); }
    
    /**
     * Checks the goal against the given shelf and computes the score
     * @param shelf Shelf to check for goal achievement
     * @return      Score obtained from goal completion
     */
    public int checkGoal(Shelf shelf) {
        long count = this.goal.entrySet()
                .stream()
                .filter((coordinateTileEntry -> {
                    Coordinate c = coordinateTileEntry.getKey();
                    Tile tile = shelf.getTile(c.row(), c.col());
                    
                    return tile == coordinateTileEntry.getValue();
                }))
                .count();
        
        final int[] scores = { 0, 1, 2, 4, 6, 9, 12 };
        
        return scores[(int) count];
    }
    
}