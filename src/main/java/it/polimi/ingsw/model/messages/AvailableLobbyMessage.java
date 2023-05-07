package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.LobbyController;

import java.util.List;

public class AvailableLobbyMessage extends ModelMessage<AvailableLobbyPayload> {
    public AvailableLobbyMessage(List<LobbyController.LobbyView> p) {
        super(new AvailableLobbyPayload(p));
    }
}
