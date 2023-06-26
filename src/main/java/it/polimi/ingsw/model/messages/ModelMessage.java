package it.polimi.ingsw.model.messages;

import java.io.Serializable;


/**
 * MVC model messages parent class. <br>
 * Every message that originate from the model must inherit from this class. <br>
 * Every Message class should define its own payload class. <br>
 * For details consult the network manual.
 * @param <Payload> Generic Payload parameter, must be Serializable.
 */
abstract public class ModelMessage<Payload extends Serializable> implements Serializable {
    
    final private Payload payload;
    
    /**
     * Get the message's payload.
     * @return the message's payload.
     */
    public Payload getPayload() {
        return payload;
    }
    
    /**
     * Initialize message with the given payload.
     * @param payload Payload object
     */
    public ModelMessage(Payload payload) {
        this.payload = payload;
    }
    
}
