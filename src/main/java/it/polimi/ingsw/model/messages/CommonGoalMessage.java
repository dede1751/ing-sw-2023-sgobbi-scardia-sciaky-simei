package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.GameModel;

import java.io.Serializable;

/**
 * Common Goal Message
 */
public class CommonGoalMessage extends ModelMessage<CommonGoalMessage.CommonGoalPayload> {
    
    /**
     * Specific payload of the Common Goal Message. It is a wrapper around a CGType object and an int field.
     * @param type The concerned common goal disambiguated by the value of the enum (X - Y)
     * @param availableTopScore The score on the top the point's stack
     */
    public record CommonGoalPayload(GameModel.CGType type, int availableTopScore) implements Serializable {
    }
    
    /**
     * Initialize a CommonGoalMessage object with the payload parameters
     * @param type The type of the common goal
     * @param topScoreAvailable The top score
     */
    public CommonGoalMessage(GameModel.CGType type, Integer topScoreAvailable) {
        super(new CommonGoalPayload(type, topScoreAvailable));
    }
    
}
