package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.messages.AvailableLobbyMessage;
import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.utils.exceptions.DuplicateListener;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.utils.files.ServerLogger;
import it.polimi.ingsw.view.messages.CreateLobbyMessage;
import it.polimi.ingsw.view.messages.JoinLobbyMessage;
import it.polimi.ingsw.view.messages.RecoverLobbyMessage;
import it.polimi.ingsw.view.messages.RequestLobbyMessage;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;


/**
 * Handles registration to multiple lobbies for concurrent play
 * LobbyController receives RequestLobby, RecoverLobby, CreateLobby and JoinLobby messages.
 * It always directly responds to each of these messages with a ServerResponseMessage, both in case of success and failure.
 * - Synchronization:
 * LobbyController constitutes a single synchronization lock, through which all login actions have to go through,
 * and also single-handedly controls the controller mapping on the LocalServer.
 */
public class LobbyController {
    
    /**
     * Lobby record, representing both recovery and normal lobbies
     *
     * @param model         Game model
     * @param clients       List of clients for the lobby
     * @param personalGoals List of personal goals, null for recovery lobbies
     * @param lobbySize     Lobby size
     * @param lobbyID       Unique lobby id
     * @param isRecovery    True if the lobby is a recovery lobby
     */
    public record Lobby(
            GameModel model,
            Map<String, Client> clients,
            Stack<Integer> personalGoals,
            int lobbySize,
            int lobbyID,
            boolean isRecovery
    ) {
        public boolean isEmpty() {
            if( isRecovery ) {
                return clients.containsValue(null);
            }else {
                return clients.size() < lobbySize();
            }
        }
        
        public void addClient(Client client, String nickname) {
            clients.put(nickname, client);
            
            if( !isRecovery ) {
                this.model.addPlayer(nickname, this.personalGoals.pop());
            }
            
            try {
                this.model.addListener(nickname, (msg) -> {
                    try {
                        client.update(msg);
                        ServerLogger.messageLog(this.toString(), msg);
                    }
                    catch( RemoteException e ) {
                        // If the client is disconnected, remove it from the lobby and end the game for everyone
                        LobbyController.getInstance().disconnectClient(client);
                        ServerLogger.errorLog(e, "Client : " + nickname);
                    }
                });
            }
            catch( DuplicateListener e ) {
                ServerLogger.errorLog(e, "Client : " + client);
            }
        }
        
        public Lobby recoverLobby() {
            return new Lobby(model, clients, personalGoals, lobbySize, lobbyID, false);
        }
        
        public LobbyView getLobbyView() {
            return new LobbyView(model.getNicknames(), lobbySize, lobbyID, isRecovery);
        }
    }
    
    /**
     * LobbyView is a serializable version of Lobby to be sent to the client for information
     *
     * @param nicknames  List of nicknames
     * @param lobbySize  Lobby size
     * @param lobbyID    Unique lobby id
     * @param isRecovery True if the lobby is a recovery lobby
     */
    public record LobbyView(
            List<String> nicknames,
            int lobbySize,
            int lobbyID,
            boolean isRecovery
    ) implements Serializable {
        @Override
        public String toString() {
            StringBuilder nickames = new StringBuilder();
            for( String nickname : this.nicknames() ) {
                nickames.append("\t").append(nickname).append("\n");
            }
            
            if( isRecovery ) {
                return ("\n------RECOVERING LOBBY------\n") +
                       ("Players: \n")
                       + nickames;
            }else {
                return ("\n------LOBBY: " + this.lobbyID() + "------\n") +
                       ("Occupancy: [" + this.nicknames().size() + "/" + this.lobbySize() + "]\n")
                       + nickames;
            }
        }
    }
    
    private static LobbyController INSTANCE;
    
    // Lobbies are mapped by their unique lobbyID (recovery lobbies have their id's reset)
    private static final HashMap<Integer, Lobby> lobbies = new HashMap<>();
    
    private LocalServer server = null;
    
    private Client client = null;
    
    /**
     * Init LobbyController by reading all the saved models from disk
     */
    private LobbyController() {
        for( GameModel model : ResourcesManager.getSavedModels() ) {
            int lobbyID = lobbies.size();
            
            // Create a new recovery lobby for the model
            Map<String, Client> clients = new HashMap<>();
            for( String nickname : model.getNicknames() ) {
                clients.put(nickname, null);
            }
            Lobby lobby = new Lobby(model, clients, null, clients.size(), lobbyID, true);
            
            // Save the model with the new id
            ResourcesManager.saveModel(model, lobbyID);
            lobbies.put(lobbyID, lobby);
        }
    }
    
