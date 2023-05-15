package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.LobbyController;

import java.io.Serializable;
import java.util.List;

public class AvailableLobbyMessage extends ModelMessage<AvailableLobbyMessage.AvailableLobbyPayload> {
    
    public record AvailableLobbyPayload(List<LobbyController.LobbyView> lobbyViewList) implements Serializable {
    }
    
    public AvailableLobbyMessage(List<LobbyController.LobbyView> p) {
        super(new AvailableLobbyPayload(p));
    }
    
}
