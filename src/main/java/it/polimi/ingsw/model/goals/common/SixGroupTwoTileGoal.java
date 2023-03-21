package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.function.BiPredicate;

public class SixGroupTwoTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Six groups, each containing at least 2 tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        
        var mat = shelf.getAllShelf();
        var check_matrix = new boolean[Shelf.N_ROW][Shelf.N_COL];
        BiPredicate<Integer, Integer> is_valid = (r, c) -> {
            return mat[r][c] != Tile.NOTILE && !check_matrix[r][c] &&
                   (r < Shelf.N_ROW && r > 0 && c > 0 && c < Shelf.N_COL);
        };
        
        int count = 0;
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                if( !check_matrix[i][j] ) {
                    var found = false;
                    if( is_valid.test(i + 1, j) && mat[i][j] == mat[i + 1][j] ) {
                        check_matrix[i][j] = true;
                        check_matrix[i + 1][j] = true;
                        count++;
                        found = true;
                    }
                    if( !found && is_valid.test(i, j + 1) && mat[i][j] == mat[i][j + 1] ) {
                        check_matrix[i][j] = true;
                        check_matrix[i + 1][j] = true;
                        count++;
                        found = true;
                    }
                    if( !found && is_valid.test(i - 1, j) && mat[i][j] == mat[i - 1][j] ) {
                        check_matrix[i][j] = true;
                        check_matrix[i - 1][j] = true;
                        count++;
                        found = true;
                    }
                    if( !found && is_valid.test(i, j - 1) && mat[i][j] == mat[i][j - 1] ) {
                        check_matrix[i][j] = true;
                        check_matrix[i][j - 1] = true;
                        count++;
                    }
                    
                }
            }
        }
        return count >= 6;
    }
    
}
