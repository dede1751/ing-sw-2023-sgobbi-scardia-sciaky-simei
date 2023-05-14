package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Shelf;

import java.util.List;

public class StartGameMessage extends ModelMessage<StartGamePayload> {
    
    public StartGameMessage(
            List<String> p, Integer personalGoalId, List<Shelf> shelves, Board board,
            int CGXIndex, int topCGXScore, int CGYIndex, int topCGYScore) {
        super(new StartGamePayload(p, personalGoalId, shelves, board, CGXIndex, topCGXScore, CGYIndex, topCGYScore));
    }
}
