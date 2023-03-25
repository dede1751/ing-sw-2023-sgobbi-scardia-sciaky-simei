package it.polimi.ingsw.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests {@link Shelf}
 */
@Tag("Shelf")
@Tag("Model")
@TestInstance(Lifecycle.PER_METHOD)
public class ShelfTest {
    
    public Shelf shelf;
    public List<List<Tile>> columns;
    
    @BeforeEach
    public void initParam() {
        shelf = new Shelf();
        fillShelfDet();
    }
    
    public void fillShelfDet() {
        columns = new ArrayList<>();
        
        columns.add(0, List.of(
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE)));
        columns.add(1, List.of(
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.BOOKS, Tile.Sprite.ONE),
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE)));
        columns.add(2, List.of(
                new Tile(Tile.Type.FRAMES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)));
        columns.add(3, List.of(
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE)));
        /*
         * COL 0 : CBP
         * COL 1 : PTBC
         * COL 2 : FTTTTT (full)
         * COL 3 : PPPPP
         * COL 4 : (empty)
         */
        for( int i = 0; i < columns.size(); i++ ) {
            shelf.addTiles(columns.get(i), i);
        }
    }
    
    /**
     * This method tests the addTiles(), getAllShelf(), getTiles() methods
     */
    @Test
    @DisplayName("Test addTiles, getAllShelf, getTiles all together")
    public void addTiles_getAllShelf_getTilesTest() {
        Tile[][] s = shelf.getAllShelf();
        
        for( int i = 0; i < columns.size(); i++ ) {
            for( int j = 0; j < columns.get(i).size(); j++ ) {
                assertEquals(columns.get(i).get(j), s[j][i]);
                assertEquals(columns.get(i).get(j), shelf.getTile(j, i));
                assertEquals(s[j][i], shelf.getTile(j, i));
            }
            for( int j = columns.get(i).size(); j < Shelf.N_ROW; j++ ) {
                assertEquals(s[j][i], Tile.NOTILE);
            }
        }
        
        for( int i = columns.size(); i < Shelf.N_COL; i++ ) {
            for( int j = 0; j < Shelf.N_ROW; j++ ) {
                assertEquals(s[j][i], Tile.NOTILE);
                assertEquals(shelf.getTile(j, i), Tile.NOTILE);
            }
        }
        
    }
    
    @Test
    @DisplayName("test for all valid insertion sizes")
    public void availableColumnTest() {
        assertEquals(shelf.availableColumns(1), List.of(0, 1, 3, 4));
        assertEquals(shelf.availableColumns(2), List.of(0, 1, 4));
        assertEquals(shelf.availableColumns(3), List.of(0, 4));
        assertEquals(shelf.availableColumns(0), List.of(0, 1, 2, 3, 4));
    }
    
    @Test
    public void spaceInColumnTest() {
        assertEquals(3, shelf.spaceInColumn(0));
        assertEquals(2, shelf.spaceInColumn(1));
        assertEquals(0, shelf.spaceInColumn(2));
        assertEquals(1, shelf.spaceInColumn(3));
        assertEquals(6, shelf.spaceInColumn(4));
    }
    
    @Test
    public void remainingSpaceTest() {
        var rem = shelf.remainingSpace();
        rem.forEach((x, y) -> assertEquals(shelf.spaceInColumn(x), y));
    }
    
    @Test
    public void isFullTest() {
        assertFalse(shelf.isFull());
        Shelf s = new Shelf();
        
        for( int i = 0; i < Shelf.N_COL; i++ ) {
            s.addTiles(
                    List.of(
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)),
                    i);
        }
        assertTrue(s.isFull());
        
        s = new Shelf();
        for( int i = 0; i < Shelf.N_COL; i++ ) {
            s.addTiles(
                    List.of(
                            Tile.NOTILE,
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE),
                            new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)),
                    i);
        }
        assertFalse(s.isFull());
    }
}
