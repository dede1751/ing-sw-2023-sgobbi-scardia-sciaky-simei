package it.polimi.ingsw.view.messages;

public class RecoverLobbyMessage extends ViewMessage<Integer> {
    
    public RecoverLobbyMessage(String playerNick, int clientId) { super(null, playerNick, clientId); }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
    
}
