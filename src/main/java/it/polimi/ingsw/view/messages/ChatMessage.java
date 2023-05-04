package it.polimi.ingsw.view.messages;

public class ChatMessage extends ViewMessage<String> {
    
    private final String dest;
    
    public ChatMessage(String s, String playerNick, int clientId) {
        super(s, playerNick, clientId);
        dest = "BROADCAST";
    }
    
    public ChatMessage(String s, String PlayerNick, String playerDestination, int clientId) {
        super(s, PlayerNick, clientId);
        dest = playerDestination;
    }
    
    public String getDestination() {
        return this.dest;
    }
    
}
