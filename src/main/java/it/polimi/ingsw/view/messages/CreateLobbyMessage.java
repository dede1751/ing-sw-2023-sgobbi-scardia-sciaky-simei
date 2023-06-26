package it.polimi.ingsw.view.messages;

/**
 * Message containing a request to create a lobby.
 */
public class CreateLobbyMessage extends ViewMessage<Integer> {
    
    /**
     * Initialize message with the given lobby size and player nickname.
     * @param i size of the lobby to create
     * @param playerNick nickname of the player that sent the message
     */
    public CreateLobbyMessage(Integer i, String playerNick) {
        super(i, playerNick);
    }
    
}
