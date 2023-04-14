package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.List;
import java.util.function.BiPredicate;

public class FourGroupFourTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Four groups, each containing at least 4 tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        record coor(int r, int c) {
            coor sum_off(coor offset) {
                return new coor(r + offset.r(), c + offset.c());
            }
        }
        ;
        var square = List.of(new coor(0, 0), new coor(0, 1), new coor(1, 0), new coor(1, 1));
        var mat = shelf.getAllShelf();
        var valid_matrix = new boolean[Shelf.N_ROW][Shelf.N_COL];
        
        var count = 0;
        //check square forms
        for( int i = Shelf.N_ROW - 1; i > 0; i-- ) {
            for( int j = 0; j < Shelf.N_COL - 1; j++ ) {
                Tile tile_t = mat[i][j];
                var valid = tile_t != Tile.NOTILE;
                if( !valid )
                    continue;
                var current_coor = new coor(i, j);
                BiPredicate<coor, coor> is_valid = (curr, off) -> {
                    var index = curr.sum_off(off);
                    return !valid_matrix[index.r][index.c] && shelf.getTile(index.r, index.c) == tile_t;
                };
                for( var x : square ) {
                    valid &= is_valid.test(current_coor, x);
                }
                if( valid ) {
                    count++;
                    for( var x : square ) {
                        var y = current_coor.sum_off(x);
                        valid_matrix[y.r][y.c] = true;
                    }
                }
            }
        }
        return count >= 6;
    }
}
