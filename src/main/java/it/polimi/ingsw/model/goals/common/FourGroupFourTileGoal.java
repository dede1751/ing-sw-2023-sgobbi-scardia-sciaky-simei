package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.*;

public class FourGroupFourTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Four groups, each containing at least 4 tiles of the same type.";
    }
    
    public boolean checkShelf(Shelf shelf) {
        
        Tile[][] mat = shelf.getAllShelf();
        boolean[][] checked = new boolean[Shelf.N_ROW][Shelf.N_COL];
        
        List<Coord> square = List.of(new Coord(0, 0), new Coord(0, 1), new Coord(1, 0), new Coord(1, 1));
        List<Coord> orizonthalLine = List.of(new Coord(0, 0), new Coord(0, 1), new Coord(0, 2), new Coord(0, 3));
        List<Coord> verticalLine = List.of(new Coord(0, 0), new Coord(1, 0), new Coord(2, 0), new Coord(3, 0));
        int total_count = 0;
        
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                Tile.Type type = mat[i][j].type();
                if( checked[i][j] || type == Tile.Type.NOTILE )
                    continue;
                
                var current = new Coord(i, j);
                //select chunck
                List<Coord> selected = new ArrayList<>();
                Queue<Coord> visited = new LinkedList<>();
                visited.add(current);
                checked[current.r()][current.c()] = true;
                while( !visited.isEmpty() ) {
                    current = visited.poll();
                    selected.add(current);
                    current.sumDir().stream()
                            .filter((x) -> x.r() < Shelf.N_ROW &&
                                           x.r() > -1 &&
                                           x.c() < Shelf.N_COL &&
                                           x.c() > -1 &&
                                           mat[x.r()][x.c()].type() == type &&
                                           !checked[x.r()][x.c()])
                            .forEach((x) -> {
                                visited.add(x);
                                checked[x.r()][x.c()] = true;
                            });
                }
                
                if( selected.size() == 4 ) {
                    //find nearest coord to the origin
                    var origin = selected.stream().reduce(selected.get(0), (x, y) -> {
                        if( x.c() > y.c() || (x.c() == y.c() && y.r() < y.c()) )
                            return y;
                        else
                            return x;
                    });
                    var form = selected.stream().map((x) -> x.sub(origin)).toList();
                    //intellij said that it was better to incapsulate all in hashSet, for performance reasons, who am I to contraddict
                    if( new HashSet<>(form).containsAll(square) || new HashSet<>(form).containsAll(orizonthalLine) ||
                        new HashSet<>(form).containsAll(verticalLine) )
                        total_count++;
                }
            }
        }
        return total_count >= 2;
    }
    
    
}
