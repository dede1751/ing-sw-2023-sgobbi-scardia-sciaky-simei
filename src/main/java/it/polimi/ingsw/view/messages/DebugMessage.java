package it.polimi.ingsw.view.messages;

public class DebugMessage extends ViewMessage<String> {
    
    public DebugMessage(String s, String playerNick, int clientId) {
        super(s, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
}
