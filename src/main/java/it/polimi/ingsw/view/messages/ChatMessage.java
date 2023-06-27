package it.polimi.ingsw.view.messages;

/**
 * Message containing a chat message. It can be sent to a single player or to all players.
 */
public class ChatMessage extends ViewMessage<String> {
    
    private final String dest;
    
    /**
     * Initialize a broadcast ChatMessage object
     *
     * @param s          The message string
     * @param playerNick The sender's nickname
     */
    public ChatMessage(String s, String playerNick) {
        super(s, playerNick);
        dest = "ALL";
    }
    
    /**
     * Initialize a ChatMessage to be sent to a single player
     *
     * @param s                 The message string
     * @param PlayerNick        The sender's nickname
     * @param playerDestination The receiving player's nickname
     */
    public ChatMessage(String s, String PlayerNick, String playerDestination) {
        super(s, PlayerNick);
        dest = playerDestination;
    }
    
    /**
     * Get the nickname of the receiving player
     *
     * @return The nickname of the receiving player as a string
     */
    public String getDestination() {
        return this.dest;
    }
}
