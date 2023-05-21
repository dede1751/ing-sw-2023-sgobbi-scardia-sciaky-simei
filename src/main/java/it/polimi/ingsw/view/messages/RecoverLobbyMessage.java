package it.polimi.ingsw.view.messages;

import java.io.Serializable;

public class RecoverLobbyMessage extends ViewMessage<Serializable> {
    
    public RecoverLobbyMessage(String playerNick) {
        super(null, playerNick);
    }
    
}
