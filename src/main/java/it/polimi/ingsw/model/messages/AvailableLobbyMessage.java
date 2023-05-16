package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.LobbyController;

import java.io.Serializable;
import java.util.List;


/**
 * Available Lobby Message <br>
 * Refer to network manual for details
 */
public class AvailableLobbyMessage extends ModelMessage<AvailableLobbyMessage.AvailableLobbyPayload> {
    /**
     * Specific payload of the AvailableLobbyMessage
     * @param lobbyViewList LobbyView list object wrapped inside the record
     */
    public record AvailableLobbyPayload(List<LobbyController.LobbyView> lobbyViewList) implements Serializable {
    }
    /**
     * Create an AvailableLobbyMessage object from a LobbyView
     * @param p LobbyView object
     */
    public AvailableLobbyMessage(List<LobbyController.LobbyView> p) {
        super(new AvailableLobbyPayload(p));
    }
}
