package it.polimi.ingsw.model.messages;

public class CommonGoalMessage extends ModelMessage<CommonGoalPayload>{
    
    public CommonGoalMessage(CommonGoalPayload.Type type, Integer topScoreAvailable) {
        super(new CommonGoalPayload(type, topScoreAvailable));
    }
}
