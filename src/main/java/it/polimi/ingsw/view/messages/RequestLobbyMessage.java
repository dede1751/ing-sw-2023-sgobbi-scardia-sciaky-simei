package it.polimi.ingsw.view.messages;

public class RequestLobbyMessage extends ViewMessage<Integer> {
    
    public RequestLobbyMessage(Integer lobbyId, String playerNick) {
        super(lobbyId, playerNick);
    }
    
}
