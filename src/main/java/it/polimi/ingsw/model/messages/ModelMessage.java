package it.polimi.ingsw.model.messages;

import java.io.Serializable;

abstract public class ModelMessage<Payload> implements Serializable {
    
    final private Payload payload;
    public Payload getPayload(){
        return payload;
    }
    public ModelMessage(Payload p){
        this.payload = p;
    }
}
