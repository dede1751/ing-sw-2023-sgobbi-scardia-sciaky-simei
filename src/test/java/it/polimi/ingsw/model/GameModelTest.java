package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.utils.mvc.ModelListener;
import it.polimi.ingsw.view.messages.ChatMessage;
import it.polimi.ingsw.view.messages.RecoverLobbyMessage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.List;
import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests {@link GameModel}
 */
@Tag("GameModel")
@Tag("Model")
public class GameModelTest {
    
    
    private static class MockupServer extends LocalServer {
        public MockupServer() throws RemoteException {
            super();
        }
        @Override
        public void removeGameControllers(List<Client> clients) {
        }
    }
    static {
        try {
            LobbyController.getInstance().setServer(new GameModelTest.MockupServer());
        }
        catch( RemoteException e ) {
            fail(e.getCause());
        }
    }
    
    private static class MockupListener implements ModelListener {
        
        private List<String> expectedPlayers = new ArrayList<>();
        private String expectedWinner;
        private String expectedMessage;
        private String expectedSender;
        private String expectedDestination;
        private String expectedResponse;
    
        @Override
        public void update(ModelMessage<?> msg) {
            
            if( msg instanceof StartGameMessage ) {
                var players = ((StartGameMessage) msg).getPayload().players()
                                .stream()
                                .map(StartGameMessage.PlayerRecord::nickname)
                                .toList();
                assertEquals( expectedPlayers, players );
                
            } else if( msg instanceof EndGameMessage ) {
                var winner = ((EndGameMessage) msg).getPayload().winner();
                assertEquals( expectedWinner, winner );

            } else if( msg instanceof IncomingChatMessage ) {
                var s = ((IncomingChatMessage) msg).getPayload();
                var sender = ((IncomingChatMessage) msg).getSender();
                var dest = ((IncomingChatMessage) msg).getDestination();
                assertEquals( expectedMessage, s );
                assertEquals( expectedSender, sender );
                assertEquals( expectedDestination, dest );

            } else if(msg instanceof ServerResponseMessage ) {
                var response = ((ServerResponseMessage) msg).getPayload().msg();
                assertEquals( expectedResponse, response );
            }
        }
        
        public void setNicknames(List<String> nicknames) {
            this.expectedPlayers.addAll(nicknames);
        }
        public void setWinner(String winner) {
            this.expectedWinner = winner;
        }
        public void setMessage(String msg) {
            this.expectedMessage = msg;
        }
        public void setSender(String sender) {
            this.expectedSender = sender;
        }
        public void setDestination(String dest) {
            this.expectedDestination = dest;
        }
        public void setResponse(String response) {
            this.expectedResponse = response;
        }
        
    }

    @Test
    void notifyServerMessageTest() {
        Stack<Integer> CGXS = new Stack<>();
        Stack<Integer> CGYS = new Stack<>();
        Board board = new Board( 2 );
        TileBag tileBag = new TileBag();
        GameModel game = new GameModel( 2, 5, 6,
                CGXS, CGYS, board, tileBag );

        MockupListener listener1 = new MockupListener();
        MockupListener listener2 = new MockupListener();
        game.addListener( "Lucrezia", listener1 );
        game.addListener( "Luca", listener2 );

        ServerResponseMessage msg = new ServerResponseMessage( Response.NicknameTaken(
                RecoverLobbyMessage.class.getSimpleName()) );

        Player player1 = new Player( "Lucrezia", 0 );
        Player player2 = new Player( "Lucrezia", 1 );
        game.addPlayer( player1 );
        game.addPlayer( player2 );

        listener1.setResponse( "NicknameTaken");
        listener2.setResponse( "NicknameTaken");
        game.notifyServerMessage( "Lucrezia", msg );
    }

