package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import it.polimi.ingsw.utils.exceptions.occupiedTileException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * this class tests {@link GameModel}
 */
public class GameModelTest {
    
    @Test
    void testInitialization() {
        GameModel game = new GameModel(4, 6, 5);
        assertEquals(6, game.getCommonGoalX());
        assertEquals(5, game.getCommonGoalY());
        assertEquals(4, game.getNumPlayers());
        assertFalse(game.isFinalTurn());
    }
    
    
    @Test
    public void isFinalTurn() {
        GameModel game = new GameModel(4, 0, 0);
        assertFalse(game.isFinalTurn());
        
        
    }
    
    @Test
    public void addPlayerTest() {
        
        GameModel game = new GameModel(4, 0, 0);
        for( int i = 0; i < 4; i++ ) {
            var player = new Player("nick_" + i, i);
            game.addPlayer("nick_" + i, i);
            assertFalse(!(player.getNickname().equals(game.getPlayers().get(i).getNickname()) &&
                          player.getPg() == game.getPlayers().get(i).getPg()));
        }
    }
    
    
    @Test
    void testGetAndSetCurrentPlayer() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 1);
        game.addPlayer("Player 2", 2);
        
        assertEquals("Player 1", game.getCurrentPlayer().getNickname());
    }
    
    
    @Test
    void testGetCoordinates() {
        GameModel game = new GameModel(4, 5, 6);
        Tile tile1 = Tile.TROPHIES;
        assertDoesNotThrow(() -> game.insertTile(new Coordinate(3, 4), tile1));
        assertTrue(game.getAllCoordinates().contains(new Coordinate(3, 4)));
        assertTrue(game.getOccupied().contains(new Coordinate(3, 4)));
        assertEquals(45, game.getAllCoordinates().size());
        assertEquals(1, game.getOccupied().size());
        
    }
    
    @Test
    public void getOccupiedATest() {
        var game = new GameModel(4, 0, 0);
        var occupied = game.getOccupied();
        var board = game.getAllCoordinates();
        for( Coordinate coordinate : board ) {
            if( !(game.getTile(coordinate).equals(Tile.NOTILE)) ) {
                assertTrue(occupied.contains(coordinate));
            }
        }
    }
    
    @Test
    void getTileInsertTileTest() {
        GameModel game = new GameModel(4, 5, 6);
        Tile tile = Tile.GAMES;
        Coordinate coordinate = new Coordinate(4, 4);
        var out = assertThrows(OutOfBoundCoordinateException.class,
                               () -> game.insertTile(new Coordinate(0, 0), Tile.TROPHIES));
        out.stackPrintOrigin();
        assertDoesNotThrow(() -> game.insertTile(coordinate, tile));
        assertEquals(tile, game.getTile(coordinate));
        var occ = assertThrows(occupiedTileException.class, () -> game.insertTile(coordinate, Tile.TROPHIES));
        occ.stackPrintOrigin();
        assertEquals(Tile.NOTILE, game.getTile(new Coordinate(5, 5)));
    }
    
    
    @Test
    void removeSelectionTest() {
        GameModel game = new GameModel(4, 5, 6);
        Tile tile1 = Tile.TROPHIES;
        Tile tile2 = Tile.TROPHIES;
        Tile tile3 = Tile.TROPHIES;
        assertDoesNotThrow(() -> {
            game.insertTile(new Coordinate(3, 4), tile1);
            game.insertTile(new Coordinate(3, 5), tile2);
            game.insertTile(new Coordinate(3, 6), tile3);
        });
        assertEquals(Tile.TROPHIES, game.getTile(new Coordinate(3, 4)));
        assertEquals(Tile.TROPHIES, game.getTile(new Coordinate(3, 5)));
        assertEquals(Tile.TROPHIES, game.getTile(new Coordinate(3, 6)));
        
        List<Coordinate> removing = new ArrayList<Coordinate>();
        removing.add(new Coordinate(3, 4));
        removing.add(new Coordinate(3, 5));
        removing.add(new Coordinate(3, 6));
        game.removeSelection(removing);
        
        assertEquals(Tile.NOTILE, game.getTile(new Coordinate(3, 4)));
        assertEquals(Tile.NOTILE, game.getTile(new Coordinate(3, 5)));
        assertEquals(Tile.NOTILE, game.getTile(new Coordinate(3, 6)));
        
        
    }
    
    @Test
    public void addCurrentPlayerScoreTest() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 1);
        game.addPlayer("Player 2", 2);
        int startingScore = game.getCurrentPlayer().getScore();
        int scoreToAdd = 10;
        int expectedScore = startingScore + scoreToAdd;
        game.addCurrentPlayerScore(scoreToAdd);
        
        assertEquals(expectedScore, game.getCurrentPlayer().getScore());
    }
    
    
    @Test
    public void getTileAmountTest() {
        GameModel game = new GameModel(2, 5, 6);
        assertEquals(22, game.getTileAmount(Tile.TROPHIES));
    }
    
}
