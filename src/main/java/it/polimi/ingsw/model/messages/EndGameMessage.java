package it.polimi.ingsw.model.messages;

import java.io.Serializable;
import java.util.Map;

public class EndGameMessage extends ModelMessage<EndGameMessage.EndGamePayload> {
    
    public record EndGamePayload(String winner, Map<String, Integer> points) implements Serializable {}
    
    public EndGameMessage(String winner, Map<String, Integer> points) {
        super(new EndGamePayload(winner, points));
    }
    
}
