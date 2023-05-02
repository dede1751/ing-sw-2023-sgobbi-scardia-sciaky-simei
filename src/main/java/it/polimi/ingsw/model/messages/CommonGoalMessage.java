package it.polimi.ingsw.model.messages;

public class CommonGoalMessage extends ModelMessage<Integer>{
    
    public enum Type{ X , Y }
    private Type type;
    private int topScoreAvailable;
    
    public CommonGoalMessage(Integer CommonGoalId, Type type, Integer topScoreAvailable) {
        super(CommonGoalId);
        
    }
}
