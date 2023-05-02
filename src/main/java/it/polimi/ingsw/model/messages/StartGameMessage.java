package it.polimi.ingsw.model.messages;

import java.util.List;

public class StartGameMessage extends ModelMessage<StartGamePayload>{
    
    
    private final int personalGoalId;
    private final int XCGnumber;
    private final int YCGnumber;
    public StartGameMessage(List<String> p, Integer personalGoalId, Integer XCGnumber, Integer YCGnumber) {
        super(new StartGamePayload(p));
        this.personalGoalId = personalGoalId;
        this.XCGnumber=XCGnumber;
        this.YCGnumber=YCGnumber;
    }
    public int getPersonalGoalId(){
        return this.personalGoalId;
    }
    
    public int getYCGnumber() {
        return YCGnumber;
    }
    
    public int getXCGnumber() {
        return XCGnumber;
    }
}