    /**
     * Get the singleton instance of the LobbyController
     *
     * @return LobbyController instance
     */
    public static LobbyController getInstance() {
        if( INSTANCE == null ) {
            INSTANCE = new LobbyController();
        }
        
        return INSTANCE;
    }
    
    /**
     * Set the server reference, only possible once
     *
     * @param server Server reference
     */
    public void setServer(LocalServer server) {
        if( this.server == null ) {
            this.server = server;
        }
    }
    
    /**
     * End a game, removing the lobby from the tracked list of lobbies
     *
     * @param lobbyID Lobby to stop tracking
     */
    public synchronized void endGame(int lobbyID) {
        List<Client> clients = lobbies.get(lobbyID)
                .clients
                .values()
                .stream()
                .toList();
        
        this.server.removeGameControllers(clients);
        lobbies.remove(lobbyID);
        ResourcesManager.deleteModel(lobbyID);
        
        ServerLogger.log("Game ended, removed lobby : " + lobbyID);
    }
    
    /**
     * Disconnect a client when a socket is not able to receive anymore (this does not work with RMI)
     *
     * @param client Client to disconnect (along with the whole lobby)
     */
    public synchronized void disconnectClient(Client client) {
        Lobby lobby = lobbies.values()
                .stream()
                .filter(l -> l.clients.containsValue(client))
                .findFirst()
                .orElse(null);
        
        if( lobby == null ) {
            ServerLogger.log("Client without an active lobby disconnected");
            return;
        }
        
        this.endGame(lobby.lobbyID());
        lobby.model.notifyWinner(); // force game end
    }
    
    /**
     * Set the client being currently served.
     * This method is only used due to Idiosyncrasies with Reflection. Since Clients are classes implementing a common
     * interface, we can't simply have a onMessage(Client, Msg) method, since reflection will wind up using the client's
     * dynamic type, so we have to set the served client first instead.
     * This needs to always be called in conjunction with an onMessage method, with proper synchronization.
     *
     * @param client Client to serve
     */
    public void setServedClient(Client client) {
        this.client = client;
    }
    
    /**
     * Simple update function to incorporate logging
     *
     * @param nickname Client nickname
     * @param m        Message to send
     */
    private void update_client(String nickname, ModelMessage<?> m) {
        try {
            client.update(m);
            ServerLogger.messageLog(nickname, m);
        }
        catch( RemoteException e ) {
            ServerLogger.errorLog(e, "Client : " + nickname);
        }
    }
    
    /**
     * Return the list of lobbies to the client
     *
     * @param msg Lobby parameters
     */
    @SuppressWarnings("unused")
    public void onMessage(RequestLobbyMessage msg) {
        List<LobbyView> lobbies = searchForLobbies(msg.getPayload());
        update_client(msg.getPlayerNickname(), new AvailableLobbyMessage(lobbies));
    }
    
    /**
     * Add the client to a recovery lobby
     *
     * @param msg Lobby parameters
     */
    @SuppressWarnings("unused")
    public void onMessage(RecoverLobbyMessage msg) {
        String nickname = msg.getPlayerNickname();
        if( nickname == null ) {
            update_client(nickname,
                          new ServerResponseMessage(Response.NicknameNull(CreateLobbyMessage.class.getSimpleName())));
            return;
        }
        
        Lobby lobby = lobbies.values()
                .stream()
                .filter((l) -> l.isRecovery && l.model.getNicknames().contains(nickname))
                .findFirst()
                .orElse(null);
        
        // lobby is unavailable/doesn't exist
        if( lobby == null ) {
            update_client(nickname, new ServerResponseMessage(
                    Response.LobbyUnavailable(RecoverLobbyMessage.class.getSimpleName())));
            return;
        }
        
        // username is already taken
        int index = lobby.model.getNicknames().indexOf(nickname);
        if( lobby.clients.get(nickname) != null ) {
            update_client(nickname,
                          new ServerResponseMessage(Response.NicknameTaken(RecoverLobbyMessage.class.getSimpleName())));
            return;
        }
        
        lobby.addClient(client, nickname);
        
        // check if the game needs to be started
        if( !lobby.isEmpty() ) {
            Map<Client, GameController> mapping = new HashMap<>();
            GameController controller = new GameController(lobby.model, lobby.lobbyID);
            
            for( Client c : lobby.clients.values() ) {
                mapping.put(client, controller);
            }
            
            // set the lobby as fully recovered
            Lobby newLobby = lobby.recoverLobby();
            lobbies.replace(lobby.lobbyID, newLobby);
            
            server.addGameControllers(mapping);
        }
        
        update_client(nickname, new ServerResponseMessage(Response.Ok(RecoverLobbyMessage.class.getSimpleName())));
    }
    
