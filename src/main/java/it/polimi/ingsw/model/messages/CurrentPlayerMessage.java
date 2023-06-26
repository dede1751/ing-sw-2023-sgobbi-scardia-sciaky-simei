package it.polimi.ingsw.model.messages;

/**
 * Message containing the current player's nickname
 */
public class CurrentPlayerMessage extends ModelMessage<String> {
    
    /**
     * Initialize a new CurrentPlayerMessage object with the given nickname
     * @param p The nickname of the current player
     */
    public CurrentPlayerMessage(String p) {
        super(p);
    }
    
}
