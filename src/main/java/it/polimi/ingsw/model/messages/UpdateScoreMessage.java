package it.polimi.ingsw.model.messages;

import java.io.Serializable;

/**
 * Message notifying the clients of a score update
 */
public class UpdateScoreMessage extends ModelMessage<UpdateScoreMessage.UpdateScorePayload> {
    
    /**
     * Enum containing the possible types of score updates
     */
    public enum Type {
        /**
         * Common Goal score
         */
        CommonGoal,
        /**
         * Personal Goal score
         */
        PersonalGoal,
        /**
         * Adjacency score, based on the size of adjacent clusters of tiles of the same type in the shelf.
         */
        Adjacency,
        /**
         * Bonus score, point awarded to the player filling his shelf first.
         */
        Bonus
    }
    
    /**
     * Specific payload for UpdateScoreMessage. <br>
     * Contains the type of the score update, the new score and nickname of the player having its score updated.
     *
     * @param type   The type of the score update
     * @param score  The new score
     * @param player The nickname of the player having its score updated
     */
    public record UpdateScorePayload(Type type, int score, String player) implements Serializable {
        @Override
        public String toString() {
            return "Update Score : " + type.name() + " " + score + " " + player;
        }
    }
    
    /**
     * Initialize a UpdateScoreMessage object with the payload parameters
     *
     * @param score    The new score
     * @param type     The type of the score update
     * @param nickname The nickname of the player having its score updated
     */
    public UpdateScoreMessage(Integer score, Type type, String nickname) {
        super(new UpdateScorePayload(type, score, nickname));
    }
    
}
