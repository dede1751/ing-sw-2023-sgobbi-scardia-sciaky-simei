package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.utils.files.ResourcesManager;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Model's internal Player representation.
 */
public class Player implements Serializable {
    
    static final long serialVersionUID = 1L;
    
    final private String nickname;
    
    private final int score;
    
    private int commonGoalScore;
    
    private int personalGoalScore;
    
    private int adjacencyScore;
    
    private int bonusScore = 0;//is the 1 point given to the first who finishes the shelf
    
    final private int pgID;
    
    final private Shelf shelf;
    
    private boolean completedGoalX;
    private boolean completedGoalY;
    
    /**
     * Initialize player with given nickname and personal goal. <br>
     * Used to initialize players within fresh games. <br>
     * Nickname uniqueness within game must be externally checked
     *
     * @param nickname Player's nickname
     * @param pgID     Integer id of the player's personal goal (0-11)
     */
    protected Player(String nickname, int pgID) {
        this(nickname, pgID, 0, new Shelf(), 0);
    }
    
    /**
     * Initialize player from given state. <br>
     * Used to initialize players when recovering games.
     *
     * @param nickname Player's nickname
     * @param pgID     Integer id of the player's personal goal (0-11)
     * @param score    Player's total score
     * @param shelf    Player's shelf
     * @param commonGoalScore Player's common goal score
     */
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
     * Get the player's current total score.
     *
     * @return Integer player score
     */
    public int getScore() {
        return this.commonGoalScore + this.personalGoalScore + this.adjacencyScore + this.bonusScore;
    }
    
    /**
     * Get the player's personal goal id.
     *
     * @return Integer id for the player's personal goal
     */
    public int getPg() {
        return pgID;
    }
    
    /**
     * Get the player's shelf.
     *
     * @return Reference to player's shelf
     */
    public Shelf getShelf() {
        return shelf;
    }
    
    /**
     * Add given score to the player's common goal score.
     *
     * @param score Integer score to add to common goal score
     * @return Updated integer score
     */
    public int addCommonGoalScore(int score) {
        this.commonGoalScore += score;
        return this.commonGoalScore;
    }
    
    /**
     * Get the player's common goal score.
     * @return Integer common goal score
     */
    public int getCommonGoalScore() {
        return this.commonGoalScore;
    }
    
    /**
     * Set the player's personal goal score.
     * @param score Integer personal goal score
     */
    public void setPersonalGoalScore(int score) {
        this.personalGoalScore = score;
    }
    
    /**
     * Get the player's personal goal score.
     * @return Integer personal goal score
     */
    public int getPersonalGoalScore() {
        return this.personalGoalScore;
    }
    
    /**
     * Get the player's adjacency score.
     * @return Integer adjacency score
     */
    public int getAdjacencyScore() {
        return adjacencyScore;
    }
    
    /**
     * Set the player's adjacency score.
     * @param score Integer adjacency score
     */
    public void setAdjacencyScore(int score) {
        this.adjacencyScore = score;
    }
    
    /**
     * Check if the player has completed the common goal X.
     * @return true if the player has completed the common goal X, false otherwise
     */
    public boolean isCompletedGoalX() {
        return completedGoalX;
    }
    
    /**
     * Set the player's completion status of the common goal X.
     * @param completedGoalX true if the player has completed the common goal X, false otherwise
     */
    public void setCompletedGoalX(boolean completedGoalX) {
        this.completedGoalX = completedGoalX;
    }
    
    /**
     * Check if the player has completed the common goal Y.
     * @return true if the player has completed the common goal Y, false otherwise
     */
    public boolean isCompletedGoalY() {
        return completedGoalY;
    }
    
    /**
     * Set the player's completion status of the common goal Y.
     * @param completedGoalY true if the player has completed the common goal Y, false otherwise
     */
    public void setCompletedGoalY(boolean completedGoalY) {
        this.completedGoalY = completedGoalY;
    }
    
    /**
     * Get the player's bonus score.
     * @return Integer bonus score
     */
    public int getBonusScore() {
        return bonusScore;
    }
    
    /**
     * Set the player's bonus score.
     * @param bonusScore Integer bonus score
     */
    public void setBonusScore(int bonusScore) {
        this.bonusScore = bonusScore;
    }
    
    /**
     * Player's custom gson serializer
     */
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
    
    /**
     * Player's custom gson deserializer
     */
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
