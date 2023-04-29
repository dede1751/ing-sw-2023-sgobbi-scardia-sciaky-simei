package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.messages.AvailableLobbyMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.exceptions.DuplicateNickname;
import it.polimi.ingsw.utils.exceptions.LoginException;
import it.polimi.ingsw.utils.exceptions.NoPlayerWithNickname;
import it.polimi.ingsw.view.messages.JoinLobby;
import it.polimi.ingsw.view.messages.JoinLobbyMessage;
import it.polimi.ingsw.view.messages.LobbyInformation;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;


/**
 * Handles registration to multiple lobbies for concurrent play
 */
public class LobbyController {
    
    private static LobbyController INSTANCE;
    
    // lobbies must include mapping clientID->nickname to be able to "connect" the correct clients to the model
    public record Lobby(List<String> nicknames, List<Integer> clientIDs, int lobbySize, int lobbyID, String lobbyName) {
        public boolean isEmpty() {
            return nicknames.size() < lobbySize();
        }
        public LobbyView getLobbyView(){
            return new LobbyView(nicknames, lobbySize, lobbyID);
        }
    }
    
    public record LobbyView(List<String> nicknames, int lobbySize, int lobbyID) implements Serializable {
        @Override
        public String toString() {
            StringBuilder nickames = new StringBuilder();
            for( String nickname : this.nicknames() ) {
                nickames.append("\t").append(nickname).append("\n");
            }
            return ("\n------LOBBY: " + this.lobbyID() + "------\n") +
                   ("Occupancy: [" + this.nicknames().size() + "/" + this.lobbySize() + "]\n")
                   + nickames;
        }
    }
    
    // Lobbies are mapped by their unique lobbyID
    private final HashMap<Integer, Lobby> lobbies = new HashMap<>();
    private int lobbyIDCounter = 0;
    
    // Clients are mapped by their unique clientID
    private final HashMap<Integer, Client> clientMapping = new HashMap<>();
    private int clientIDCounter = 0;
    
    private LobbyController() {}
    
    public static LobbyController getInstance() {
        if( INSTANCE == null ) {
            INSTANCE = new LobbyController();
        }
        
        return INSTANCE;
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
        client.update(new AvailableLobbyMessage(searchForLobbies(new LobbyInformation(null, null))));
    }
    
    /**
     * Check if a client is currently registered to the server
     *
     * @param clientID Client's id
     *
     * @return true if it's registered, false otherwise
     */
    public boolean checkRegistration(int clientID) {
        return this.clientMapping.get(clientID) != null;
    }
    
    
    /**
     * Return the reference to the client object corrisponding to the supplied id
     * @param clientID clientID to be returned
     * @return Client object corrisponding to the supplied id
     */
    
    public Client getClient(int clientID){
        return clientMapping.get(clientID);
    }
    
    
    /**
     * Search for available lobbies with the information given in info.
     * Any null parameters in info is ignored in the search.
     * @param info information on the lobbies
     * @return a list of lobbyView of all the available lobbies that match info parameters
     */
    public List<LobbyView> searchForLobbies(LobbyInformation info){
        return this.lobbies.values()
                .stream()
                .filter(Lobby::isEmpty)
                .filter((x) -> info.size() == null || (info.size().equals(x.lobbySize())))
                .filter((x) -> info.name() == null || (x.lobbyName().equals(info.name())))
                .map(Lobby::getLobbyView)
                .toList();
    }
    
    
    /**
     * End a game, removing the lobby from the tracked list of lobbies
     *
     * @param lobbyID Lobby to stop tracking
     */
    public void endGame(int lobbyID) {
        this.lobbies.remove(lobbyID);
    }
    
    /**
     * Create a new lobby
     *
     * @param info the initial information about the lobby: its name and size
     * @param firstPlayer the nickname of the first player
     * @param fpId the id of the first player
     * @return the id of the lobby
     */

    public int createLobby(LobbyInformation info, String firstPlayer, int fpId) {
        Lobby newLobby = new Lobby(
                new ArrayList<>(List.of(firstPlayer)),
                new ArrayList<>(List.of(fpId)),
                info.size(),
                lobbyIDCounter,
                info.name()
        );
        lobbies.put(lobbyIDCounter, newLobby);
        lobbyIDCounter++;
        return lobbyIDCounter - 1;
    }
    
    /**
     * Join a lobby from an appropriate ViewMessage
     * Only call when msg is associated with a JOIN_LOBBY action
     * If a game needs to start, instantiate a controller and return the mapping to the server
     *
     * @param msg Message received from view
     *
     * @return Map between clientIDs and the same gamecontroller, null if no game started
     */
    
    public Map<Integer, GameController> joinLobby(JoinLobbyMessage msg) throws RemoteException {
        
        JoinLobby info = msg.getPayload();
        if( this.noLobbyAvailable() ) {
            this.clientMapping.remove(msg.getClientId());
            throw new RemoteException("No available lobby to join, please create one yourself!", new LoginException());
        }
        if( this.nicknameTaken(msg.getPlayerNickname()) ) {
            this.clientMapping.remove(msg.getClientId());
            throw new RemoteException("Nickname '" + msg.getPlayerNickname() + "' is already taken!", new LoginException());
        }
        
        // find a lobby that isn't full
        Lobby lobby = this.lobbies.values()
                .stream()
                .filter(Lobby::isEmpty)
                .findFirst()
                .orElseThrow();
        
        lobby.nicknames.add(msg.getPlayerNickname());
        lobby.clientIDs.add(msg.getClientId());
        
        // check if the game needs to be started
        if( !lobby.isEmpty() ) {
            return initGame(lobby);
        }else {
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
        
        for( int i = 0; i < lobby.lobbySize(); ++i ) {
            int clientID = lobby.clientIDs().get(i);
            
            Client client = clientMapping.get(clientID);
            model.addPlayer(lobby.nicknames().get(i), personalGoalIndices[i]);
            try {
                model.addClient(lobby.nicknames().get(i), client);
            }
            catch( DuplicateNickname | NoPlayerWithNickname ignored ) {
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
