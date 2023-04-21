package it.polimi.ingsw.view.messages;

public class CreateLobbyMessage extends ViewMsg<LobbyInformation> {
    public CreateLobbyMessage(LobbyInformation s, String playerNick, int clientId) {
        super(s, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
}
