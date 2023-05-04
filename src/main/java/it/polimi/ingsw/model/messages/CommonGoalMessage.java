package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.GameModel;

public class CommonGoalMessage extends ModelMessage<CommonGoalPayload> {
    
    public CommonGoalMessage(GameModel.CGType type, Integer topScoreAvailable) {
        super(new CommonGoalPayload(type, topScoreAvailable));
    }
}
