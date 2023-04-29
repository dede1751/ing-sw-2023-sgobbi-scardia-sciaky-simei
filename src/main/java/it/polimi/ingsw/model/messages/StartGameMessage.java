package it.polimi.ingsw.model.messages;

import java.util.List;

public class StartGameMessage extends ModelMessage<List<String>>{
    
    public StartGameMessage(List<String> p) {
        super(p);
    }
}