    @Test
    void chatBrokerTest() {

        Stack<Integer> CGXS = new Stack<>();
        Stack<Integer> CGYS = new Stack<>();
        Board board = new Board( 2 );
        TileBag tileBag = new TileBag();
        GameModel game = new GameModel( 2, 5, 6,
                CGXS, CGYS, board, tileBag );

        Player player1 = new Player( "Lucrezia", 0 );
        Player player2 = new Player( "Luca", 1 );
        game.addPlayer( player1 );
        game.addPlayer( player2 );

        MockupListener listener1 = new MockupListener();
        MockupListener listener2 = new MockupListener();
        game.addListener( "Lucrezia", listener1 );
        game.addListener( "Luca", listener2 );

        ChatMessage msg1 = new ChatMessage( "Hello world", "Lucrezia" );
        listener1.setMessage( "Hello world" );
        listener1.setSender( "Lucrezia" );
        listener1.setDestination( "ALL" );
        listener2.setMessage( "Hello world" );
        listener2.setSender( "Lucrezia" );
        listener2.setDestination( "ALL" );
        game.chatBroker( msg1 );

        ChatMessage msg2 = new ChatMessage( "Hi Luca", "Lucrezia", "Luca" );
        listener1.setMessage( "Hi Luca" );
        listener1.setSender( "Lucrezia" );
        listener1.setDestination( "Luca" );
        listener2.setMessage( "Hi Luca" );
        listener2.setSender( "Lucrezia" );
        listener2.setDestination( "Luca" );
        game.chatBroker( msg2 );
    }

    @Test
    void startGameTest() {
        
        Stack<Integer> CGXS = new Stack<>();
        Stack<Integer> CGYS = new Stack<>();
        Board board = new Board( 2 );
        TileBag tileBag = new TileBag();
        GameModel game = new GameModel( 2, 5, 6,
                                        CGXS, CGYS, board, tileBag );
        
        Player player1 = new Player( "Lucrezia", 0 );
        Player player2 = new Player( "Luca", 1 );
        game.addPlayer( player1 );
        game.addPlayer( player2 );
        
        MockupListener listener1 = new MockupListener();
        MockupListener listener2 = new MockupListener();
        List<String> nicknames = new ArrayList<>();
        nicknames.add( "Lucrezia" );
        nicknames.add( "Luca" );
        listener1.setNicknames( nicknames );
        listener2.setNicknames( nicknames );
        game.addListener( "Lucrezia", listener1 );
        game.addListener( "Luca", listener2 );
        
        game.startGame();
        
    }
    
    
    @Tag("notifyWinner")
    @Nested
    class notifyWinner {
        @Test
        void notifyWinnerTest1() {
            Stack<Integer> CGXS = new Stack<>();
            Stack<Integer> CGYS = new Stack<>();
            Board board = new Board(2);
            TileBag tileBag = new TileBag();
            GameModel game = new GameModel(2, 5, 6,
                                           CGXS, CGYS, board, tileBag);
        
            Player player1 = new Player("Lucrezia", 0);
            Player player2 = new Player("Luca", 1);
            game.addPlayer(player1);
            game.addPlayer(player2);
            player1.setPersonalGoalScore(8);
            player2.setPersonalGoalScore(4);
        
            MockupListener listener1 = new MockupListener();
            MockupListener listener2 = new MockupListener();
            String winner = "Lucrezia";
            listener1.setWinner(winner);
            listener2.setWinner(winner);
            game.addListener("Lucrezia", listener1);
            game.addListener("Luca", listener2);
    
            game.notifyWinner();
        }

        @Test
        void notifyWinnerTest2() {
            Stack<Integer> CGXS = new Stack<>();
            Stack<Integer> CGYS = new Stack<>();
            Board board = new Board(2);
            TileBag tileBag = new TileBag();
            GameModel game = new GameModel(2, 5, 6,
                    CGXS, CGYS, board, tileBag);

            Player player1 = new Player("Lucrezia", 0);
            Player player2 = new Player("Luca", 1);
            game.addPlayer(player1);
            game.addPlayer(player2);
            player1.setPersonalGoalScore(4);
            player2.setPersonalGoalScore(8);

            MockupListener listener1 = new MockupListener();
            MockupListener listener2 = new MockupListener();
            String winner = "Luca";
            listener1.setWinner(winner);
            listener2.setWinner(winner);
            game.addListener("Lucrezia", listener1);
            game.addListener("Luca", listener2);

            game.notifyWinner();
        }

