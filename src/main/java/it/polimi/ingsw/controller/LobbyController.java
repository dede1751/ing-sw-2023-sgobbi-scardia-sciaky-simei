package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.exceptions.LoginException;
import it.polimi.ingsw.view.ViewMessage;

import java.rmi.RemoteException;
import java.util.*;


/**
 * Handles registration to multiple lobbies for concurrent play
 */
public class LobbyController {
    
    private static LobbyController INSTANCE;
    
    // lobbies must include mapping clientID->nickname to be able to "connect" the correct clients to the model
    public record Lobby(List<String> nicknames, List<Integer> clientIDs, int lobbySize, int lobbyID) {}
    
    // Lobbies are mapped by their unique lobbyID
    private final HashMap<Integer, Lobby> lobbies = new HashMap<>();
    private int lobbyIDCounter = 0;
    
    // Clients are mapped by their unique clientID
    private final HashMap<Integer, Client> clientMapping = new HashMap<>();
    private int clientIDCounter = 0;
    
    private LobbyController() {}
    
    public static LobbyController getInstance() {
        if ( INSTANCE == null ) {
            INSTANCE = new LobbyController();
        }
        
        return INSTANCE;
    }
    
    /**
     * Register a client to the global lobby
     * @param client            Client to register
     * @throws RemoteException  Unable to set the client's id
     */
    public void register(Client client) throws RemoteException {
        client.setClientID(clientIDCounter);
        this.clientMapping.put(clientIDCounter, client);
        clientIDCounter++;
    }
    
    /**
     * Check if a client is currently registered to the server
     * @param clientID Client's id
     * @return         true if it's registered, false otherwise
     */
    public boolean checkRegistration(int clientID) { return this.clientMapping.get(clientID) != null; }
    
    /**
     * End a game, removing the lobby from the tracked list of lobbies
     * @param lobbyID Lobby to stop tracking
     */
    public void endGame(int lobbyID) {
        this.lobbies.remove(lobbyID);
    }
    
    /**
     * Create a lobby from an appropriate ViewMessage
     * Only call when msg is associated with a CREATE_LOBBY action
     * @param msg Message received from view
     * @return    Created lobby ID
     */
    public void createLobby(ViewMessage msg) {
        Lobby newLobby = new Lobby(
                new ArrayList<>(List.of(msg.getNickname())),
                new ArrayList<>(List.of(msg.getClientID())),
                msg.getLobbySize(),
                lobbyIDCounter
        );
        lobbies.put(lobbyIDCounter, newLobby);
        lobbyIDCounter++;
    }
    
    /**
     * Join a lobby from an appropriate ViewMessage
     * Only call when msg is associated with a JOIN_LOBBY action
     * If a game needs to start, instantiate a controller and return the mapping to the server
     * @param msg Message received from view
     * @return    Map between clientIDs and the same gamecontroller, null if no game started
     */
    public Map<Integer, GameController> joinLobby(ViewMessage msg) throws RemoteException {
        if (this.noLobbyAvailable()) {
            this.clientMapping.remove(msg.getClientID());
            throw new RemoteException("No available lobby to join, please create one yourself!", new LoginException());
        }
        if (this.nicknameTaken(msg.getNickname())) {
            this.clientMapping.remove(msg.getClientID());
            throw new RemoteException("Nickname '" + msg.getNickname() + "' is already taken!", new LoginException());
        }
        
        // find a lobby that isn't full
        Lobby lobby = this.lobbies.values()
                .stream()
                .filter((l) -> l.nicknames.size() < l.lobbySize)
                .findFirst()
                .orElseThrow();
        
        lobby.nicknames.add(msg.getNickname());
        lobby.clientIDs.add(msg.getClientID());
        
        // check if the game needs to be started
        if ( lobby.nicknames.size() >= lobby.lobbySize ) {
            return initGame(lobby);
        } else {
            return null;
        }
    }
    
    private boolean noLobbyAvailable() {
        return lobbies.values()
                .stream()
                .noneMatch((l) -> l.nicknames.size() < l.lobbySize);
    }
    
    private boolean nicknameTaken(String nickname) {
        return lobbies.values()
                .stream()
                .anyMatch((l) -> l.nicknames.contains(nickname));
    }
    
    private int[] randDistinctIndices(int count) {
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
        
        for (int i = 0; i < lobby.lobbySize(); ++i) {
            int clientID = lobby.clientIDs().get(i);
            
            Client client = clientMapping.get(clientID);
            model.addObserver((o, evt) -> {
                try {
                    client.update(new GameModelView((GameModel) o), evt);
                } catch ( RemoteException e) {
                    System.err.println("Unable to update the client: " + e.getMessage() + ". Skipping the update...");
                }
            });
            
            model.addPlayer(lobby.nicknames().get(i), personalGoalIndices[i]);
        }
        
        Map<Integer, GameController> gameMapping = new HashMap<>();
        GameController controller = new GameController(model, lobby.lobbyID);
        
        for (Integer clientID: lobby.clientIDs) {
            gameMapping.put(clientID, controller);
        }
        
        return gameMapping;
    }
    
}
