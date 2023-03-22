package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SixGroupTwoTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Six groups, each containing at least 2 tiles of the same type.";
    }
    
    /*
    public boolean checkShelf(Shelf shelf) {
        
        var mat = shelf.getAllShelf();
        var check_matrix = new boolean[Shelf.N_ROW][Shelf.N_COL];
        BiPredicate<Integer, Integer> is_valid = (r, c) -> {
            return (r < Shelf.N_ROW && r >= 0 && c >= 0 && c < Shelf.N_COL) && mat[r][c] != Tile.NOTILE &&
                   !check_matrix[r][c];
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
                        check_matrix[i][j + 1] = true;
                        count++;
                    }
                }
            }
        }
        return count >= 6;
    }
    */
    // TODO test new implementation of checkShelf
    @Override
    public boolean checkShelf(Shelf shelf) {
        int count = 0;
        
        record coor(int r, int c) {
            coor sum_off(coor off) {
                return new coor(r + off.r, c + off.c);
            }
            
            List<coor> sum_list(List<coor> off) {
                return off.stream().map((x) -> x.sum_off(this)).toList();
            }
        }
        
        var tile_matrix = shelf.getAllShelf();
        var valid_matrix = new boolean[Shelf.N_ROW][Shelf.N_COL];
        
        var or_line = List.of(new coor(0, 0), new coor(1, 0));
        var ve_line = List.of(new coor(0, 0), new coor(0, 1));
        
        var or_cont = List.of(new coor(1, 0), new coor(1, 1), new coor(0, 2),
                              new coor(-1, 1), new coor(-1, 0), new coor(0, -1));
        var ve_cont = List.of(new coor(-1, 0), new coor(0, 1), new coor(0, -1),
                              new coor(1, -1), new coor(1, 1), new coor(2, 0));
        
        Predicate<List<coor>> is_valid_form = (x) -> (
                x.stream().allMatch((y) -> tile_matrix[y.r][y.c] == tile_matrix[x.get(0).r][x.get(0).c]) &&
                x.stream().noneMatch(y -> shelf.getTile(y.r, y.c) == null)
        );
        
        BiPredicate<List<coor>, Tile> is_valis_cont = (x, t) -> (
                x.stream().allMatch(y -> shelf.getTile(y.r, y.c) != t || !valid_matrix[y.r][y.c])
        );
        
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                var current = new coor(i, j);
                var tile = tile_matrix[i][j];
                boolean valid;
                valid = is_valid_form.test(current.sum_list(or_line)) &&
                        is_valis_cont.test(current.sum_list(or_cont), tile);
                if( valid ) {
                    current.sum_list(or_line).forEach((x) -> valid_matrix[x.r][x.c] = true);
                    count++;
                    continue;
                }
                valid = is_valid_form.test(current.sum_list(ve_line)) &&
                        is_valis_cont.test(current.sum_list(ve_cont), tile);
                if( valid ) {
                    current.sum_list(ve_line).forEach((x) -> valid_matrix[x.r][x.c] = true);
                    count++;
                }
            }
        }
        return count >= 6;
    }
}
