package it.polimi.ingsw.view.messages;

public class RequestLobby extends ViewMsg<LobbyInformation>{
    
    public RequestLobby(LobbyInformation lobbyInformation, String playerNick, int clientId) {
        super(lobbyInformation, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
}
