package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.view.tui.TUIUtils;

import java.io.Serializable;
import java.util.List;
import java.util.function.BinaryOperator;

public class AvailableLobbyMessage extends ModelMessage<AvailableLobbyMessage.AvailableLobbyPayload> {
    
    public record AvailableLobbyPayload(List<LobbyController.LobbyView> lobbyViewList) implements Serializable {
    
        @Override
        public String toString(){
            BinaryOperator<String> concatenate = (x, y) ->
                TUIUtils.concatString(x, y, 4);
            return lobbyViewList.stream().map(LobbyController.LobbyView::toString).reduce("", concatenate);
        }
    
    }
    
    public AvailableLobbyMessage(List<LobbyController.LobbyView> p) {
        super(new AvailableLobbyPayload(p));
    }
    
}
