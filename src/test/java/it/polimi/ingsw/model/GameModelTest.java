package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import it.polimi.ingsw.utils.files.ResourcesManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests {@link GameModel}
 */

@Tag("GameModel")
@Tag("Model")
public class GameModelTest {
    
    @Test
    void testInitialization() {
        GameModel game = new GameModel(4, 6, 5);
        assertEquals(6, game.getCommonGoalX());
        assertEquals(5, game.getCommonGoalY());
        assertEquals(4, game.getNumPlayers());
        assertFalse(game.isLastTurn());
    }
    
    @Test
    public void isLastTurn() {
        GameModel game = new GameModel(4, 0, 0);
        assertFalse(game.isLastTurn());
    }
    
    @Test
    public void addPlayerTest() {
        GameModel game = new GameModel(4, 0, 0);
        for( int i = 0; i < 4; i++ ) {
            Player player = new Player("nick_" + i, i);
            game.addPlayer("nick_" + i, i);
            
            assertEquals(player.getNickname(), game.getPlayers().get(i).getNickname());
            assertEquals(player.getPg(), game.getPlayers().get(i).getPg());
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
        Tile tile = new Tile(Tile.Type.TROPHIES, Tile.Sprite.TWO);
        Coordinate coord = new Coordinate(3, 4);
        
        assertDoesNotThrow(() -> game.insertTile(coord, tile));
        
        assertTrue(game.getAllCoordinates().contains(coord));
        assertTrue(game.getOccupied().contains(coord));
        
        assertEquals(45, game.getAllCoordinates().size());
        assertEquals(1, game.getOccupied().size());
    }
    
    @Test
    public void getOccupiedATest() {
        GameModel game = new GameModel(4, 0, 0);
        List<Coordinate> occupied = game.getOccupied();
        List<Coordinate> board = game.getAllCoordinates();
        
        for( Coordinate coordinate : board ) {
            if( game.getTile(coordinate) != Tile.NOTILE ) {
                assertTrue(occupied.contains(coordinate));
            }
        }
    }
    
    @Test
    void getTileInsertTileTest() {
        GameModel game = new GameModel(4, 5, 6);
        Tile tile = new Tile(Tile.Type.GAMES, Tile.Sprite.ONE);
        Coordinate coordinate = new Coordinate(4, 4);
        
        var out = assertThrows(OutOfBoundCoordinateException.class,
                               () -> game.insertTile(new Coordinate(0, 0), tile));
        out.stackPrintOrigin();
        
        assertDoesNotThrow(() -> game.insertTile(coordinate, tile));
        assertEquals(tile, game.getTile(coordinate));
        
        var occ = assertThrows(OccupiedTileException.class, () -> game.insertTile(coordinate, tile));
        occ.stackPrintOrigin();
        
        assertEquals(Tile.NOTILE, game.getTile(new Coordinate(5, 5)));
    }
    
    @Test
    void removeSelectionTest() {
        GameModel game = new GameModel(4, 5, 6);
        List<Coordinate> coords = List.of(
                new Coordinate(3, 4), new Coordinate(3, 5), new Coordinate(3, 6)
        );
        Tile tile = new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE);
        
        assertDoesNotThrow(() -> {
            for( Coordinate coord : coords ) {
                game.insertTile(coord, tile);
            }
        });
        
        for( Coordinate coord : coords ) {
            assertEquals(Tile.Type.TROPHIES, game.getTile(coord).type());
        }
        
        game.removeSelection(coords);
        for( Coordinate coord : coords ) {
            assertEquals(Tile.NOTILE, game.getTile(coord));
        }
    }
    
    @Test
    public void shelveRemoveSelectionTest() {
        GameModel game = new GameModel(2, 5, 6);
        Coordinate coord1 = new Coordinate(7, 3);
        Coordinate coord2 = new Coordinate(7, 4);
        Tile tile1 = new Tile(Tile.Type.CATS, Tile.Sprite.ONE);
        Tile tile2 = new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE);
        
        assertDoesNotThrow(() -> game.insertTile(coord1, tile1));
        assertDoesNotThrow(() -> game.insertTile(coord2, tile2));
        
        List<Tile> orderedTiles = List.of(tile1, tile2);
        game.addPlayer("Lucrezia", 1);
        game.addPlayer("Luca", 2);
        
        int column = 1;
        int tileAmount1 = game.getTileAmount(tile1);
        int tileAmount2 = game.getTileAmount(tile2);
        game.removeSelection(List.of(coord1, coord2));
        game.shelveSelection(orderedTiles, column);
        
        assertEquals(tile1, game.getCurrentPlayer().getShelf().getTile(0, column));
        assertEquals(tile2, game.getCurrentPlayer().getShelf().getTile(1, column));
        assertEquals(Tile.NOTILE, game.getTile(coord1));
        assertEquals(Tile.NOTILE, game.getTile(coord2));
        assertEquals(tileAmount1 - 1, game.getTileAmount(tile1));
        assertEquals(tileAmount2 - 1, game.getTileAmount(tile2));
    }
    
    @Test
    public void addCurrentPlayerScoreTest() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 1);
        game.addPlayer("Player 2", 2);
        
        int startingScore = game.getCurrentPlayer().getScore();
        int scoreToAdd = 10;
        int expectedScore = startingScore + scoreToAdd;
        
        game.addCurrentPlayerCommongGoalScore(scoreToAdd, GameModel.CGType.X);
        assertEquals(expectedScore, game.getCurrentPlayer().getScore());
    }
    
    @Test
    public void getTileAmountTest() {
        GameModel game = new GameModel(2, 5, 6);
        assertEquals(8, game.getTileAmount(new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)));
    }
    
    @Nested
    public class SaveTesting {
        @Test
        public void testNotThrow() {
            GameModel game = new GameModel(4, 5, 6);
            game.addPlayer("Luca", 5);
            game.addPlayer("Lucrezia", 6);
            game.addPlayer("Andrea", 7);
            game.addPlayer("Camilla", 8);
            assertDoesNotThrow(() -> {
                var file = ResourcesManager.openFileWrite(
                        ResourcesManager.testRootDir + "/resources/volatile", "SaveFile.json");
                var saveState = game.toJson();
                ByteBuffer buf = ByteBuffer.allocate(saveState.length());
                buf.put(saveState.getBytes());
                buf.flip();
                file.write(buf);
                file.close();
            });
        }
    }
}
