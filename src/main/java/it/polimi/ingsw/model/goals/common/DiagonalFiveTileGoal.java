package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class DiagonalFiveTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Five tiles of the same type forming a diagonal.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return (shelf.getTile(5, 0) != Tile.NOTILE
                && shelf.getTile(5, 0).type() == shelf.getTile(4, 1).type()
                && shelf.getTile(4, 1).type() == shelf.getTile(3, 2).type()
                && shelf.getTile(3, 2).type() == shelf.getTile(2, 3).type()
                && shelf.getTile(2, 3).type() == shelf.getTile(1, 4).type())
               || (shelf.getTile(4, 0) != Tile.NOTILE
                   && shelf.getTile(4, 0).type() == shelf.getTile(3, 1).type()
                   && shelf.getTile(3, 1).type() == shelf.getTile(2, 2).type()
                   && shelf.getTile(2, 2).type() == shelf.getTile(1, 3).type()
                   && shelf.getTile(1, 3).type() == shelf.getTile(0, 4).type())
               || (shelf.getTile(5, 4) != Tile.NOTILE
                   && shelf.getTile(5, 4).type() == shelf.getTile(4, 3).type()
                   && shelf.getTile(4, 3).type() == shelf.getTile(3, 2).type()
                   && shelf.getTile(3, 2).type() == shelf.getTile(2, 1).type()
                   && shelf.getTile(2, 1).type() == shelf.getTile(1, 0).type())
               || (shelf.getTile(4, 4) != Tile.NOTILE
                   && shelf.getTile(4, 4).type() == shelf.getTile(3, 3).type()
                   && shelf.getTile(3, 3).type() == shelf.getTile(2, 2).type()
                   && shelf.getTile(2, 2).type() == shelf.getTile(1, 1).type()
                   && shelf.getTile(0, 0).type() == shelf.getTile(0, 0).type());
    }
    
}
