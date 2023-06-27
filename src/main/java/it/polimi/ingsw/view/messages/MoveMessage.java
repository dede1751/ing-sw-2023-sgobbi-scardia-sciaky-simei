package it.polimi.ingsw.view.messages;

/**
 * Message containing a player move.
 */
public class MoveMessage extends ViewMessage<Move> {
    
    /**
     * Initialize message with the given move and player nickname.
     *
     * @param move       move
     * @param playerNick nickname of the player that sent the message
     */
    public MoveMessage(Move move, String playerNick) {
        super(move, playerNick);
    }
    
}
