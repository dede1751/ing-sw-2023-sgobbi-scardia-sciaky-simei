package it.polimi.ingsw.model.messages;

import java.io.Serializable;

abstract public class ModelMessage<Payload extends ModelComponent> implements Serializable {
    
    final private Payload payload;
    Payload getPayload(){
        return payload;
    }
    ModelMessage(Payload p){
        this.payload = p;
    }
}
