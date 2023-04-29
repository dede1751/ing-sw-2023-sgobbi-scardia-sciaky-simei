package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.LobbyController;

import java.util.List;

public class AvailableLobbyMessage extends ModelMessage<LobbyPayload>{
    public AvailableLobbyMessage(List<LobbyController.LobbyView> p) {
        super(new LobbyPayload(p));
    }
}
