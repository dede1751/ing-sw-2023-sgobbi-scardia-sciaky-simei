package it.polimi.ingsw.model.messages;

public class UpdateScoreMessage extends ModelMessage<Integer> {
    
    public enum Type {
        CommonGoal, PersonalGoal, Adiajency, Bonus
    }
    
    private final Type type;
    
    public UpdateScoreMessage(Integer score, Type type) {
        super(score);
        this.type = type;
    }
    
    public Type getType() {
        return this.type;
    }
}
