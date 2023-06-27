package it.polimi.ingsw.view.messages;

import java.io.Serializable;

/**
 * Message containing a request to join recovering lobby.
 */
public class RecoverLobbyMessage extends ViewMessage<Serializable> {
    
    /**
     * Initialize message with the given player nickname. <br>
     * The lobby is inferred from the nickname.
     *
     * @param playerNick nickname of the player that sent the message
     */
    public RecoverLobbyMessage(String playerNick) {
        super(null, playerNick);
    }
    
}
