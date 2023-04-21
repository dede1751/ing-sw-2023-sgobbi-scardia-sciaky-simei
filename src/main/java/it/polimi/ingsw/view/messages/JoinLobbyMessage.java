package it.polimi.ingsw.view.messages;

public class JoinLobbyMessage extends ViewMsg<JoinLobby>{
    
    
    public JoinLobbyMessage(JoinLobby joinLobby, String playerNick, int clientId) {
        super(joinLobby, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
}
