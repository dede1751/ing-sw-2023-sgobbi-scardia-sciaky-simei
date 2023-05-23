package it.polimi.ingsw.model.messages;

public class IncomingChatMessage extends ModelMessage<String> {
    
    private final String sender;
    private final String destination;
    
    public IncomingChatMessage(String p, String sender, String destination) {
        super(p);
        this.sender = sender;
        this.destination = destination;
    }
    
    public String getSender() {
        return sender;
    }
    
    public String getDestination() { return destination; }
    
}
