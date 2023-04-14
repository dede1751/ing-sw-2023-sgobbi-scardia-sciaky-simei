package it.polimi.ingsw.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests {@link Board}
 */
@Tag("Board")
@Tag("Model")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class BoardTest {
    
    @Test
    public void initTwoPlayer() {
        Board board = new Board(2);
        
        assertNotNull(board.getTile(new Coordinate(7, 3)));
        assertNull(board.getTile(new Coordinate(0, 3)));
    }
    
    @Test
    public void initFourPlayer() {
        Board board = new Board(4);
        
        assertNotNull(board.getTile(new Coordinate(1, 3)));
        assertNotNull(board.getTile(new Coordinate(2, 2)));
        assertNotNull(board.getTile(new Coordinate(3, 1)));
        assertNull(board.getTile(new Coordinate(-1, -1)));
    }
    
    @Test
    public void insertRemoveSelection() {
        Board board = new Board(3);
        
        var selection = new ArrayList<Coordinate>();
        Coordinate c = new Coordinate(6, 6);
        Tile t = new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE);
        selection.add(c);
        
        assertDoesNotThrow(() -> board.insertTile(c, t));
        assertEquals(board.getTile(c), t);
        
        board.removeSelection(selection);
        assertEquals(board.getTile(c), Tile.NOTILE);
    }
    
    @Test
    public void refillTest() {
        Board board = new Board(3);
        TileBag tileBag = new TileBag();
        board.refill(tileBag);
        
        for( Coordinate x : board.getTiles().keySet().stream().toList() ) {
            assertNotEquals(Tile.NOTILE, board.getTile(x));
        }
    }
    
}
