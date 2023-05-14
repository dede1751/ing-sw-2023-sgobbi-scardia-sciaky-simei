package it.polimi.ingsw.model.messages;

public class IncomingChatMessage extends ModelMessage<String> {
    
    private final String sender;
    
    public IncomingChatMessage(String p, String sender) {
        super(p);
        this.sender = sender;
    }
    
    public String getSender() {
        return sender;
    }
    
}
