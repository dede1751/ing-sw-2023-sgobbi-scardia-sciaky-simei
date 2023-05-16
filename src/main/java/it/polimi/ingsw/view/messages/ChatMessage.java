package it.polimi.ingsw.view.messages;

/**
 * Chat Message
 */
public class ChatMessage extends ViewMessage<String> {
    
    private final String dest;
    
    /**
     * Initialize a broadcast ChatMessage object
     * @param s The message string
     * @param playerNick The sender's nickname
     * @param clientId The sender's client id
     */
    public ChatMessage(String s, String playerNick, int clientId) {
        super(s, playerNick, clientId);
        dest = "BROADCAST";
    }
    
    /**
     * Initialize a ChatMessage to be sent to a single player
     * @param s The message string
     * @param PlayerNick The sender's nickname
     * @param playerDestination The receiving player's nickname
     * @param clientId the sender's client id
     */
    public ChatMessage(String s, String PlayerNick, String playerDestination, int clientId) {
        super(s, PlayerNick, clientId);
        dest = playerDestination;
    }
    
    /**
     * Get the nickname of the receiving player
     * @return The nickname of the receiving player as a string
     */
    public String getDestination() {
        return this.dest;
    }
}
