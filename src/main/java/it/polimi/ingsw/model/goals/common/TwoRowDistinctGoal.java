package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class TwoRowDistinctGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Two rows each formed by 5 distinct types of tiles.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            int[] temp = new int[] {1, 1, 1, 1, 1, 1};
            int sum = 0;
            for (int j = 0; j < 5; j++) {
                if (shelf.getTile(i, j) == Tile.CATS) {
                    if (temp[0] == 1) temp[0]--;
                }
                if (shelf.getTile(i, j) == Tile.BOOKS) {
                    if (temp[1] == 1) temp[1]--;
                }
                if (shelf.getTile(i, j) == Tile.GAMES) {
                    if (temp[2] == 1) temp[2]--;
                }
                if (shelf.getTile(i, j) == Tile.FRAMES) {
                    if (temp[3] == 1) temp[3]--;
                }
                if (shelf.getTile(i, j) == Tile.TROPHIES) {
                    if (temp[4] == 1) temp[4]--;
                }
                if (shelf.getTile(i, j) == Tile.PLANTS) {
                    if (temp[5] == 1) temp[5]--;
                }
            }
            for (int j = 0; j < 6; j++) {
                sum = sum + temp[j];
            }
            if (sum == 1) counter++;
        }
        return counter >= 2;
    }
}
