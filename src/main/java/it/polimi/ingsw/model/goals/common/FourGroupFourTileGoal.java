package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.List;
import java.util.function.Predicate;

public class FourGroupFourTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Four groups, each containing at least 4 tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        
        record coor(int r, int c) {
            coor sum_off(coor offset) {
                return new coor(r + offset.r(), c + offset.c());
            }
            
            List<coor> sum_list(List<coor> offset) {
                return offset.stream().map((x) -> x.sum_off(this)).toList();
            }
        }
        
        var square = List.of(new coor(0, 0), new coor(0, 1),
                             new coor(1, 0), new coor(1, 1));
        var oriz_line = List.of(new coor(0, 0), new coor(0, 1),
                                new coor(0, 2), new coor(0, 3));
        var ver_line = List.of(new coor(0, 0), new coor(1, 0),
                               new coor(2, 0), new coor(3, 0));
        var mat = shelf.getAllShelf();
        var valid_matrix = new boolean[Shelf.N_ROW][Shelf.N_COL];
        
        var count = 0;
        for( int i = Shelf.N_ROW - 1; i > 0; i-- ) {
            for( int j = 0; j < Shelf.N_COL - 1; j++ ) {
                Tile tile_t = mat[i][j];
                var valid = tile_t != Tile.NOTILE;
                if( !valid )
                    continue;
                var current = new coor(i, j);
                Predicate<coor> is_valid =
                        (curr) -> !valid_matrix[curr.r][curr.c] && shelf.getTile(curr.r, curr.c) == tile_t;
                //tests for square forms
                valid = current.sum_list(square).stream().allMatch(is_valid);
                if( valid ) {
                    count++;
                    current.sum_list(square).forEach((x) -> valid_matrix[x.r][x.c] = true);
                    continue;
                }
                //tests for orizzontal lines forms
                valid = current.sum_list(oriz_line).stream().allMatch(is_valid);
                if( valid ) {
                    count++;
                    current.sum_list(oriz_line).forEach((x) -> valid_matrix[x.r][x.c] = true);
                    continue;
                }
                //tests for vertical lines forms
                valid = current.sum_list(ver_line).stream().allMatch(is_valid);
                if( valid ) {
                    count++;
                    current.sum_list(ver_line).forEach((x) -> valid_matrix[x.r][x.c] = true);
                }
            }
        }
        return count >= 2;
    }
    
    
}
