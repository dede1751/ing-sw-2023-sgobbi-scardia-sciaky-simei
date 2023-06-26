package it.polimi.ingsw.view.messages;

/**
 * Message containing a request to see the available lobbies.
 */
public class RequestLobbyMessage extends ViewMessage<Integer> {
    
    /**
     * Initialize message with the given lobby size and player nickname.
     * @param size required size of the lobbies, null if all lobbies are requested
     * @param playerNick nickname of the player that sent the message
     */
    public RequestLobbyMessage(Integer size, String playerNick) {
        super(size, playerNick);
    }
    
}
