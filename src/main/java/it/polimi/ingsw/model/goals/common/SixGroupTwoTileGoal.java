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
        
        record Coord(int r, int c) {
            Coord sumOffset(Coord offset) {
                return new Coord(r + offset.r, c + offset.c);
            }
        
            List<Coord> sumList(List<Coord> off) {
                return off.stream().map((x) -> x.sumOffset(this)).toList();
            }
            
            boolean inBounds() { return r >= 0 && r < Shelf.N_ROW && c >= 0 && c < Shelf.N_COL; }
        }
    
        var tileMatrix = shelf.getAllShelf();
        var checked = new boolean[Shelf.N_ROW][Shelf.N_COL];
    
        List<Coord> orLine = List.of(new Coord(0, 0), new Coord(0, 1));
        List<Coord> veLine = List.of(new Coord(0, 0), new Coord(1, 0));
        List<Coord> orCont = List.of(new Coord(1, 0), new Coord(1, 1), new Coord(0, 2), new Coord(-1, 1), new Coord(-1, 0), new Coord(0, -1));
        List<Coord> veCont = List.of(new Coord(-1, 0), new Coord(0, 1), new Coord(0, -1), new Coord(1, -1), new Coord(1, 1), new Coord(2, 0));
        
        BiPredicate<List<Coord>, Tile> isValidForm = (x, t) -> (
                x.stream().allMatch(y -> y.inBounds()
                                         && tileMatrix[y.r][y.c].type() == t.type()
                                         && t != Tile.NOTILE));
        
        BiPredicate<List<Coord>, Tile> isValidCont = (x, t) -> (
                x.stream().allMatch(y -> shelf.getTile(y.r, y.c).type() != t.type()
                                             || (shelf.getTile(y.r, y.c).type() == t.type() && !checked[y.r][y.c])));
    
        int count = 0;
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                Coord curr = new Coord(i, j);
                Tile tile = tileMatrix[i][j];
                
                boolean valid = isValidForm.test(curr.sumList(orLine), tile)
                                && isValidCont.test(curr.sumList(orCont), tile);
                if ( valid ) {
                    curr.sumList(orLine).forEach((x) -> checked[x.r][x.c] = true);
                    curr.sumList(orCont).forEach((x) -> {
                        if( x.inBounds() && shelf.getTile(x.r, x.c).type() == tile.type() ) {
                            checked[x.r][x.c] = true;
                        }
                    });
                    count++;
                    continue;
                }
                
                curr.sumList(orCont).forEach((x) -> {
                    if( x.inBounds() && shelf.getTile(x.r, x.c).type() == tile.type() ) {
                        checked[x.r][x.c] = true;
                    }
                });
                
                valid = isValidForm.test(curr.sumList(veLine), tile)
                        && isValidCont.test(curr.sumList(veCont), tile);
                if ( valid ) {
                    curr.sumList(veLine).forEach((x) -> checked[x.r][x.c] = true);
                    count++;
                }
                
                curr.sumList(veCont).forEach((x) -> {
                    if( x.inBounds() && shelf.getTile(x.r, x.c).type() == tile.type() ) {
                        checked[x.r][x.c] = true;
                    }
                });
            }
        }
        return count >= 6;
    }
}
