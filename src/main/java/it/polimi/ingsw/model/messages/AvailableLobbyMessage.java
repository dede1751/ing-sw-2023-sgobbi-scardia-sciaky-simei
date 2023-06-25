package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.view.tui.TUIUtils;

import java.io.Serializable;
import java.util.List;
import java.util.function.BinaryOperator;


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
        
        @Override
        public String toString() {
            BinaryOperator<String> concatenate = (x, y) ->
                    TUIUtils.concatString(x, y, 4);
            return lobbyViewList.stream().map(LobbyController.LobbyView::toString).reduce("", concatenate);
        }
        
    }
    /**
     * Create an AvailableLobbyMessage object from a LobbyView
     * @param p LobbyView object
     */
    public AvailableLobbyMessage(List<LobbyController.LobbyView> p) {
        super(new AvailableLobbyPayload(p));
    }
}
