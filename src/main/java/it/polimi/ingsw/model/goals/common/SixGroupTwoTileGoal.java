package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SixGroupTwoTileGoal implements CommonGoalStrategy {
    
    public String getDescription() {
        return "Six groups, each containing at least 2 tiles of the same type.";
    }
    
    @Override
    public boolean checkShelf(Shelf shelf) {
        
        var mat = shelf.getAllShelf();
        var checked = new boolean[Shelf.N_ROW][Shelf.N_COL];
        int totalCount = 0;
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
                
                if( selected.size() > 1 )
                    totalCount++;
            }
        }
        return totalCount >= 6;
    }
}
