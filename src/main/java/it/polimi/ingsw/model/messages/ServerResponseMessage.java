package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.view.messages.ViewMessage;

public class ServerResponseMessage extends ViewMessage<Response> {
    
    public ServerResponseMessage(Response response, String playerNick, int clientId) {
        super(response, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return null;
    }
}
