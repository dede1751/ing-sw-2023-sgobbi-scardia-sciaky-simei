package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.GameModel;

public class BoardMessage extends ModelMessage<Board> {
    public BoardMessage(GameModel p) {
        super(p.getBoard());
    }
    
}
