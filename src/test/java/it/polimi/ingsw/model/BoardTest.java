package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * This class tests {@link Board}
 */
public class BoardTest {

    @Test
    public void initTwoPlayer() {
        Board board = new Board(2);

        assertNotNull (board.getTile(new Coordinate(1, 3)));
        assertNull(board.getTile(new Coordinate(0, 3)));
    }

    @Test
    public void initFourPlayer() {
        Board board = new Board(4);

        assertNotNull (board.getTile(new Coordinate(1, 3)));
        assertNotNull (board.getTile(new Coordinate(3, 8)));
        assertNotNull (board.getTile(new Coordinate(7, 3)));
        assertNull(board.getTile(new Coordinate(-1, -1)));
    }

    @Test
    public void insertRemoveSelection() {
        Board board = new Board(3);

        var selection = new ArrayList<Coordinate>();

        var c = new Coordinate(6, 6);
        selection.add(c);
        board.insertTile(c, Tile.TROPHIES);
        assertEquals(board.getTile(c), Tile.TROPHIES);

        board.removeSelection(selection);
        assertEquals(board.getTile(c), Tile.NOTILE);

    }
}
