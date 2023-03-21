package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;

import java.util.List;
import java.util.function.Predicate;

public class TwoGroupSquareGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Two 2x2 squares of tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        var count = 0;
        record coor(int r, int c) {
            coor sum_off(coor off) {
                return new coor(r + off.r, c + off.c);
            }
        }
        var checked_matrix = new boolean[Shelf.N_ROW][Shelf.N_COL];
        var square = List.of(new coor(0, 0), new coor(1, 0), new coor(1, 1), new coor(0, 1));
        Predicate<coor> is_valid = (x) -> {
            return (x.r >= 0 && x.r < Shelf.N_ROW && x.c >= 0 && x.c < Shelf.N_COL) &&
                   (shelf.getTile(x.r, x.c) != null) && (!checked_matrix[x.r][x.c]);
        };
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                var current = new coor(i, j);
                var valid = square.stream()
                        .map(x -> is_valid.test(x.sum_off(current)) &&
                                  shelf.getTile(current.r, current.c) ==
                                  shelf.getTile(current.r + x.r, current.c + x.c))
                        .reduce(true, (a, b) -> a && b);
                if( valid ) {
                    for( var x : square ) {
                        checked_matrix[i + x.r][j + x.c] = true;
                    }
                    count++;
                }
            }
        }
        
        return count > 1;
    }
    
    
}
