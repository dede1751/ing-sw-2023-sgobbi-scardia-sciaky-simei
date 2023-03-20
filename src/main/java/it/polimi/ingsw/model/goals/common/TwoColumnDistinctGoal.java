package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.ArrayList;

public class TwoColumnDistinctGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Two columns each formed by 6 distinct types of tiles.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        int counter = 0;
        for (int j = 0; j < 5; j++) {
            int[] temp = new int[] {1, 1, 1, 1, 1, 1};
            int sum = 0;
            for (int i = 0; i < 6; i++) {
                if (shelf.getTile(i, j) == Tile.CATS) temp[0]--;
                if (shelf.getTile(i, j) == Tile.BOOKS) temp[1]--;
                if (shelf.getTile(i, j) == Tile.GAMES) temp[2]--;
                if (shelf.getTile(i, j) == Tile.FRAMES) temp[3]--;
                if (shelf.getTile(i, j) == Tile.TROPHIES) temp[4]--;
                if (shelf.getTile(i, j) == Tile.PLANTS) temp[5]--;
            }
            for (int i = 0; i < 6; i++) {
                sum = sum + temp[i];
            }
            if (sum == 0) counter++;
        }
        return counter >= 2;
    }
}
