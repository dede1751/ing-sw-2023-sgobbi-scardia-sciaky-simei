package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.messages.AvailableLobbyMessage;
import it.polimi.ingsw.model.messages.ModelMessage;

import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.view.messages.CreateLobbyMessage;
import it.polimi.ingsw.view.messages.JoinLobbyMessage;
import it.polimi.ingsw.view.messages.ViewMessage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.view.messages.*;
import org.junit.jupiter.api.*;


import java.io.File;
import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class LobbyControllerTest {
    
    
    private static class mockupServer extends LocalServer {
        public mockupServer() throws RemoteException {
            super();
        }
        @Override
        public void removeGameControllers(List<Client> clients) {
        }
    }
    static {
        try {
            LobbyController.getInstance().setServer(new GameControllerTest.mockupServer());
        }
        catch( RemoteException e ) {
            fail(e.getCause());
        }
    }
    
    private static class clientStub implements Client {
        
        private Response lastResponse;
        private List<LobbyController.LobbyView> lobbies;
        public clientStub() throws RemoteException {
            super();
        }
        @Override
        public void update(ModelMessage<?> msg) {
            if( msg instanceof ServerResponseMessage ){
                lastResponse = ((ServerResponseMessage) msg).getPayload();
            }else if(msg instanceof AvailableLobbyMessage ){
                lobbies = ((AvailableLobbyMessage) msg).getPayload().lobbyViewList();
            }
        }
        
        public Response getLastResponse() {
            return lastResponse;
        }
        public List<LobbyController.LobbyView> getLobbies(){
            return lobbies;
        }
    }
    
    
    private static class UnimplementedViewMessage extends ViewMessage<Integer> {
        
        public UnimplementedViewMessage(Integer integer, String playerNick) {
            super(integer, playerNick);
        }
    }
    /*
    @BeforeEach
    public void eliminateRecovery(){
        
        File rec = ResourcesManager.getRecoveryDir();
        if(rec != null){
            File[] files = rec.listFiles();
            if(files != null) {
                for( var x : files ) {
                    x.delete();
                }
            }
        }
    }
    */
    
    private String nameModifier(String name){
        while(LobbyController.nicknameTaken(name)){
            name = name + "+";
        }
        return name;
    }
    
    
    @Test
    public void unimplementedViewMessage(){
        assertDoesNotThrow(() -> {
            GameModel game = new GameModel(2, 5, 6);
            Map<String, Client> clients = new HashMap<>();
            
            Stack<Integer> personalGoals = new Stack<>();
            int pg1 = 1;
            int pg2 = 2;
            personalGoals.add(pg1);
            personalGoals.add(pg2);
            
            int lobbySize = 2;
            int lobbyID = 0;
            boolean isRecovery = false;
            
            LobbyController.Lobby lobby = new LobbyController.Lobby(
                    game, clients, personalGoals, lobbySize, lobbyID, isRecovery);
            
            clientStub client1 = new clientStub();
            clientStub client2 = new clientStub();
            String nick1 = nameModifier("__L|u|c|r|e|z|i|a__");
            String nick2 = nameModifier("__L|u|c|a|__");
            
            lobby.addClient(client1, nick1);
            lobby.addClient(client2, nick2);
            
            assertFalse(LobbyController.getInstance().update(client1, new UnimplementedViewMessage(0, "Lucrezia")));
            
        });
    }
    
    @Test
    public void OnMessageCreateLobby(){
        assertDoesNotThrow(() -> {
            
            clientStub client1 = new clientStub();
            clientStub client2 = new clientStub();
            
            String nick1 = nameModifier("__L|u|c|r|e|z|i|a__");
            String nick2 = nameModifier("__L|u|c|a|__");
            
            LobbyController.getInstance().update(client1, new CreateLobbyMessage(2,nick1));
            assertEquals(0, client1.lastResponse.status());
            LobbyController.getInstance().update(client2, new CreateLobbyMessage(2,nick1));
            assertEquals(-1, client2.lastResponse.status());
            LobbyController.getInstance().update(client2, new CreateLobbyMessage(3, ""));
            assertEquals(-1, client2.lastResponse.status());
            LobbyController.getInstance().update(client2, new CreateLobbyMessage(2, null));
            assertEquals(-1, client2.lastResponse.status());
            LobbyController.getInstance().update(client2, new CreateLobbyMessage(5, nick2));
            assertEquals(-1, client2.lastResponse.status());
        });
    }
    
    @Test
    public void OnMessageJoinLobby(){
        
        assertDoesNotThrow(() -> {
            
            clientStub client1 = new clientStub();
            clientStub client2 = new clientStub();
            
            String nick1 = nameModifier("__|C|a|m|i|l|l|a|__");
            String nick2 = nameModifier("__|p|i|p|p|o|__");
            String nick3 = nameModifier("__|A|n|d|r|e|a|__");
            String nick4 = nameModifier("__|R|o|b|__");
            
            LobbyController.getInstance().update(client1, new CreateLobbyMessage(2, nick1));
            assertEquals(0, client1.lastResponse.status());
            LobbyController.getInstance().update(client2, new RequestLobbyMessage(null, nick2));
            int lobbyID = client2.getLobbies().size() - 1;
            
            LobbyController.getInstance().update(client2, new JoinLobbyMessage(lobbyID, nick1 ));
            assertEquals(-1, client2.lastResponse.status());
            
            LobbyController.getInstance().update(client2, new JoinLobbyMessage(lobbyID, ""));
            assertEquals(-1, client2.lastResponse.status());
            LobbyController.getInstance().update(client2, new JoinLobbyMessage(lobbyID, null));
            assertEquals(-1, client2.lastResponse.status());
            
            LobbyController.getInstance().update(client2, new JoinLobbyMessage(lobbyID +10, nick3));
            assertEquals(-1, client2.lastResponse.status());
            
            LobbyController.getInstance().update(client2, new JoinLobbyMessage(lobbyID, nick3));
            assertEquals(0, client2.lastResponse.status());
            
            LobbyController.getInstance().update(client2, new JoinLobbyMessage(lobbyID, nick4));
            assertEquals(-1, client2.lastResponse.status());
        });
    }
    
    @Tag("Lobby")
    @Nested
    class lobbyTest {
        
        
        @Test
        public void isEmptyTest() throws RemoteException {
        
            GameModel game = new GameModel(2, 5, 6);
            Map<String, Client> clients = new HashMap<>();
        
            Stack<Integer> personalGoals = new Stack<>();
            int pg1 = 1;
            int pg2 = 2;
            personalGoals.add(pg1);
            personalGoals.add(pg2);
        
            int lobbySize = 2;
            int lobbyID = 0;
            boolean isRecovery = false;
        
            LobbyController.Lobby lobby = new LobbyController.Lobby(
                    game, clients, personalGoals, lobbySize, lobbyID, isRecovery);
        
            assertTrue(lobby.isEmpty());
        
            clientStub client1 = new clientStub();
            clientStub client2 = new clientStub();
        

            lobby.addClient(client1, "|L|u|c|r|e|z|i|a|");
            lobby.addClient(client2, "|L|u|c|a|");
            
            assertFalse(lobby.isEmpty());

        }
    
    
        @Test
        public void recoverLobbyTest() {
        
            GameModel game = new GameModel(2, 5, 6);
            Map<String, Client> clients = new HashMap<>();
            Stack<Integer> personalGoals = new Stack<>();
            int lobbySize = 2;
            int lobbyID = 0;
            boolean isRecovery = true;
        
            LobbyController.Lobby lobby = new LobbyController.Lobby(
                    game, clients, personalGoals, lobbySize, lobbyID, isRecovery);
            
            assertTrue( lobby.isRecovery() );
    
            LobbyController.Lobby newLobby = lobby.recoverLobby();
            assertFalse( newLobby.isRecovery() );
        }
    
    
        @Test
        public void getLobbyViewTest() throws RemoteException {
        
            GameModel game = new GameModel(2, 5, 6);
            Map<String, Client> clients = new HashMap<>();
    
            Stack<Integer> personalGoals = new Stack<>();
            int pg1 = 1;
            int pg2 = 2;
            personalGoals.add(pg1);
            personalGoals.add(pg2);
    
            int lobbySize = 2;
            int lobbyID = 0;
            boolean isRecovery = false;
        
            LobbyController.Lobby lobby = new LobbyController.Lobby(
                    game, clients, personalGoals, lobbySize, lobbyID, isRecovery);
        
            clientStub client1 = new clientStub();
            clientStub client2 = new clientStub();
            lobby.addClient(client1, "|L|u|c|r|e|z|i|a|");
            lobby.addClient(client2, "|L|u|c|a|");
    
            LobbyController.LobbyView lobbyView = lobby.getLobbyView();
    
            assertEquals(lobbyView.nicknames().get(0), "|L|u|c|r|e|z|i|a|");
            assertEquals(lobbyView.nicknames().get(1), "|L|u|c|a|");
            assertEquals(lobbyView.lobbySize(), 2);
            assertEquals(lobbyView.lobbyID(), 0);
            assertFalse(lobbyView.isRecovery());
        }
        
    }
    
    
    @Tag("LobbyView")
    @Nested
    class lobbyViewTest {
        
        
        @Test
        public void toStringTest() throws RemoteException {
    
            GameModel game = new GameModel(2, 5, 6);
            
            Map<String, Client> clients = new HashMap<>();
            Stack<Integer> personalGoals = new Stack<>();
            int pg1 = 1;
            int pg2 = 2;
            personalGoals.add(pg1);
            personalGoals.add(pg2);
    
            int lobbySize = 2;
            int lobbyID = 0;
            boolean isRecovery = true;
    
            LobbyController.Lobby lobby = new LobbyController.Lobby(
                    game, clients, personalGoals, lobbySize, lobbyID, isRecovery);
            
            lobby.model().addPlayer( "Lucrezia", 1 );
            lobby.model().addPlayer( "Luca", 2 );
            
            LobbyController.LobbyView lobbyView = lobby.getLobbyView();
            assertTrue( lobby.isRecovery() );
    
            assertEquals("""
                                 
                                 ------RECOVERING LOBBY------
                                 Players:\s
                                 \tLucrezia
                                 \tLuca
                                 """,
                         lobbyView.toString() );
    
            LobbyController.Lobby newLobby = lobby.recoverLobby();
            assertFalse( newLobby.isRecovery() );
            lobbyView = newLobby.getLobbyView();
    
            assertEquals("""
                                 
                                 ------LOBBY: 0------
                                 Occupancy: [2/2]
                                 \tLucrezia
                                 \tLuca
                                 """,
                         lobbyView.toString() );
        }
        
        
        @Test
        public void isFullTest() throws RemoteException {
    
            GameModel game = new GameModel(2, 5, 6);
            Map<String, Client> clients = new HashMap<>();
    
            Stack<Integer> personalGoals = new Stack<>();
            int pg1 = 1;
            int pg2 = 2;
            personalGoals.add(pg1);
            personalGoals.add(pg2);
    
            int lobbySize = 2;
            int lobbyID = 0;
            boolean isRecovery = false;
    
            LobbyController.Lobby lobby = new LobbyController.Lobby(
                    game, clients, personalGoals, lobbySize, lobbyID, isRecovery);
    
            clientStub client1 = new clientStub();
            clientStub client2 = new clientStub();
            lobby.addClient(client1, "|L|u|c|r|e|z|i|a");
            lobby.addClient(client2, "|L|u|c|a|");
    
            LobbyController.LobbyView lobbyView = lobby.getLobbyView();
            
            assertTrue( lobbyView.isFull() );
        }
        
    }
    
    
    @Test
    public void endGameTest() throws RemoteException {
    
        GameModel game = new GameModel(2, 5, 6);
        Map<String, Client> clients = new HashMap<>();
    
        Stack<Integer> personalGoals = new Stack<>();
        int pg1 = 1;
        int pg2 = 2;
        personalGoals.add(pg1);
        personalGoals.add(pg2);
    
        int lobbySize = 2;
        int lobbyID = 0;
        boolean isRecovery = false;
    
        LobbyController.Lobby lobby = new LobbyController.Lobby(
                game, clients, personalGoals, lobbySize, lobbyID, isRecovery);
    
        clientStub client1 = new clientStub();
        clientStub client2 = new clientStub();
        lobby.addClient(client1, "Lucrezia");
        lobby.addClient(client2, "Luca");
    
        LobbyController lobbyController = new LobbyController();
        lobbyController.setServer(new mockupServer());
        lobbyController.endGame( 0 );
        
    }
    
    
    @Test
    public void updateTest() throws RemoteException {
    
        GameModel game = new GameModel(2, 5, 6);
        Map<String, Client> clients = new HashMap<>();
    
        Stack<Integer> personalGoals = new Stack<>();
        int pg1 = 1;
        int pg2 = 2;
        personalGoals.add(pg1);
        personalGoals.add(pg2);
    
        int lobbySize = 2;
        int lobbyID = 0;
        boolean isRecovery = false;
    
        LobbyController.Lobby lobby = new LobbyController.Lobby(
                game, clients, personalGoals, lobbySize, lobbyID, isRecovery);
    
        LobbyController lobbyController = new LobbyController();
    
        clientStub client1 = new clientStub();
        clientStub client2 = new clientStub();
        
        lobby.addClient(client1, "Lucrezia");
        JoinLobbyMessage msg1 = new JoinLobbyMessage( 0, "Lucrezia" );
        lobbyController.update( client1, msg1 );
        
        lobby.addClient(client2, "Luca");
        JoinLobbyMessage msg2 = new JoinLobbyMessage( 0, "Lucrezia" );
        lobbyController.update( client2, msg2 );
        
        assertFalse( lobby.isEmpty() );
        
    }
    
    
    
}
