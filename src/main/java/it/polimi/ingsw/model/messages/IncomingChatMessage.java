package it.polimi.ingsw.model.messages;

/**
 * Message containing a chat message. <br>
 * Chat messages can either be broadcast with destination "all" or sent to a specific player.
 */
public class IncomingChatMessage extends ModelMessage<String> {
    
    private final String sender;
    private final String destination;
    
    /**
     * Initialize a new IncomingChatMessage object with the given sender and destination.
     *
     * @param p           The message
     * @param sender      The sender
     * @param destination The destination (either a player's nickname or "all")
     */
    public IncomingChatMessage(String p, String sender, String destination) {
        super(p);
        this.sender = sender;
        this.destination = destination;
    }
    
    /**
     * Get the sender of the message
     *
     * @return The sender
     */
    public String getSender() {
        return sender;
    }
    
    /**
     * Get the destination of the message
     *
     * @return The nickname of the recipient or "all"
     */
    public String getDestination() {
        return destination;
    }
    
}
