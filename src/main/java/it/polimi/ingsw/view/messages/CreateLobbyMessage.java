package it.polimi.ingsw.view.messages;

public class CreateLobbyMessage extends ViewMessage<Integer> {
    
    public CreateLobbyMessage(Integer i, String playerNick) {
        super(i, playerNick);
    }
    
}