        @Test
        void notifyWinnerTest3() {
            Stack<Integer> CGXS = new Stack<>();
            Stack<Integer> CGYS = new Stack<>();
            Board board = new Board(2);
            TileBag tileBag = new TileBag();
            GameModel game = new GameModel(2, 5, 6,
                    CGXS, CGYS, board, tileBag);

            Player player1 = new Player("Lucrezia", 0);
            Player player2 = new Player("Luca", 1);
            game.addPlayer(player1);
            game.addPlayer(player2);
            player1.setPersonalGoalScore(8);
            player2.setPersonalGoalScore(8);

            MockupListener listener1 = new MockupListener();
            MockupListener listener2 = new MockupListener();
            String winner = "Luca";
            listener1.setWinner(winner);
            listener2.setWinner(winner);
            game.addListener("Lucrezia", listener1);
            game.addListener("Luca", listener2);

            game.notifyWinner();
        }
    }
    
    @Test
    void testInitialization() {
        GameModel game = new GameModel(4, 6, 5);
        assertEquals(6, game.getCommonGoalX());
        assertEquals(5, game.getCommonGoalY());
        assertEquals(4, game.getNumPlayers());
        assertFalse(game.isLastTurn());
    }
    
    @Test
    public void getGameModelTest() {
        GameModel game = new GameModel(2, 6, 5);
        int numPlayers = game.getNumPlayers();
        int CGX = game.getCommonGoalX();
        int CGY = game.getCommonGoalY();
        assertEquals(numPlayers, 2);
        assertEquals(CGX, 6);
        assertEquals(CGY, 5);
    }
    
    @Test
    public void stackTest() {
        GameModel game = new GameModel(2, 6, 5);
        
        int CGXScore = game.popStackCGX();
        int CGYScore = game.popStackCGY();
        assertEquals(8, CGXScore);
        assertEquals(8, CGYScore);
        
        CGXScore = game.peekStackCGX();
        CGYScore = game.peekStackCGY();
        assertEquals(4, CGXScore);
        assertEquals(4, CGYScore);
        
        game.popStackCGX();
        game.popStackCGY();
        
        assertEquals(0, game.peekStackCGX());
        assertEquals(0, game.peekStackCGY());
    }
    
    @Test
    public void setLastTurnTest() {
        GameModel game = new GameModel(2, 6, 5);
        game.addPlayer("Lucrezia", 1);
        game.addPlayer("Luca", 2);
        int score = game.getCurrentPlayer().getScore();
        assertFalse(game.isLastTurn());
        
        game.setLastTurn();
        assertTrue(game.isLastTurn());
        assertEquals(score + 1, game.getCurrentPlayer().getScore());
    }
    
    @Test
    public void isLastTurn() {
        GameModel game = new GameModel(4, 0, 0);
        assertFalse(game.isLastTurn());
    }
    
    @Test
    public void addPlayerTest1() {
        GameModel game = new GameModel(4, 0, 0);
        for( int i = 0; i < 4; i++ ) {
            Player player = new Player("nick_" + i, i);
            game.addPlayer("nick_" + i, i);
            
            assertEquals(player.getNickname(), game.getPlayers().get(i).getNickname());
            assertEquals(player.getPg(), game.getPlayers().get(i).getPg());
        }
    }
    
    @Test
    public void addPlayerTest2() {
        GameModel game = new GameModel(2, 0, 0);
        Player player = new Player("Player1", 0);
        game.addPlayer(player);
        assertEquals(game.getPlayers().get(0).getNickname(), "Player1");
    }
    
