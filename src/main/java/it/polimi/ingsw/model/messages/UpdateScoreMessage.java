package it.polimi.ingsw.model.messages;

public class UpdateScoreMessage extends ModelMessage<UpdateScorePayload> {
    
    public UpdateScoreMessage(Integer score, UpdateScorePayload.Type type, String nickname) {
        super(new UpdateScorePayload(type, score,  nickname));
    }
}
