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
        
        record Coord(int r, int c) {
            Coord sumOffset(Coord offset) {
                return new Coord(r + offset.r(), c + offset.c());
            }
            
            List<Coord> sumList(List<Coord> offset) {
                return offset.stream().map((x) -> x.sumOffset(this)).toList();
            }
        }
        
        Tile[][] mat = shelf.getAllShelf();
        boolean[][] checked = new boolean[Shelf.N_ROW][Shelf.N_COL];
        
        List<Coord> square = List.of(new Coord(0, 0), new Coord(0, 1), new Coord(1, 0), new Coord(1, 1));
        List<Coord> orizonthalLine = List.of(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2), new Coord(0, 3));
        List<Coord> verticalLine = List.of(new Coord(0, 0), new Coord(1, 0), new Coord(2, 0), new Coord(3, 0));
        
        int count = 0;
        for ( int i = Shelf.N_ROW - 1; i > 0; i-- ) {
            for ( int j = 0; j < Shelf.N_COL - 1; j++ ) {
                Tile.Type tile = mat[i][j].type();
                
                if ( tile == Tile.Type.NOTILE ) {
                    continue;
                }
                
                Coord current = new Coord(i, j);
                Predicate<Coord> isValid = curr -> 
                        !checked[curr.r][curr.c]
                        && shelf.getTile(curr.r, curr.c).type() == tile;
                
                //tests for square forms
                if( current.sumList(square).stream().allMatch(isValid) ) {
                    count++;
                    current.sumList(square).forEach((x) -> checked[x.r][x.c] = true);
                    continue;
                }
                
                //tests for orizonthal line forms
                if( current.sumList(orizonthalLine).stream().allMatch(isValid) ) {
                    count++;
                    current.sumList(orizonthalLine).forEach((x) -> checked[x.r][x.c] = true);
                    continue;
                }
                
                //tests for vertical line forms
                if( current.sumList(verticalLine).stream().allMatch(isValid) ) {
                    count++;
                    current.sumList(verticalLine).forEach((x) -> checked[x.r][x.c] = true);
                }
            }
        }
        return count >= 2;
    }
    
    
}
