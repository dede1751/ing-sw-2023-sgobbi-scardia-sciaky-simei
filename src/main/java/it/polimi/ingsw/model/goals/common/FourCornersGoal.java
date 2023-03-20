package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class FourCornersGoal implements CommonGoalStrategy {
    
    
    public String getDescription() {
        return "Four tiles of the same type in the four corners of the shelf.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        return (shelf.getTile(0, 0) != Tile.NOTILE) &&
               (shelf.getTile(0, 0) == (shelf.getTile(0, 4))) &&
               (shelf.getTile(0, 4) == (shelf.getTile(5, 0))) &&
               (shelf.getTile(5, 0) == (shelf.getTile(5, 4)));
       }
    }