    private static int[] randDistinctIndices(int count) {
        return new Random().ints(0, 12)
                .distinct()
                .limit(count)
                .toArray();
    }
    
    /**
     * Create a new lobby
     *
     * @param msg Lobby creation message
     */
    @SuppressWarnings("unused")
    public void onMessage(CreateLobbyMessage msg) {
        String nickname = msg.getPlayerNickname();
        if( nickname == null ) {
            update_client(nickname,
                          new ServerResponseMessage(Response.NicknameNull(CreateLobbyMessage.class.getSimpleName())));
            return;
        }
        
        if( nicknameTaken(nickname) ) {
            update_client(nickname,
                          new ServerResponseMessage(Response.NicknameTaken(CreateLobbyMessage.class.getSimpleName())));
            return;
        }
        int lobbySize = msg.getPayload();
        if( lobbySize < 2 || lobbySize > 4 ) {
            update_client(nickname, new ServerResponseMessage(Response.InvalidLobbySize()));
            return;
        }
        
        // initialize lobby and model
        int lobbyID = lobbies.size();
        int[] commonGoalIndices = randDistinctIndices(2);
        int[] personalGoalIndices = randDistinctIndices(lobbySize);
        
        GameModel model = new GameModel(lobbySize, commonGoalIndices[0], commonGoalIndices[1]);
        Stack<Integer> personalGoals = new Stack<>();
        for( int i : personalGoalIndices ) {
            personalGoals.push(i);
        }
        
        Lobby lobby = new Lobby(model, new HashMap<>(), personalGoals, lobbySize, lobbyID, false);
        
        lobby.addClient(client, nickname);
        lobbies.put(lobbyID, lobby);
        
        update_client(
                nickname,
                new ServerResponseMessage(Response.Ok(CreateLobbyMessage.class.getSimpleName())));
    }
    
    /**
     * Join a lobby with the supplied id, and if needed start the game
     *
     * @param msg Description of lobby to join
     */
    @SuppressWarnings("unused")
    public void onMessage(JoinLobbyMessage msg) {
        String nickname = msg.getPlayerNickname();
        Lobby lobby = lobbies.get(msg.getPayload());
        
        if( lobby == null || !lobby.isEmpty() ) {
            update_client(nickname,
                          new ServerResponseMessage(Response.LobbyUnavailable(JoinLobbyMessage.class.getSimpleName())));
            return;
        }
        
        if( msg.getPlayerNickname() == null ) {
            update_client(nickname,
                          new ServerResponseMessage(Response.NicknameNull(JoinLobbyMessage.class.getSimpleName())));
            return;
        }
        
        if( nicknameTaken(msg.getPlayerNickname()) ) {
            update_client(nickname,
                          new ServerResponseMessage(Response.NicknameTaken(JoinLobbyMessage.class.getSimpleName())));
            return;
        }
        
        lobby.addClient(client, nickname);
        
        // check if the game needs to be started
        if( !lobby.isEmpty() ) {
            Map<Client, GameController> mapping = new HashMap<>();
            GameController controller = new GameController(lobby.model, lobby.lobbyID);
            
            for( Client c : lobby.clients.values() ) {
                mapping.put(c, controller);
            }
            server.addGameControllers(mapping);
        }
        update_client(nickname, new ServerResponseMessage(Response.Ok(JoinLobbyMessage.class.getSimpleName())));
    }
    
    /**
     * Search for all lobbies with the information given in info.
     * With null size return all the available lobby
     * Normal and recovery lobbies are merged.
     *
     * @param size the desired size of the lobby
     *
     * @return a list of lobbyView of all the lobbies that match info parameters
     */
    private static List<LobbyView> searchForLobbies(Integer size) {
        return lobbies.values()
                .stream()
                .filter((x) -> size == null || (size.equals(x.lobbySize())))
                .map(Lobby::getLobbyView)
                .toList();
    }
    
    /**
     * Check if a nickname is already taken, looking at both recovery and normal lobbies
     *
     * @param nickname Nickname to check
     *
     * @return True if the nickname is already present in any lobby
     */
    private static boolean nicknameTaken(String nickname) {
        return lobbies.values()
                .stream()
                .anyMatch((l) -> l.model.getNicknames().contains(nickname));
    }
    
}
