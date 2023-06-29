package it.polimi.ingsw.model;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
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
    public void insertExceptionTest() throws OutOfBoundCoordinateException, OccupiedTileException {
        Board board = new Board(2);
        Coordinate coord = new Coordinate(1, 4);
        Tile tile1 = new Tile(Tile.Type.CATS, Tile.Sprite.ONE);
        Tile tile2 = new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE);
        
        board.insertTile(coord, tile1);
        assertThrows(OccupiedTileException.class, () -> board.insertTile(coord, tile2));
        
        Coordinate err = new Coordinate(0, 0);
        assertThrows(OutOfBoundCoordinateException.class, () -> board.insertTile(err, tile2));
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
    
    @Test
    public void getAsMatrixTest() {
        Board board = new Board(2);
        var matrix = board.getAsMatrix();
        
        for( int i = 0; i < 9; i++ ) {
            for( int j = 0; j < 9; j++ ) {
                var coord = new Coordinate(i, j);
                assertEquals(board.getTile(coord), matrix[i][j]);
            }
        }
    }
    
    
    @Test
    public void serDeserTest() {
        Board board = new Board( 2 );
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Board.class, new Board.BoardSerializer());
        var ser = gson.create().toJson(board, Board.class);
    
        gson.registerTypeAdapter(Board.class, new Board.BoardDeserializer());
        var deser = gson.create().fromJson(ser, Board.class);
    
        var json = board.toString();
        assertEquals(json, ser);
        
        var mat1 = board.getAsMatrix();
        var mat2 = deser.getAsMatrix();
        
        for( int i = 0; i < 9; i++ ) {
            for( int j = 0; j < 9; j++ ) {
                assertEquals( mat1[i][j], mat2[i][j] );
            }
        }
    }
}
