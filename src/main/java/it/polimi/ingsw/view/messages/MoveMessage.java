package it.polimi.ingsw.view.messages;

public class MoveMessage extends ViewMessage<Move> {
    
    public MoveMessage(Move move, String playerNick, int clientId) {
        super(move, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
}
