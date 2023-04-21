package it.polimi.ingsw.view.messages;

import java.lang.reflect.Type;

public class MoveMessage extends ViewMsg<Move> {
    
    public MoveMessage(Move move, String playerNick, int clientId) {
        super(move, playerNick, clientId);
    }
    
    @Override
    public Class<?> getMessageType() {
        return this.getClass();
    }
}
