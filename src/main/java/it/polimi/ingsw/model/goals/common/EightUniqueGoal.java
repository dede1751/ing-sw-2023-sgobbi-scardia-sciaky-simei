package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class EightUniqueGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Eight tiles of the same type, in any position.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        int[] temp = new int[] {0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (shelf.getTile(i, j) == Tile.CATS) temp[0]++;
                if (shelf.getTile(i, j) == Tile.BOOKS) temp[1]++;
                if (shelf.getTile(i, j) == Tile.GAMES) temp[2]++;
                if (shelf.getTile(i, j) == Tile.FRAMES) temp[3]++;
                if (shelf.getTile(i, j) == Tile.TROPHIES) temp[4]++;
                if (shelf.getTile(i, j) == Tile.PLANTS) temp[5]++;
            }
        }
        return (temp[0] >= 8) ||
               (temp[1] >= 8) ||
               (temp[2] >= 8) ||
               (temp[3] >= 8) ||
               (temp[4] >= 8) ||
               (temp[5] >= 8);
    }
}
