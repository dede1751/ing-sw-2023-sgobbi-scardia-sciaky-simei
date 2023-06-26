package it.polimi.ingsw.view.messages;

/**
 * Message containing a request to join a lobby.
 */
public class JoinLobbyMessage extends ViewMessage<Integer> {
    
    /**
     * Initialize message with the given lobby id and player nickname.
     * @param lobbyId id of the lobby to join
     * @param playerNick nickname of the player that sent the message
     */
    public JoinLobbyMessage(Integer lobbyId, String playerNick) {
        super(lobbyId, playerNick);
    }
    
}
