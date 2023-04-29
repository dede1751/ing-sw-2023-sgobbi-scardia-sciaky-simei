package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.LobbyController;

import java.util.List;

public class AvailableLobbyMessage extends ModelMessage<List<LobbyController.LobbyView>> {
    public AvailableLobbyMessage(List<LobbyController.LobbyView> p) {
        super(p);
    }
}
