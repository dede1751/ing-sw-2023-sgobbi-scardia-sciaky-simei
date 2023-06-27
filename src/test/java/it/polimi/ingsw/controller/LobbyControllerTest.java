package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.LocalClient;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.socket.ClientSkeleton;
import it.polimi.ingsw.utils.files.ClientLogger;
import it.polimi.ingsw.view.messages.JoinLobbyMessage;
import it.polimi.ingsw.view.messages.RecoverLobbyMessage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
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
        public clientStub() throws RemoteException {
            super();
        }
        @Override
        public void update(ModelMessage<?> msg) {
        }
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
        
            lobby.addClient(client1, "Lucrezia");
            lobby.addClient(client2, "Luca");
    
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
            lobby.addClient(client1, "Lucrezia");
            lobby.addClient(client2, "Luca");
    
            LobbyController.LobbyView lobbyView = lobby.getLobbyView();
    
            assertEquals(lobbyView.nicknames().get(0), "Lucrezia");
            assertEquals(lobbyView.nicknames().get(1), "Luca");
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
            lobby.addClient(client1, "Lucrezia");
            lobby.addClient(client2, "Luca");
    
            LobbyController.LobbyView lobbyView = lobby.getLobbyView();
            
            assertTrue( lobbyView.isFull() );
        }
        
    }
    
    
    
    
}
