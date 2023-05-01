package it.polimi.ingsw.model.messages;

import java.util.List;

public class StartGameMessage extends ModelMessage<StartGamePayload>{
    
    public StartGameMessage(List<String> p) {
        super(new StartGamePayload(p));
    }
}
