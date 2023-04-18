package it.polimi.ingsw.utils.mvc;

import it.polimi.ingsw.model.Coordinate;

import java.util.HashSet;
import java.util.List;

public class IntegrityChecks {
    
    public static boolean checkSelection(List<Coordinate> selection) {
        if(selection.size() < 4){
            var reference = selection.get(0);
            if(selection.stream().allMatch((x) -> reference.col() == x.col())){
                
                var minRow = selection.stream().map(Coordinate::row).reduce(reference.row(), (x, y) -> {
                    if( x > y )
                        return y;
                    else
                        return x;
                });
                var form = selection.stream().map((x) -> x.sub(new Coordinate(minRow, reference.col()))).toList();
                var verticalLine = List.of(new Coordinate(0, 0), new Coordinate(1,0), new Coordinate(2, 0));
                return new HashSet<>(form).containsAll(verticalLine);
            }else if(selection.stream().allMatch((x) -> reference.row() == x.row())){
                
                var minRow = selection.stream().map(Coordinate::col).reduce(reference.col(), (x, y) -> {
                    if( x > y )
                        return y;
                    else
                        return x;
                });
                var form = selection.stream().map((x) -> x.sub(new Coordinate(minRow, reference.col()))).toList();
                var orizontalLine = List.of(new Coordinate(0, 0), new Coordinate(0,1), new Coordinate(0, 2));
                return new HashSet<>(form).containsAll(orizontalLine);
            }else{
                return false;
            }
        }
        return false;
    }
}
