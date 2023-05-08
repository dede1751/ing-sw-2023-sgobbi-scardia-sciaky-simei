package it.polimi.ingsw.view.messages;

import java.io.Serializable;

public abstract class ViewMessage<Payload extends Serializable> implements Serializable {
    
    private final Payload payload;
    private final String nickname;
    
    private final int clientId;
    
    public ViewMessage(Payload payload, String playerNick, int clientId) {
        this.payload = payload;
        this.nickname = playerNick;
        this.clientId = clientId;
    }
    
    public Payload getPayload() {
        return this.payload;
    }
    
    public String getPlayerNickname() {
        return this.nickname;
    }
    
    public int getClientId() {
        return this.clientId;
    }
    
}
