package it.polimi.ingsw.view.messages;

import java.io.Serializable;

public abstract class ViewMessage<Payload extends Serializable> implements Serializable {
    
    private final Payload payload;
    private final String nickname;
    
    public ViewMessage(Payload payload, String playerNick) {
        this.payload = payload;
        this.nickname = playerNick;
    }
    
    public Payload getPayload() {
        return this.payload;
    }
    
    public String getPlayerNickname() {
        return this.nickname;
    }
    
}
