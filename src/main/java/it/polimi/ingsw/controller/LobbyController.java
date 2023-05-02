package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messages.AvailableLobbyMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.utils.exceptions.DuplicateNickname;
import it.polimi.ingsw.utils.exceptions.NoPlayerWithNickname;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.view.messages.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Stream;


/**
 * Handles registration to multiple lobbies for concurrent play
 */
public class LobbyController {
    
    private static LobbyController INSTANCE;
    
    private LocalServer server = null;
    
    // lobbies must include mapping clientID->nickname to be able to "connect" the correct clients to the model
    public record Lobby(List<String> nicknames, List<Integer> clientIDs, int lobbySize, int lobbyID) {
        public boolean isEmpty() {
            return nicknames.size() < lobbySize();
        }
        public LobbyView getLobbyView(){
            return new LobbyView(nicknames, lobbySize, lobbyID, false);
        }
    }
    
    // RecoveryLobby are lobbies derived from models saved to disk. When the game restarts, they are converted back
    public record RecoveryLobby(List<String> nicknames, List<Integer> clientIDs, int lobbySize, int lobbyID, GameModel model) {
        public boolean isEmpty() { return clientIDs.contains(-1); }
        public Lobby getLobby() { return new Lobby(nicknames, clientIDs, lobbySize, lobbyID); }
        public LobbyView getLobbyView(){ return new LobbyView(nicknames, lobbySize, lobbyID, true); }
    }
    
    // LobbyView is a serializable version of Lobby to be sent to the client for information
    public record LobbyView(List<String> nicknames, int lobbySize, int lobbyID, boolean isRecovery) implements Serializable {
        @Override
        public String toString() {
            StringBuilder nickames = new StringBuilder();
            for( String nickname : this.nicknames() ) {
                nickames.append("\t").append(nickname).append("\n");
            }
            
            if ( isRecovery ) {
                return ("\n------RECOVERING LOBBY------\n") +
                       ("Players: \n")
                       + nickames;
            } else {
                return ("\n------LOBBY: " + this.lobbyID() + "------\n") +
                       ("Occupancy: [" + this.nicknames().size() + "/" + this.lobbySize() + "]\n")
                       + nickames;
            }
            
        }
    }
    
    // Lobbies are mapped by their unique lobbyID (recovery lobbies have their id's reset)
    private final HashMap<Integer, Lobby> lobbies = new HashMap<>();
    private final HashMap<Integer, RecoveryLobby> recoveryLobbies = new HashMap<>();
    private int lobbyIDCounter = 0;
    
    // Clients are mapped by their unique clientID
    private final HashMap<Integer, Client> clientMapping = new HashMap<>();
    private int clientIDCounter = 0;
    
    /**
     * Init LobbyController by reading all the saved models from disk
     */
    private LobbyController() {
        File dir = new File(ResourcesManager.recoveryDir);
        if( !dir.exists() && !dir.mkdir() ) {
            System.err.println("Unable to create recovery directory");
            return;
        }
        
        for (File file: Objects.requireNonNull(dir.listFiles()) ) {
            try {
                String modelJson = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(GameModel.class, new GameModel.ModelDeserializer())
                        .create();
                
                GameModel model = gson.fromJson(modelJson, GameModel.class);
                RecoveryLobby lobby = new RecoveryLobby(
                        model.getPlayers().stream().map(Player::getNickname).toList(),
                        new ArrayList<>(Collections.nCopies(model.getNumPlayers(), -1)),
                        model.getNumPlayers(),
                        lobbyIDCounter,
                        model
                );
                
                recoveryLobbies.put(lobbyIDCounter, lobby);
                lobbyIDCounter++;
                
            } catch( IOException e ) {
                System.err.println("Unable to read file " + file.getName());
            }
        }
    }
    
    /**
     * Get the singleton instance of the LobbyController
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
     * @param server Server reference
     */
    public void setServer(LocalServer server) {
        if (this.server == null) {
            this.server = server;
        }
    }
    
