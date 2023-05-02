package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.messages.ViewMessage;

public class ServerResponseMessage extends ModelMessage<Response> {
    
    public ServerResponseMessage(Response response) {
        super(response);
    }
}
