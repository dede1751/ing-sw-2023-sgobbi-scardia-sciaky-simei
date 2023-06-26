package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.GameModel;

import java.io.Serializable;

/**
 * Message with all information for a single common goal
 */
public class CommonGoalMessage extends ModelMessage<CommonGoalMessage.CommonGoalPayload> {
    
    /**
     * Specific payload of the Common Goal Message. <br>
     * Contains the type of the common goal (X-Y) and the score on the top of the stack.
     *
     * @param type The concerned common goal disambiguated by the value of the enum (X - Y)
     * @param availableTopScore The score on the top the point's stack
     */
    public record CommonGoalPayload(GameModel.CGType type, int availableTopScore) implements Serializable {
        @Override
        public String toString() {
            return type().name() + " , " + availableTopScore;
        }
    }
    
    /**
     * Initialize a CommonGoalMessage object with the payload parameters
     *
     * @param type The type of the common goal
     * @param topScoreAvailable The top score
     */
    public CommonGoalMessage(GameModel.CGType type, Integer topScoreAvailable) {
        super(new CommonGoalPayload(type, topScoreAvailable));
    }
    
}
