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
import it.polimi.ingsw.utils.mvc.ModelListener;
import it.polimi.ingsw.view.messages.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;


/**
 * Handles registration to multiple lobbies for concurrent play
 * LobbyController receives RequestLobby, RecoverLobby, CreateLobby and JoinLobby messages.
 * It always directly responds to each of these messages with a ServerResponseMessage, both in case of success and failure.
 */
public class LobbyController {
    
    private static LobbyController INSTANCE;
    
    private LocalServer server = null;
    
    /**
     * Client context derived from a received message
     * Can be used as a model listener, which simply calls the client's update method and logs messages/errors
     *
     * @param client   Client reference
     * @param nickname Nickname of the client
     * @param id       Client's id on the server
     */
    public record ClientContext(Client client, String nickname, int id) implements ModelListener {
        @Override
        public String toString() {
            return nickname + " , " + id;
        }
        
        public static ClientContext getCC(ViewMessage<?> msg) {
            return new ClientContext(getInstance().clientMapping.get(msg.getClientId()), msg.getPlayerNickname(),
                                     msg.getClientId());
        }
        
        @Override
        public void update(ModelMessage<?> m) {
            try {
                this.client.update(m);
                ServerLogger.messageLog(this.toString(), m);
            }
            catch( RemoteException e ) {
                ServerLogger.errorLog(e, "Client : " + this.toString());
            }
        }
    }
    
    /**
     * Lobby record, representing both recovery and normal lobbies
     *
     * @param model         Game model
     * @param clientIDs     List of client ids
     * @param personalGoals List of personal goals, null for recovery lobbies
     * @param lobbySize     Lobby size
     * @param lobbyID       Unique lobby id
     * @param isRecovery    True if the lobby is a recovery lobby
     */
    public record Lobby(
            GameModel model,
            List<Integer> clientIDs,
            List<Integer> personalGoals,
            int lobbySize,
            int lobbyID,
            boolean isRecovery
    ) {
        public boolean isEmpty() {
            if( isRecovery ) {
                return clientIDs.contains(-1);
            }else {
                return clientIDs.size() < lobbySize();
            }
        }
        
        public void addClient(ClientContext client) {
            if( isRecovery ) {
                int index = this.model.getNicknames().indexOf(client.nickname());
                clientIDs.set(index, client.id());
            }else {
                int index = this.clientIDs.size();
                clientIDs.add(client.id());
                this.model.addPlayer(client.nickname(), this.personalGoals.get(index));
            }
            
            try {
                this.model.addListener(client.nickname(), client);
            }
            catch( DuplicateListener e ) {
                ServerLogger.errorLog(e, "Client : " + client);
            }
        }
        
        public Lobby recoverLobby() {
            return new Lobby(model, clientIDs, personalGoals, lobbySize, lobbyID, false);
        }
        
        public LobbyView getLobbyView() {
            return new LobbyView(model.getNicknames(), lobbySize, lobbyID, isRecovery);
        }
    }
    
    // LobbyView is a serializable version of Lobby to be sent to the client for information
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
    
    // Lobbies are mapped by their unique lobbyID (recovery lobbies have their id's reset)
    private final HashMap<Integer, Lobby> lobbies = new HashMap<>();
    private int lobbyIDCounter = 0;
    
    // Clients are mapped by their unique clientID
    private final HashMap<Integer, Client> clientMapping = new HashMap<>();
    private int clientIDCounter = 0;
    
