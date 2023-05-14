package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Shelf;

import java.io.Serializable;
import java.util.List;

public record StartGamePayload(
        List<String> nicknames, Integer personalGoalId, List<Shelf> shelves, Board board,
        int CGXIndex, int topCGXScore, int CGYIndex, int topCGYScore) implements Serializable {
}
