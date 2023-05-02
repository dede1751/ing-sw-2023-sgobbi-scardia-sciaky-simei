package it.polimi.ingsw.view.messages;

import java.io.Serializable;

public class RecoverLobbyMessage extends ViewMessage<Serializable> {
    
    public RecoverLobbyMessage(String playerNick, int clientId) { super(null, playerNick, clientId); }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
    
}
