package it.polimi.ingsw.view.messages;

/**
 * Message pinging the server for debug purposes.
 */
public class DebugMessage extends ViewMessage<String> {
    
    /**
     * Initialize message with the given extra content and player nickname.
     * @param s message contents
     * @param playerNick nickname of the player that sent the message
     */
    public DebugMessage(String s, String playerNick) {
        super(s, playerNick);
    }
    
}
