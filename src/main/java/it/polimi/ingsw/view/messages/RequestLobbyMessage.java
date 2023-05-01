package it.polimi.ingsw.view.messages;

public class RequestLobbyMessage extends ViewMessage<Integer> {
    
    public RequestLobbyMessage(Integer lobbyId, String playerNick, int clientId) {
        super(lobbyId, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
    
}
