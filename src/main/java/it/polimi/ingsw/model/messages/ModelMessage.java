package it.polimi.ingsw.model.messages;

import java.io.Serializable;


/**
 * MVC model messages parent class. <br>
 * Every message that originate from the model inherith from this class. <br>
 * Every Message class should define its own payload class. <br>
 * For details consult the network manual. <br>
 * @param <Payload> Generic Payload parameter
 */
abstract public class ModelMessage<Payload extends Serializable> implements Serializable {
    
    final private Payload payload;
    
    /**
     * @return the message's payload.
     */
    public Payload getPayload() {
        return payload;
    }
    
    /**
     * Initialize message with payload p of type Payload.
     * @param payload Payload object
     */
    public ModelMessage(Payload payload) {
        this.payload = payload;
    }
    
}