    /**
     * Init LobbyController by reading all the saved models from disk
     */
    private LobbyController() {
        for( GameModel model : ResourcesManager.getSavedModels() ) {
            Lobby lobby = new Lobby(
                    model,
                    new ArrayList<>(Collections.nCopies(model.getNumPlayers(), -1)),
                    null,
                    model.getNumPlayers(),
                    lobbyIDCounter,
                    true
            );
            
            // Save the model with the new id
            ResourcesManager.saveModel(model, lobbyIDCounter);
            
            lobbies.put(lobbyIDCounter, lobby);
            lobbyIDCounter++;
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
     * Register a client to the global lobby
     *
     * @param client Client to register
     *
     * @throws RemoteException Unable to set the client's id
     */
    public void register(Client client) throws RemoteException {
        client.setClientID(clientIDCounter);
        this.clientMapping.put(clientIDCounter, client);
        clientIDCounter++;
        
        ServerLogger.log("Registered new Client with id : " + clientIDCounter);
    }
    
    /**
     * End a game, removing the lobby from the tracked list of lobbies
     *
     * @param lobbyID Lobby to stop tracking
     */
    public void endGame(int lobbyID) {
        for( int clientID : lobbies.get(lobbyID).clientIDs ) {
            clientMapping.remove(clientID);
        }
        this.lobbies.remove(lobbyID);
        ResourcesManager.deleteModel(lobbyID);
        
        ServerLogger.log("Game ended, removed lobby : " + lobbyID);
    }
    
    /**
     * Return the list of lobbies to the client
     *
     * @param requestLobbyMessage Lobby parameters
     */
    @SuppressWarnings("unused")
    public void onMessage(RequestLobbyMessage requestLobbyMessage) {
        ClientContext.getCC(requestLobbyMessage)
                .update(new AvailableLobbyMessage(this.searchForLobbies(requestLobbyMessage.getPayload())));
    }
    
    /**
     * Add the client to a recovery lobby
     *
     * @param recoverLobbyMessage Lobby parameters
     */
    @SuppressWarnings("unused")
    public void onMessage(RecoverLobbyMessage recoverLobbyMessage) {
        ClientContext client = ClientContext.getCC(recoverLobbyMessage);
        Lobby lobby = lobbies.values()
                .stream()
                .filter((l) -> l.isRecovery && l.model.getNicknames().contains(client.nickname()))
                .findFirst()
                .orElse(null);
        
        if( lobby == null) {
            // lobby is unavailable/doesn't exist
            client.update(
                    new ServerResponseMessage(Response.LobbyUnavailable(RecoverLobbyMessage.class.getSimpleName())));
            return;
        }
        
        int index = lobby.model.getNicknames().indexOf(client.nickname());
        if ( lobby.clientIDs().get(index) != -1 ) {
            // username is already taken
            client.update(
                    new ServerResponseMessage(Response.NicknameTaken(RecoverLobbyMessage.class.getSimpleName())));
            return;
        }
        
        lobby.addClient(client);
        
        // check if the game needs to be started
        if( !lobby.isEmpty() ) {
            Map<Integer, GameController> mapping = new HashMap<>();
            GameController controller = new GameController(lobby.model, lobby.lobbyID);
            
            for( Integer clientID : lobby.clientIDs ) {
                mapping.put(clientID, controller);
            }
            
            // set the lobby as fully recovered
            Lobby newLobby = lobby.recoverLobby();
            lobbies.replace(lobby.lobbyID, newLobby);
            
            server.addGameController(mapping);
        }
        
        client.update(new ServerResponseMessage(Response.Ok(RecoverLobbyMessage.class.getSimpleName())));
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
     * @param message Lobby creation message
     */
    @SuppressWarnings("unused")
    public void onMessage(CreateLobbyMessage message) {
        ClientContext client = ClientContext.getCC(message);
        
        if( this.nicknameTaken(client.nickname()) ) {
            client.update(new ServerResponseMessage(Response.NicknameTaken(CreateLobbyMessage.class.getSimpleName())));
            return;
        }
        
        int lobbySize = message.getPayload();
        if( lobbySize < 2 || lobbySize > 4 ) {
            client.update(new ServerResponseMessage(Response.InvalidLobbySize()));
            return;
        }
        
        // initialize lobby and model
        int[] commonGoalIndices = randDistinctIndices(2);
        int[] personalGoalIndices = randDistinctIndices(lobbySize);
        Lobby lobby = new Lobby(
                new GameModel(lobbySize, commonGoalIndices[0], commonGoalIndices[1]),
                new ArrayList<>(),
                new ArrayList<>(Arrays.stream(personalGoalIndices).boxed().toList()),
                lobbySize,
                lobbyIDCounter,
                false
        );
        
        lobby.addClient(client);
        lobbies.put(lobbyIDCounter, lobby);
        lobbyIDCounter++;
        
        client.update(new ServerResponseMessage(Response.Ok(CreateLobbyMessage.class.getSimpleName())));
    }
    
    /**
     * Join a lobby with the supplied id, and if needed start the game
     *
     * @param message Description of lobby to join
     */
    @SuppressWarnings("unused")
    public void onMessage(JoinLobbyMessage message) {
        ClientContext client = ClientContext.getCC(message);
        Lobby lobby = lobbies.get(message.getPayload());
        
        if( lobby == null || !lobby.isEmpty() ) {
            client.update(new ServerResponseMessage(Response.LobbyUnavailable(JoinLobbyMessage.class.getSimpleName())));
            return;
        }
        
        if( this.nicknameTaken(message.getPlayerNickname()) ) {
            client.update(new ServerResponseMessage(Response.NicknameTaken(JoinLobbyMessage.class.getSimpleName())));
            return;
        }
        
        lobby.addClient(client);
        
        // check if the game needs to be started
        if( !lobby.isEmpty() ) {
            Map<Integer, GameController> mapping = new HashMap<>();
            GameController controller = new GameController(lobby.model, lobby.lobbyID);
            
            for( Integer clientID : lobby.clientIDs ) {
                mapping.put(clientID, controller);
            }
            server.addGameController(mapping);
        }
        client.update(new ServerResponseMessage(Response.Ok(JoinLobbyMessage.class.getSimpleName())));
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
    private List<LobbyView> searchForLobbies(Integer size) {
        return this.lobbies.values()
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
    private boolean nicknameTaken(String nickname) {
        return lobbies.values()
                .stream()
                .anyMatch((l) -> l.model.getNicknames().contains(nickname));
    }
    
}
