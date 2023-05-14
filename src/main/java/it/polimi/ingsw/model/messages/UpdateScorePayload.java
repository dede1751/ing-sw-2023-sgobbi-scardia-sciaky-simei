package it.polimi.ingsw.model.messages;

import java.io.Serializable;

public record UpdateScorePayload(Type type, int score, String player) implements Serializable {
    public enum Type {
        CommonGoal, PersonalGoal, Adjacency, Bonus
    }
    
}
