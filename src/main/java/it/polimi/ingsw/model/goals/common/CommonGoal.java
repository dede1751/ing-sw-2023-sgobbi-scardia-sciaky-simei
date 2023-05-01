package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

/**
 * Class representing the game's common goals
 * This class is immutable
 */
public class CommonGoal {
    
    private final int goalIndex;
    
    private final CommonGoalStrategy strategy;
    
    private final static CommonGoal[] goalList = {
            new CommonGoal(0), new CommonGoal(1), new CommonGoal(2),
            new CommonGoal(3), new CommonGoal(4), new CommonGoal(5),
            new CommonGoal(6), new CommonGoal(7), new CommonGoal(8),
            new CommonGoal(9), new CommonGoal(10), new CommonGoal(11),
    };
    
    /**
     * Initialize common goal from unique goal index.
     * Goals can only be created internally, only pre-initialized ones can actually be used.
     *
     * @param goalIndex Unique integer ID of the goal
     */
    private CommonGoal(int goalIndex) {
        this.goalIndex = goalIndex;
        
        switch( goalIndex ) {
            case 0 -> this.strategy = new SixGroupTwoTileGoal();
            case 1 -> this.strategy = new FourGroupFourTileGoal();
            case 2 -> this.strategy = new FourCornersGoal();
            case 3 -> this.strategy = new TwoGroupSquareGoal();
            case 4 -> this.strategy = new ThreeColumnSixTileGoal();
            case 5 -> this.strategy = new EightUniqueGoal();
            case 6 -> this.strategy = new DiagonalFiveTileGoal();
            case 7 -> this.strategy = new FourRowFiveTileGoal();
            case 8 -> this.strategy = new TwoColumnDistinctGoal();
            case 9 -> this.strategy = new TwoRowDistinctGoal();
            case 10 -> this.strategy = new CrossGoal();
            case 11 -> this.strategy = new DecreasingColumnsGoal();
            default -> throw new IndexOutOfBoundsException("Goal index must be between 0 and 11, got:" + goalIndex);
        }
    }
    
    /**
     * Get instance of common goal with given id
     *
     * @param goalIndex Unique integer id of goal to get
     *
     * @return Goal with given id
     */
    public static CommonGoal getCommonGoal(int goalIndex) {
        return goalList[goalIndex];
    }
    
    /**
     * Get the unique id of the goal
     *
     * @return Unique integer id of goal
     */
    public int getGoalIndex() {
        return this.goalIndex;
    }
    
    /**
     * Get a short description of the goal
     *
     * @return String description of goal
     */
    public String getDescription() {
        return this.strategy.getDescription();
    }
    
    /**
     * Check whether the goal has been completed on the given shelf
     *
     * @param shelf Shelf to check for the goal
     *
     * @return True if the shelf satisfies the goal, else false
     */
    public boolean checkGoal(Shelf shelf) {
        return this.strategy.checkShelf(shelf);
    }
}
