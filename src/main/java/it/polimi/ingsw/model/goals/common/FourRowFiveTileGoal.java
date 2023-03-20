package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class FourRowFiveTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Four rows, each formed by 5 tiles of maximum 3 different types.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            int[] temp = new int[] {0, 0, 0, 0, 0, 0};
            int notpresent = 0;
            for (int j = 0; j < 5; j++) {
                if (shelf.getTile(i, j) == Tile.CATS) temp[0]++;
                if (shelf.getTile(i, j) == Tile.BOOKS) temp[1]++;
                if (shelf.getTile(i, j) == Tile.GAMES) temp[2]++;
                if (shelf.getTile(i, j) == Tile.FRAMES) temp[3]++;
                if (shelf.getTile(i, j) == Tile.TROPHIES) temp[4]++;
                if (shelf.getTile(i, j) == Tile.PLANTS) temp[5]++;
            }
            for (int j = 0; j < 6; j++) {
                if (temp[j] == 0) notpresent++;
            }
            if (notpresent >= 3) counter++;
        }
        return counter >= 4;
    }
}
