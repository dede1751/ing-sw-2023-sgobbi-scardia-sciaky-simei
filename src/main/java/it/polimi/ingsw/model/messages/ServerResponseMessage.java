package it.polimi.ingsw.model.messages;

/**
 * Message containing a Response object.
 */
public class ServerResponseMessage extends ModelMessage<Response> {
    
    /**
     * Initialize a new ServerResponseMessage object with the given Response object.
     *
     * @param response The Response object.
     */
    public ServerResponseMessage(Response response) {
        super(response);
    }
    
}
