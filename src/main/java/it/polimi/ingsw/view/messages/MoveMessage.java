package it.polimi.ingsw.view.messages;

public class MoveMessage extends ViewMessage<Move> {
    
    public MoveMessage(Move move, String playerNick) {
        super(move, playerNick);
    }
    
}
