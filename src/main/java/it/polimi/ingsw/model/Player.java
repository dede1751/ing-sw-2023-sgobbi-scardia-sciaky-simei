package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Player representation within model
 */
public class Player implements Serializable {
    
    static final long serialVersionUID = 1L;
    
    final private String nickname;
    
    private int score;
    
    final private int pgID;
    
    private final Shelf shelf;
    
    private boolean completedGoalX;
    private boolean CompletedGoalY;
    
    /**
     * Initialized player with given nickname and personal goal.
     * Nickname uniqueness within game must be externally checked
     *
     * @param nickname String of the player's nickname
     * @param pgID     Integer id of the player's personal goal (0-11)
     */
    protected Player(String nickname, int pgID) {
        this.nickname = nickname;
        this.pgID = pgID;
        this.score = 0;
        this.shelf = new Shelf();
    }
    
    /**
     * Get the player's nickname
     *
     * @return String of the player's nickname
     */
    public String getNickname() {
        return nickname;
    }
    
    /**
     * Get the player's current score
     *
     * @return Integer player score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Get the player's personal goal id
     *
     * @return Integer id for the player's personal goal
     */
    public int getPg() {
        return pgID;
    }
    
    /**
     * Get the player's shelf
     *
     * @return Reference to player's shelf
     */
    public Shelf getShelf() {
        return shelf;
    }
    
    /**
     * Add given score to the player's total
     *
     * @param score Integer score to add to running total
     *
     * @return Updated integer score
     */
    public int addScore(int score) {
        this.score += score;
        return this.score;
    }
    
    public boolean isCompletedGoalX() {
        return completedGoalX;
    }
    
    public void setCompletedGoalX(boolean completedGoalX) {
        this.completedGoalX = completedGoalX;
    }
    
    public boolean isCompletedGoalY() {
        return CompletedGoalY;
    }
    
    public void setCompletedGoalY(boolean completedGoalY) {
        CompletedGoalY = completedGoalY;
    }
}
