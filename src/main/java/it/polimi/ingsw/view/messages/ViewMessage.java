package it.polimi.ingsw.view.messages;

import java.io.Serializable;

/**
 * MVC view message parent class. <br>
 * Every message that originate from the view must inherit from this class. <br>
 * Every Message class should define its own payload class. <br>
 * For details consult the network manual.
 * @param <Payload> Generic Payload parameter, must be Serializable.
 */
public abstract class ViewMessage<Payload extends Serializable> implements Serializable {
    
    private final Payload payload;
    private final String nickname;
    
    /**
     * Initialize message from the given player with the given payload.
     * @param payload payload of the message
     * @param playerNick nickname of the player that sent the message
     */
    public ViewMessage(Payload payload, String playerNick) {
        this.payload = payload;
        this.nickname = playerNick;
    }
    
    /**
     * Get the message's payload.
     * @return the message's payload.
     */
    public Payload getPayload() {
        return this.payload;
    }
    
    /**
     * Get the nickname of the player that sent the message.
     * @return the nickname of the player that sent the message.
     */
    public String getPlayerNickname() {
        return this.nickname;
    }
    
}
