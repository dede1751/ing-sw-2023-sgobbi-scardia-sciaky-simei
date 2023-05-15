package it.polimi.ingsw.model.messages;

import java.io.Serializable;

public class UpdateScoreMessage extends ModelMessage<UpdateScoreMessage.UpdateScorePayload> {
    
    public enum Type {
        CommonGoal, PersonalGoal, Adjacency, Bonus
    }
    
    public record UpdateScorePayload(Type type, int score, String player) implements Serializable {
    }
    
    public UpdateScoreMessage(Integer score, Type type, String nickname) {
        super(new UpdateScorePayload(type, score, nickname));
    }
    
}
