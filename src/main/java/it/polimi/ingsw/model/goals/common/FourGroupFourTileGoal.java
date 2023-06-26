package it.polimi.ingsw.model.goals.common;

import it.polimi.ingsw.model.Coordinate;
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
        
        List<Coordinate> square = List.of(new Coordinate(0, 0),
                                          new Coordinate(0, 1),
                                          new Coordinate(1, 0),
                                          new Coordinate(1, 1));
        List<Coordinate> orizonthalLine = List.of(new Coordinate(0, 0),
                                                  new Coordinate(0, 1),
                                                  new Coordinate(0, 2),
                                                  new Coordinate(0, 3));
        List<Coordinate> verticalLine = List.of(new Coordinate(0, 0),
                                                new Coordinate(1, 0),
                                                new Coordinate(2, 0),
                                                new Coordinate(3, 0));
        int total_count = 0;
        
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                Tile.Type type = mat[i][j].type();
                if( checked[i][j] || type == Tile.Type.NOTILE )
                    continue;
                
                var current = new Coordinate(i, j);
                //select chunck
                List<Coordinate> selected = new ArrayList<>();
                Queue<Coordinate> visited = new LinkedList<>();
                visited.add(current);
                checked[current.row()][current.col()] = true;
                while( !visited.isEmpty() ) {
                    current = visited.poll();
                    selected.add(current);
                    current.adjacent().stream()
                            .filter((x) -> x.row() < Shelf.N_ROW &&
                                           x.row() > -1 &&
                                           x.col() < Shelf.N_COL &&
                                           x.col() > -1 &&
                                           mat[x.row()][x.col()].type() == type &&
                                           !checked[x.row()][x.col()])
                            .forEach((x) -> {
                                visited.add(x);
                                checked[x.row()][x.col()] = true;
                            });
                }
                
                if( selected.size() == 4 ) {
                    //find nearest coord to the origin
                    var origin = selected.stream().reduce(selected.get(0), (x, y) -> {
                        if( x.col() > y.col() || (x.col() == y.col() && y.row() < x.row()) )
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
        return total_count >= 4;
    }
}
