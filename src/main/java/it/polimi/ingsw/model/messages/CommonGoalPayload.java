package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.GameModel;

import java.io.Serializable;

public record CommonGoalPayload(GameModel.CGType type, int availableTopScore) implements Serializable {
}
