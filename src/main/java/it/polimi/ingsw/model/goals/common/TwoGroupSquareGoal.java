package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.List;
import java.util.function.Predicate;

public class TwoGroupSquareGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Two 2x2 squares of tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        
        record Coord(int r, int c) {
            Coord sumOffset(Coord offset) {
                return new Coord(r + offset.r, c + offset.c);
            }
        }
        
        boolean[][] checked = new boolean[Shelf.N_ROW][Shelf.N_COL];
        
        List<Coord> square = List.of(new Coord(0, 0), new Coord(1, 0), new Coord(1, 1), new Coord(0, 1));
        
        Predicate<Coord> isValid = (x) -> (
                x.r >= 0
                && x.r < Shelf.N_ROW
                && x.c >= 0
                && x.c < Shelf.N_COL
                && shelf.getTile(x.r, x.c) != Tile.NOTILE
                && !checked[x.r][x.c]);
        
        int count = 0;
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                Coord curr = new Coord(i, j);
                boolean valid = square.stream()
                        .map(x -> isValid.test(x.sumOffset(curr))
                                  && shelf.getTile(curr.r, curr.c).type() == shelf.getTile(curr.r + x.r, curr.c + x.c).type())
                        .reduce(true, (a, b) -> a && b);
                
                if( valid ) {
                    for( Coord x : square ) {
                        checked[i + x.r][j + x.c] = true;
                    }
                    count++;
                }
            }
        }
        
        return count > 1;
    }
    
}
