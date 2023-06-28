package it.polimi.ingsw.view.messages;

public class RequestLobbyMessage extends ViewMessage<Integer> {
    
    public RequestLobbyMessage(Integer size, String playerNick) {
        super(size, playerNick);
    }
    
}
