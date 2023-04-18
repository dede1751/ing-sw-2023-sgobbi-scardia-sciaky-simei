package it.polimi.ingsw.model;

import java.io.Serializable;

import com.google.gson.*;
import it.polimi.ingsw.utils.files.ResourcesManager;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Player representation within model
 */
public class Player implements Serializable {
    
    static final long serialVersionUID = 1L;
    
    final private String nickname;
    
    private final int score;
    
    private int commonGoalScore;
    
    private int personalGoalScore;
    
    private int adjacentScore;
    
    private int bonusScore = 0;//is the 1 point given to the first who finishes the shelf
    
    final private int pgID;
    
    final private Shelf shelf;
    
    private boolean completedGoalX;
    private boolean completedGoalY;
    
    /**
     * Initialized player with given nickname and personal goal.
     * Nickname uniqueness within game must be externally checked
     *
     * @param nickname String of the player's nickname
     * @param pgID     Integer id of the player's personal goal (0-11)
     */
    protected Player(String nickname, int pgID) {
        this(nickname, pgID, 0, new Shelf(), 0);
    }
    
    private Player(String nickname, int pgID, int score, Shelf shelf, int commonGoalScore) {
        this.nickname = nickname;
        this.pgID = pgID;
        this.score = score;
        this.shelf = shelf;
        this.commonGoalScore = commonGoalScore;
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
        return this.commonGoalScore + this.personalGoalScore + this.adjacentScore + this.bonusScore;
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
    public int addCommonGoalScore(int score) {
        this.commonGoalScore += score;
        return this.commonGoalScore;
    }
    public int getCommonGoalScore(){
        return this.commonGoalScore;
    }
    public int setPersonalGoalScore(int score) {
        this.personalGoalScore = score;
        
        return this.personalGoalScore;
    }
    
    public int getPersonalGoalScore() {
        return this.personalGoalScore;
    }
    
    public int setAdjacentScore(int score) {
        this.adjacentScore = score;
        return this.adjacentScore;
    }
    
    
    public boolean isCompletedGoalX() {
        return completedGoalX;
    }
    
    public void setCompletedGoalX(boolean completedGoalX) {
        this.completedGoalX = completedGoalX;
    }
    
    public boolean isCompletedGoalY() {
        return completedGoalY;
    }
    
    public void setCompletedGoalY(boolean completedGoalY) {
        this.completedGoalY = completedGoalY;
    }
    
    public int getBonusScore() {
        return bonusScore;
    }
    
    public void setBonusScore(int bonusScore) {
        this.bonusScore = bonusScore;
    }
    
    
    protected static class PlayerSerializer implements JsonSerializer<Player> {
        @Override
        public JsonElement serialize(Player player, Type typeOfSrc, JsonSerializationContext context) {
            
            var result = new JsonObject();
            result.addProperty("Nickname", player.nickname);
            result.addProperty("PersonalGoal", player.pgID);
            result.addProperty("Score", player.score);
            result.add("Shelf", ResourcesManager.JsonManager.getElementByAttribute(player.getShelf().toJson(),
                                                                                   "shelf").getAsJsonObject().get(
                    "shelf"));
            result.addProperty("CommonGoalScore", player.commonGoalScore);
            result.addProperty("CGXCompleted", player.completedGoalX);
            result.addProperty("CGYCompleted", player.completedGoalY);
            return result;
        }
    }
    
    protected static class PlayerDeserializer implements JsonDeserializer<Player> {
        @Override
        public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            
            var nickname = ResourcesManager.JsonManager.getElementByAttribute(json, "Nickname");
            var pgID = ResourcesManager.JsonManager.getElementByAttribute(json, "PersonalGoal");
            var score = ResourcesManager.JsonManager.getElementByAttribute(json, "Score");
            var shelf = Shelf.fromJson(ResourcesManager.JsonManager.getObjectByAttribute(json.toString(), "Shelf"));
            var commonGoalScore = ResourcesManager.JsonManager.getElementByAttribute(json, "CommonGoalScore");
            var GCXcompleted = ResourcesManager.JsonManager.getElementByAttribute(json, "CGXCompleted");
            var GCYcompleted = ResourcesManager.JsonManager.getElementByAttribute(json, "CGYCompleted");
            var result = new Player(nickname.getAsString(), pgID.getAsInt(), score.getAsInt(), shelf,
                                    commonGoalScore.getAsInt());
            result.setCompletedGoalX(GCXcompleted.getAsBoolean());
            result.setCompletedGoalY(GCYcompleted.getAsBoolean());
            return result;
        }
    }
}
