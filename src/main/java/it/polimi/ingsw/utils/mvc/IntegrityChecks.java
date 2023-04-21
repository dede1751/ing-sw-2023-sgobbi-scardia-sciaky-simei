package it.polimi.ingsw.utils.mvc;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.Move;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class IntegrityChecks {
    
    public static boolean checkSelectionForm(List<Coordinate> selection) {
        if( selection.size() < 4 ) {
            var reference = selection.get(0);
            if( selection.stream().allMatch((x) -> reference.col() == x.col()) ) {
                
                var minRow = selection.stream().map(Coordinate::row).min(Integer::compare).get();
                var form = selection.stream().map((x) -> x.sub(new Coordinate(minRow, reference.col()))).toList();
                var verticalLine = List.of(new Coordinate(0, 0), new Coordinate(1, 0), new Coordinate(2, 0));
                return new HashSet<>(form).containsAll(verticalLine);
            }else if( selection.stream().allMatch((x) -> reference.row() == x.row()) ) {
                
                var minRow = selection.stream().map(Coordinate::col).min(Integer::compare).get();
                var form = selection.stream().map((x) -> x.sub(new Coordinate(minRow, reference.col()))).toList();
                var orizontalLine = List.of(new Coordinate(0, 0), new Coordinate(0, 1), new Coordinate(0, 2));
                return new HashSet<>(form).containsAll(orizontalLine);
            }else {
                return false;
            }
        }
        return false;
    }
    
    public static boolean checkTileSelection(List<Coordinate> coordinates, List<Tile> tiles, Board board) {
        if( tiles.size() > 3 )
            return false;
        var controll = new ArrayList<Tile>();
        coordinates.forEach((x) -> controll.add(board.getTile(x)));
        return (new HashSet<>(controll).containsAll(tiles)) && (new HashSet<>(tiles).containsAll(controll));
    }
    
    public static boolean checkColumnValidity(List<Tile> tiles, int column, Shelf shelf) {
        return shelf.spaceInColumn(column) >= tiles.size();
    }
    
    public static boolean checkMove(Move move, Board board, Shelf shelf) {
        return checkSelectionForm(move.selection()) && checkTileSelection(move.selection(), move.tiles(), board) &&
               checkColumnValidity(move.tiles(), move.column(), shelf);
    }
    
    
}
