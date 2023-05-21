package it.polimi.ingsw.view.messages;

public class ChatMessage extends ViewMessage<String> {
    
    private final String dest;
    
    public ChatMessage(String s, String playerNick) {
        super(s, playerNick);
        dest = "BROADCAST";
    }
    
    public ChatMessage(String s, String PlayerNick, String playerDestination) {
        super(s, PlayerNick);
        dest = playerDestination;
    }
    
    public String getDestination() {
        return this.dest;
    }
    
}
