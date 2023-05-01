package it.polimi.ingsw.view.messages;

public class RequestLobby extends ViewMessage<Integer> {
    
    public RequestLobby(Integer lobbyId, String playerNick, int clientId) {
        super(lobbyId, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
    
}
