package it.polimi.ingsw.utils.mvc;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.view.messages.Move;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * IntegrityChecks unifies player input validation over game actions, to be used by both the client and server.
 */
public class IntegrityChecks {
    
    /**
     * Checks if the list of coordinates is a well-formed selection.
     * Selections must be a single connected line of 1 to 3 tiles.
     *
     * @param selection List of coordinates selected by the player
     *
     * @return true if the selection is valid, false otherwise
     */
    public static boolean checkSelectionForm(List<Coordinate> selection, Board board, Shelf shelf) {
        // selection is null, too small or has invalid coordinates, shelf cannot fit selection
        if( selection == null
            || selection.isEmpty()
            || selection.size() > 3
            || selection.stream()
                    .anyMatch((x) -> x.col() < 0 || x.col() > 8 || x.row() < 0 || x.row() > 8)
            || shelf.remainingSpace()
                    .values()
                    .stream()
                    .noneMatch((x) -> x >= selection.size()) ) {
            return false;
        }
        
        // there must be a tile on the selected coordinate, and at least one side must be free
        for( Coordinate c : selection ) {
            Tile t = board.getTile(c);
            if( t == null || t.equals(Tile.NOTILE) ) {
                return false;
            }
            
            Tile up = board.getTile(c.getUp());
            Tile down = board.getTile(c.getDown());
            Tile left = board.getTile(c.getLeft());
            Tile right = board.getTile(c.getRight());
            
            // All 4 sides are occupied
            if( up != null && !up.equals(Tile.NOTILE)
                && down != null && !down.equals(Tile.NOTILE)
                && left != null && !left.equals(Tile.NOTILE)
                && right != null && !right.equals(Tile.NOTILE) ) {
                return false;
            }
        }
        
        // Check that the coordinates are in a continuous line, and that there are no duplicates
        if( selection.stream().allMatch((x) -> selection.get(0).col() == x.col()) ) {
            ArrayList<Coordinate> sorted = new ArrayList<>(selection);
            sorted.sort(Comparator.comparingInt(Coordinate::row));
            
            Coordinate reference = sorted.get(0);

            int r1 = reference.row();
            int r2 = sorted.size() > 1 ? sorted.get(1).row() : r1 + 1;
            int r3 = sorted.size() > 2 ? sorted.get(2).row() : 2 * r2 - r1;
            
            return r1 - r2 == r2 - r3 && Math.abs(r1 - r2) == 1;
            
        }else if( selection.stream().allMatch((x) -> selection.get(0).row() == x.row()) ) {
            
            ArrayList<Coordinate> sorted = new ArrayList<>(selection);
            sorted.sort(Comparator.comparingInt(Coordinate::col));
            
            Coordinate reference = sorted.get(0);
            int c1 = reference.col();
            int c2 = sorted.size() > 1 ? sorted.get(1).col() : c1 + 1;
            int c3 = sorted.size() > 2 ? sorted.get(2).col() : 2 * c2 - c1;
            
            return c1 - c2 == c2 - c3 && Math.abs(c1 - c2) == 1;
            
        }else {
            return false;
        }
    }
    
    /**
     * Check that an ordered tile list is consistent with the coordinates selected from the board
     *
     * @param selection List of coordinates selected by the player on the board
     * @param tiles     ordered list of tiles at those coordinates to check
     * @param board     board to check
     *
     * @return true if the tiles are consistent with the coordinates, false otherwise
     */
    public static boolean checkTileSelection(List<Coordinate> selection, List<Tile> tiles, Board board) {
        if( tiles == null
            || tiles.isEmpty()
            || tiles.size() > 3
            || tiles.size() != selection.size()
            || tiles.contains(Tile.NOTILE) ) {
            return false;
        }
        
        List<Tile> control = new ArrayList<>();
        selection.forEach((x) -> control.add(board.getTile(x)));
        for( Tile t : tiles ) {
            if( !control.remove(t) ) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check that the tiles can be placed in the selected column of the shelf
     *
     * @param tiles  list of tiles to place
     * @param column column of the shelf to place the tiles in
     * @param shelf  shelf to check
     *
     * @return true if the tiles can be placed in the selected column of the shelf, false otherwise
     */
    public static boolean checkColumnValidity(List<Tile> tiles, int column, Shelf shelf) {
        if( column < 0 || column >= Shelf.N_COL ) {
            return false;
        }
        return shelf.spaceInColumn(column) >= tiles.size();
    }
    
    /**
     * Perform all the integrity checks on a move
     *
     * @param move  move to check
     * @param board board to base the check on
     * @param shelf shelf to base the check on
     *
     * @return true if the move is valid, false otherwise
     */
    public static boolean checkMove(Move move, Board board, Shelf shelf) {
        return checkSelectionForm(move.selection(), board, shelf)
               && checkTileSelection(move.selection(), move.tiles(), board)
               && checkColumnValidity(move.tiles(), move.column(), shelf);
    }
    
}
