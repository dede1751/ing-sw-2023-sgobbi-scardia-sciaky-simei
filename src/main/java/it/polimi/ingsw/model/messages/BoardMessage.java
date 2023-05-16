package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.GameModel;

/**
 * Board Message <br>
 * Refer to network manula for details
 */
public class BoardMessage extends ModelMessage<Board> {
    /**
     * Initialize a new BoardMessage object from a GameModel object.
     * @param p The GameModel object.
     */
    public BoardMessage(GameModel p) {
        super(p.getBoard());
    }
    
}
