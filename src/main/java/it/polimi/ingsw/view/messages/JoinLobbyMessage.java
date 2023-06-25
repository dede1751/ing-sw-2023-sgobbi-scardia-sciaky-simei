package it.polimi.ingsw.view.messages;

public class JoinLobbyMessage extends ViewMessage<Integer> {
    
    public JoinLobbyMessage(Integer lobbyId, String playerNick) {
        super(lobbyId, playerNick);
    }
    
}
