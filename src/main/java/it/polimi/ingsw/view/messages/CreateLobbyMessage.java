package it.polimi.ingsw.view.messages;

public class CreateLobbyMessage extends ViewMessage<Integer> {
    public CreateLobbyMessage(Integer i, String playerNick, int clientId) {
        super(i, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
    
}
