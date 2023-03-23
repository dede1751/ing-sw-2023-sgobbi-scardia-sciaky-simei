package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.List;
import java.util.function.BiPredicate;

public class SixGroupTwoTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Six groups, each containing at least 2 tiles of the same type.";
    }
    
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
        
        var or_line = List.of(new coor(0, 0), new coor(0, 1));
        var ve_line = List.of(new coor(0, 0), new coor(1, 0));
        
        var or_cont = List.of(new coor(1, 0), new coor(1, 1), new coor(0, 2),
                              new coor(-1, 1), new coor(-1, 0), new coor(0, -1));
        var ve_cont = List.of(new coor(-1, 0), new coor(0, 1), new coor(0, -1),
                              new coor(1, -1), new coor(1, 1), new coor(2, 0));
        
        BiPredicate<List<coor>, Tile> is_valid_form = (x, t) -> (
                x.stream().allMatch(y -> (y.r < Shelf.N_ROW && y.c < Shelf.N_COL && y.r >= 0 && y.c >= 0) &&
                                         tile_matrix[y.r][y.c] == t && t != Tile.NOTILE));
        BiPredicate<List<coor>, Tile> is_valis_cont = (x, t) -> (
                x.stream().allMatch(y -> shelf.getTile(y.r, y.c) != t ||
                                         shelf.getTile(y.r, y.c) == t && !valid_matrix[y.r][y.c]));
        
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                var current = new coor(i, j);
                var tile = tile_matrix[i][j];
                boolean valid;
                valid = is_valid_form.test(current.sum_list(or_line), tile) &&
                        is_valis_cont.test(current.sum_list(or_cont), tile);
                if( valid ) {
                    current.sum_list(or_line).forEach((x) -> valid_matrix[x.r][x.c] = true);
                    current.sum_list(or_cont).forEach((x) -> {
                        if( shelf.getTile(x.r, x.c) == tile ) {
                            valid_matrix[x.r][x.c] = true;
                        }
                    });
                    count++;
                    continue;
                }
                current.sum_list(or_cont).forEach((x) -> {
                    if( shelf.getTile(x.r, x.c) == tile ) {
                        valid_matrix[x.r][x.c] = true;
                    }
                });
                valid = is_valid_form.test(current.sum_list(ve_line), tile) &&
                        is_valis_cont.test(current.sum_list(ve_cont), tile);
                if( valid ) {
                    current.sum_list(ve_line).forEach((x) -> valid_matrix[x.r][x.c] = true);
                    count++;
                }
                current.sum_list(ve_cont).forEach((x) -> {
                    if( shelf.getTile(x.r, x.c) == tile ) {
                        valid_matrix[x.r][x.c] = true;
                    }
                });
            }
        }
        return count >= 6;
    }
}
