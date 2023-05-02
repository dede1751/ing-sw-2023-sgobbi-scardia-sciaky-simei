package it.polimi.ingsw.view.messages;

public class JoinLobbyMessage extends ViewMessage<Integer> {
    
    public JoinLobbyMessage(Integer lobbyId, String playerNick, int clientId) {
        super(lobbyId, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
    
}