    /**
     * Register a client to the global lobby
     *
     * @param client Client to register
     * @throws RemoteException Unable to set the client's id
     */
    public void register(Client client) throws RemoteException {
        client.setClientID(clientIDCounter);
        this.clientMapping.put(clientIDCounter, client);
        clientIDCounter++;
    }
    
    /**
     * Check if a client is currently registered to the server
     *
     * @param clientID Client's id
     * @return true if it's registered, false otherwise
     */
    public boolean checkRegistration(int clientID) {
        return this.clientMapping.get(clientID) != null;
    }
    
    /**
     * TODO: remove clients from mapping
     * End a game, removing the lobby from the tracked list of lobbies
     *
     * @param lobbyID Lobby to stop tracking
     */
    public void endGame(int lobbyID) {
        this.lobbies.remove(lobbyID);
    }
    
    /**
     * Return the list of lobbies to the client
     * @param requestLobbyMessage Lobby parameters
     * @return Response to the client
     */
    @SuppressWarnings("unused")
    public Response onMessage(RequestLobbyMessage requestLobbyMessage) {
        Client c = clientMapping.get(requestLobbyMessage.getClientId());
        try {
            c.update(new AvailableLobbyMessage(this.searchForLobbies(requestLobbyMessage.getPayload())));
            return Response.Ok(RequestLobbyMessage.class.getSimpleName());
        } catch (RemoteException e) {
            return Response.ServerError(RequestLobbyMessage.class.getSimpleName());
        }
    }
    
    /**
     * Add the client to a recovery lobby
     * @param recoverLobbyMessage Lobby parameters
     * @return Response to the client
     */
    @SuppressWarnings("unused")
    public Response onMessage(RecoverLobbyMessage recoverLobbyMessage) {
        String nickname = recoverLobbyMessage.getPlayerNickname();
        RecoveryLobby lobby = recoveryLobbies.values()
                .stream()
                .filter((l) -> l.nicknames.contains(nickname))
                .findFirst()
                .orElse(null);
        
        if ( lobby == null ) {
            return Response.LobbyUnavailable(RecoverLobbyMessage.class.getSimpleName());
        }
        
        int index = lobby.nicknames.indexOf(nickname);
        lobby.clientIDs.set(index, recoverLobbyMessage.getClientId());
        
        // check if the game needs to be started
        if ( !lobby.isEmpty() ) {
            Map<Integer, GameController> mapping = resumeGame(lobby);
            server.addGameController(mapping);
        }
        return Response.Ok(RecoverLobbyMessage.class.getSimpleName());
    }
    
    /**
     * Create a new lobby
     * @param message Lobby creation message
     * @return Response to the client
     */
    @SuppressWarnings("unused")
    public Response onMessage(CreateLobbyMessage message) {
        String nickname = message.getPlayerNickname();
        if( this.nicknameTaken(nickname) ) {
            return Response.NicknameTaken(CreateLobbyMessage.class.getSimpleName());
        }
        int lobbySize = message.getPayload();
        if ( lobbySize < 2 || lobbySize > 4  ) {
            return new Response(-1, "Invalid Lobby Size", CreateLobbyMessage.class.getSimpleName());
        }
        
        Lobby newLobby = new Lobby(
                new ArrayList<>(List.of(nickname)),
                new ArrayList<>(List.of(message.getClientId())),
                lobbySize,
                lobbyIDCounter
        );
        lobbies.put(lobbyIDCounter, newLobby);
        lobbyIDCounter++;
        return Response.Ok(CreateLobbyMessage.class.getSimpleName());
    }
    
    /**
     * Join a lobby with the supplied id, and if needed start the game
     * @param message Description of lobby to join
     * @return Response to the client
     */
    @SuppressWarnings("unused")
    public Response onMessage(JoinLobbyMessage message) {
        Integer Iid = message.getPayload();
        int id = 0;
        if(Iid != null){
            id = Iid;
        }
        Lobby lobby = lobbies.get(id);
        
        if( lobby == null || !lobby.isEmpty() ) {
            return Response.LobbyUnavailable(JoinLobbyMessage.class.getSimpleName());
        }
        if( this.nicknameTaken(message.getPlayerNickname()) ) {
            return Response.NicknameTaken(JoinLobbyMessage.class.getSimpleName());
        }
        
        lobby.nicknames.add(message.getPlayerNickname());
        lobby.clientIDs.add(message.getClientId());
        
        // check if the game needs to be started
        if( !lobby.isEmpty() ) {
            Map<Integer, GameController> mapping = initGame(lobby);
            server.addGameController(mapping);
        }
        return Response.Ok(message.getClass().getSimpleName());
    }
    