    @Test
    void getPlayersTest() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 1);
        game.addPlayer("Player 2", 2);
        
        var players = game.getPlayers();
        assertEquals(players.get(0).getNickname(), "Player 1");
        assertEquals(players.get(0).getPg(), 1);
    }
    
    @Test
    void setCurrentPlayerIndex() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 1);
        game.addPlayer("Player 2", 2);
        
        game.setCurrentPlayerIndex(0);
        assertEquals(0, game.getCurrentPlayerIndex());
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
    public void addCurrentPlayerCommonGoalScoreTest() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 0);
        game.addPlayer("Player 2", 1);
        
        game.setCurrentPlayerIndex(0);
        int startingScore1 = game.getCurrentPlayer().getScore();
        int CGXScore = game.popStackCGX();
        int expectedScore = startingScore1 + CGXScore;
        
        game.addCurrentPlayerCommongGoalScore(CGXScore, GameModel.CGType.X);
        assertEquals(expectedScore, game.getCurrentPlayer().getScore());
        
        int CGYScore = game.popStackCGY();
        expectedScore = expectedScore + CGYScore;
        
        game.addCurrentPlayerCommongGoalScore(CGYScore, GameModel.CGType.Y);
        assertEquals(expectedScore, game.getCurrentPlayer().getScore());
        
        game.popStackCGX();
        game.popStackCGY();
        
        game.setCurrentPlayerIndex(1);
        int startingScore2 = game.getCurrentPlayer().getScore();
        CGXScore = game.peekStackCGX();
        CGYScore = game.peekStackCGY();
        game.addCurrentPlayerCommongGoalScore(CGYScore, GameModel.CGType.X);
        game.addCurrentPlayerCommongGoalScore(CGYScore, GameModel.CGType.Y);
        assertEquals(startingScore2, game.getCurrentPlayer().getScore());
        
    }
    
    @Test
    public void getNicknamesTest() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 0);
        game.addPlayer("Player 2", 1);
        var nicknames = game.getNicknames();
        
        game.setCurrentPlayerIndex(0);
        var nickname1 = nicknames.get(0);
        assertEquals(nickname1, game.getCurrentPlayer().getNickname());
        
        game.setCurrentPlayerIndex(1);
        var nickname2 = nicknames.get(1);
        assertEquals(nickname2, game.getCurrentPlayer().getNickname());
    }
    
    
    @Test
    public void setCurrentPlayerPersonalScoreTest() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 1);
        game.addPlayer("Player 2", 2);
        
        int score = 10;
        
        game.setCurrentPlayerPersonalScore(score);
        assertEquals(score, game.getCurrentPlayer().getPersonalGoalScore());
    }
    
    @Test
    public void setCurrentPlayerAdjencyScoreTest() {
        GameModel game = new GameModel(2, 5, 6);
        game.addPlayer("Player 1", 1);
        game.addPlayer("Player 2", 2);
        
        int score = 10;
        
        game.setCurrentPlayerAdjacencyScore(score);
        assertEquals(score, game.getCurrentPlayer().getAdjacencyScore());
    }
    
    @Test
    public void getOccupiedTest() {
        GameModel game = new GameModel(2, 5, 6);
        
        game.refillBoard();
        assertEquals(29, game.getOccupied().size());
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
    
    @Test
    public void endGameTest() {
        GameModel game = new GameModel(2, 5, 6);
        
        var gameEnded = game.getGameEnded();
        assertFalse(gameEnded);
        
        game.setGameEnded(true);
        gameEnded = game.getGameEnded();
        assertTrue(gameEnded);
    }
    
    @Test
    public void getTileBagTest() {
        GameModel game = new GameModel(2, 5, 6);
        
        var tileBag = game.getTileBag();
        assertEquals(132, tileBag.currentTileNumber());
    }
    
    
}
