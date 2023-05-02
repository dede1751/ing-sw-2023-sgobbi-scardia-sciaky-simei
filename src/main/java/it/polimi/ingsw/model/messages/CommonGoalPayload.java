package it.polimi.ingsw.model.messages;

import java.io.Serializable;

public record CommonGoalPayload(Type type, int availableTopScore) implements Serializable {
    public enum Type{
        X, Y
    }
}
