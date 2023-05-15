package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.GameModel;

import java.io.Serializable;

public class CommonGoalMessage extends ModelMessage<CommonGoalMessage.CommonGoalPayload> {
    
    public record CommonGoalPayload(GameModel.CGType type, int availableTopScore) implements Serializable {
    }
    
    public CommonGoalMessage(GameModel.CGType type, Integer topScoreAvailable) {
        super(new CommonGoalPayload(type, topScoreAvailable));
    }
    
}
