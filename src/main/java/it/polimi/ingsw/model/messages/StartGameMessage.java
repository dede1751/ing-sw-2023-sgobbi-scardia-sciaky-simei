package it.polimi.ingsw.model.messages;

import java.util.List;

public class StartGameMessage extends ModelMessage<StartGamePayload> {
    
    public StartGameMessage(List<String> p, Integer personalGoalId, Integer XCGnumber, Integer YCGnumber) {
        super(new StartGamePayload(p, personalGoalId, XCGnumber, YCGnumber));
    }
}