    /**
     * Search for all lobbies with the information given in info.
     * With null size return all the available lobby
     * Normal and recovery lobbies are merged.
     * @param size the desired size of the lobby
     * @return a list of lobbyView of all the lobbies that match info parameters
     */
    private List<LobbyView> searchForLobbies(Integer size){
        Stream<LobbyView> lobbyStream = this.lobbies.values()
                .stream()
                .filter((x) -> size == null || (size.equals(x.lobbySize())))
                .map(Lobby::getLobbyView);
        
        Stream<LobbyView> recoveryStream = this.recoveryLobbies.values()
                .stream()
                .filter((x) -> size == null || (size.equals(x.lobbySize())))
                .map(RecoveryLobby::getLobbyView);
                
        return Stream.concat(lobbyStream, recoveryStream).toList();
    }
    
    /**
     * Check if a nickname is already taken, looking at both recovery and normal lobbies
     * @param nickname Nickname to check
     * @return True if the nickname is already present in any lobby
     */
    private boolean nicknameTaken(String nickname) {
        return lobbies.values()
                .stream()
                .anyMatch((l) -> l.nicknames.contains(nickname))
                || recoveryLobbies.values()
                       .stream()
                       .anyMatch((l) -> l.nicknames.contains(nickname));
    }
    
    private Map<Integer, GameController> resumeGame(RecoveryLobby lobby) {
        GameModel model = lobby.model;
        
        for( int i = 0; i < lobby.clientIDs.size(); ++i ) {
            int clientID = lobby.clientIDs.get(i);
            Client client = clientMapping.get(clientID);
            
            try {
                model.addClient(lobby.nicknames.get(i), client);
            } catch( DuplicateNickname | NoPlayerWithNickname ignored ) {}
        }
        
        Map<Integer, GameController> gameMapping = new HashMap<>();
        GameController controller = new GameController(model, lobby.lobbyID);
        
        for( Integer clientID : lobby.clientIDs ) {
            gameMapping.put(clientID, controller);
        }
        
        // convert the recovery lobby to a regular lobby
        Lobby newLobby = lobby.getLobby();
        recoveryLobbies.remove(lobby.lobbyID);
        lobbies.put(lobby.lobbyID, newLobby);
        
        return gameMapping;
    }
    
    private static int[] randDistinctIndices(int count) {
        return new Random().ints(0, 12)
                .distinct()
                .limit(count)
                .toArray();
    }
    
    private Map<Integer, GameController> initGame(Lobby lobby) {
        int[] commonGoalIndices = randDistinctIndices(2);
        int[] personalGoalIndices = randDistinctIndices(4);
        
        GameModel model = new GameModel(lobby.lobbySize(), commonGoalIndices[0], commonGoalIndices[1]);
        model.getBoard().refill(model.getTileBag());
        
        for( int i = 0; i < lobby.lobbySize(); ++i ) {
            int clientID = lobby.clientIDs().get(i);
            
            Client client = clientMapping.get(clientID);
            model.addPlayer(lobby.nicknames().get(i), personalGoalIndices[i]);
            try {
                model.addClient(lobby.nicknames().get(i), client);
            } catch( DuplicateNickname | NoPlayerWithNickname ignored ) {
            }
        }
        Map<Integer, GameController> gameMapping = new HashMap<>();
        GameController controller = new GameController(model, lobby.lobbyID);
        
        for( Integer clientID : lobby.clientIDs ) {
            gameMapping.put(clientID, controller);
        }
        
        return gameMapping;
    }
    
}
