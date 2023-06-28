package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.messages.AvailableLobbyMessage;
import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.LocalServer;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.utils.files.ServerLogger;
import it.polimi.ingsw.utils.mvc.ReflectionUtility;
import it.polimi.ingsw.view.messages.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;


/**
 * Handles registration to multiple lobbies for concurrent play. <br>
 * LobbyController receives RequestLobby, RecoverLobby, CreateLobby and JoinLobby messages. <br>
 * It always directly responds to each of these messages with a ServerResponseMessage, both in case of success and failure.<br>
 * LobbyController constitutes a single synchronization lock, through which all login actions have to go through,
 * and also single-handedly controls the controller mapping on the LocalServer.
 */
public class LobbyController {
    
    /**
     * Lobby record, representing both recovery and normal lobbies. <br>
     * Recovery lobbies are identified by the isRecovery flag, with the only difference is them not needing new players
     * being added to the model.
     *
     * @param model         Game model for the lobby
     * @param clients       List of clients in the lobby
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
        /**
         * Returns true if the lobby can still fit more clients
         *
         * @return True if the lobby can accept clients, false otherwise
         */
        public boolean isEmpty() {
            if( isRecovery ) {
                return clients.containsValue(null);
            }else {
                return clients.size() < lobbySize();
            }
        }
        
        /**
         * Add a client with the chosen nickname to the lobby. <br>
         * Takes care of including the client within the lobby's model.
         *
         * @param client   Client to add
         * @param nickname Nickname of the client
         */
        public void addClient(Client client, String nickname) {
            clients.put(nickname, client);
            
            if( !isRecovery ) {
                this.model.addPlayer(nickname, this.personalGoals.pop());
            }
            
            this.model.addListener(nickname, (msg) -> {
                try {
                    client.update(msg);
                    ServerLogger.messageLog(nickname, msg);
                }
                catch( RemoteException e ) {
                    // If the client is disconnected, remove it from the lobby and end the game for everyone
                    this.model.addListener(nickname, (m) -> {
                    }); // Stop this listener
                    LobbyController.getInstance().disconnectClient(this);
                    ServerLogger.errorLog(e, "Client disconnected: " + nickname);
                }
            });
        }
        
        /**
         * Transform a recovery lobby into a normal lobby. <br>
         * Used when all players have reconnected.
         *
         * @return A normal lobby
         */
        public Lobby recoverLobby() {
            return new Lobby(model, clients, personalGoals, lobbySize, lobbyID, false);
        }
        
        /**
         * Transform a Lobby into its serializable LobbyView.
         *
         * @return A LobbyView of the lobby
         */
        public LobbyView getLobbyView() {
            return new LobbyView(model.getNicknames(), lobbySize, lobbyID, isRecovery);
        }
    }
    
    /**
     * LobbyView is a serializable version of Lobby to be sent to the client for information. <br>
     * This is an object meant to be handled by the Client.
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
        
        /**
         * Check if the lobby is full
         *
         * @return True if the lobby is full
         */
        public boolean isFull() {
            return nicknames.size() == lobbySize;
        }
    }
    
    private static LobbyController INSTANCE;
    
    // Lobbies are mapped by their unique lobbyID (recovery lobbies have their id's reset)
    private static final HashMap<Integer, Lobby> lobbies = new HashMap<>();
    
    private LocalServer server = null;
    
    private Client client = null;
    
    /**
     * Init LobbyController by reading all the saved models from disk.
     */
    LobbyController() {
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
     * Get the singleton instance of the running LobbyController.
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
     * Set the server reference, only possible once. <br>
     * This is needed because the LobbyController has to pass mappings to the server once the game starts.
     *
     * @param server Server reference
     */
    public void setServer(LocalServer server) {
        if( this.server == null ) {
            this.server = server;
        }
    }
    
    /**
     * Disconnect a client when it's impossible to update it (ends the game for everyone). <br>
     * This is a failsafe called whenever a client is unreachable, meant to be called during the game when a specific
     * model listener fails to update its client. <br>
     * It notifies everyone else that the game is over and stops the model from receiving further updates.
     */
    private synchronized void disconnectClient(Lobby lobby) {
        lobby.model.notifyWinner(); // we can safely modify this since we are still sync'd on the controller
        lobby.model.setGameEnded(true); // force game end
        this.endGame(lobby.lobbyID());
    }
    
    /**
     * Quietly remove all records of a Lobby from the server. <br>
     * Gets rid of all the mappings and the saved model.<br>
     * Does not notify clients in any way.
     *
     * @param lobbyID Lobby to stop tracking
     */
    public synchronized void endGame(int lobbyID) {
        Lobby lobby = lobbies.get(lobbyID);
        if( lobby == null ) {
            return;
        }
        
        List<Client> clients = lobby.clients
                .values()
                .stream()
                .toList();
        
        this.server.removeGameControllers(clients);
        lobbies.remove(lobbyID);
        ResourcesManager.deleteModel(lobbyID);
        
        ServerLogger.log("Game ended, removed lobby : " + lobbyID);
    }
    
    /**
     * Forward a ViewMessage to the LobbyController.<br>
     * Executes the corresponding onMessage() method if it exists.<br>
     * This method is synchronized over the LobbyController instance only if it's a message it can handle.
     *
     * @param client Client that sent the message
     * @param msg    Message to forward
     *
     * @return True if the message was forwarded successfully, false otherwise
     */
    public boolean update(Client client, ViewMessage<?> msg) {
        if( !ReflectionUtility.hasMethod(this, "onMessage", msg) ) {
            return false;
        }
        
        synchronized(this) {
            this.client = client;
            try {
                ReflectionUtility.invokeMethod(this, "onMessage", msg);
            }
            catch( NoSuchMethodException ignored ) {
            } // impossible
        }
        return true;
    }
    
    /**
     * Simple update function to incorporate logging
     *
     * @param nickname Client nickname
     * @param m        Message to send
     */
    private void updateClient(String nickname, ModelMessage<?> m) {
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
        updateClient(msg.getPlayerNickname(), new AvailableLobbyMessage(lobbies));
    }
    
    /**
     * Add the client to a recovery lobby.<br>
     * If the lobby is full, start the game.
     *
     * @param msg Lobby parameters
     */
    @SuppressWarnings("unused")
    public void onMessage(RecoverLobbyMessage msg) {
        String nickname = msg.getPlayerNickname();
        if( nickname == null || nickname.equals("") ) {
            updateClient("No Nickname",
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
            updateClient(nickname, new ServerResponseMessage(
                    Response.LobbyUnavailable(RecoverLobbyMessage.class.getSimpleName())));
            return;
        }
        
        // username is already taken
        if( lobby.clients.get(nickname) != null ) {
            updateClient(nickname,
                         new ServerResponseMessage(Response.NicknameTaken(RecoverLobbyMessage.class.getSimpleName())));
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
            
            // set the lobby as fully recovered
            Lobby newLobby = lobby.recoverLobby();
            lobbies.replace(lobby.lobbyID, newLobby);
            
            server.addGameControllers(mapping);
        }
        
        updateClient(nickname, new ServerResponseMessage(Response.Ok(RecoverLobbyMessage.class.getSimpleName())));
    }
    
    private static int[] randDistinctIndices(int count) {
        return new Random().ints(0, 12)
                .distinct()
                .limit(count)
                .toArray();
    }
    
    /**
     * Create a new lobby.
     *
     * @param msg Lobby creation message
     */
    @SuppressWarnings("unused")
    public void onMessage(CreateLobbyMessage msg) {
        String nickname = msg.getPlayerNickname();
        if( nickname == null || nickname.equals("") ) {
            updateClient("No Nickname",
                         new ServerResponseMessage(Response.NicknameNull(CreateLobbyMessage.class.getSimpleName())));
            return;
        }
        
        if( nicknameTaken(nickname) ) {
            updateClient(nickname,
                         new ServerResponseMessage(Response.NicknameTaken(CreateLobbyMessage.class.getSimpleName())));
            return;
        }
        int lobbySize = msg.getPayload();
        if( lobbySize < 2 || lobbySize > 4 ) {
            updateClient(nickname, new ServerResponseMessage(Response.InvalidLobbySize()));
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
        
        updateClient(
                nickname,
                new ServerResponseMessage(Response.Ok(CreateLobbyMessage.class.getSimpleName())));
    }
    
    /**
     * Join the chosen lobby.<br>
     * If the lobby is full, start the game.
     *
     * @param msg Join Lobby Message
     */
    @SuppressWarnings("unused")
    public void onMessage(JoinLobbyMessage msg) {
        String nickname = msg.getPlayerNickname();
        Lobby lobby = lobbies.get(msg.getPayload());
        
        if( lobby == null || !lobby.isEmpty() ) {
            updateClient(nickname,
                         new ServerResponseMessage(Response.LobbyUnavailable(JoinLobbyMessage.class.getSimpleName())));
            return;
        }
        
        if( nickname == null || nickname.equals("") ) {
            updateClient(nickname,
                         new ServerResponseMessage(Response.NicknameNull(JoinLobbyMessage.class.getSimpleName())));
            return;
        }
        
        if( nicknameTaken(msg.getPlayerNickname()) ) {
            updateClient(nickname,
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
        updateClient(nickname, new ServerResponseMessage(Response.Ok(JoinLobbyMessage.class.getSimpleName())));
    }
    
    /**
     * Search for all lobbies of the given size.<br>
     * If size is null, return all lobbies.
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
    public static boolean nicknameTaken(String nickname) {
        return lobbies.values()
                .stream()
                .anyMatch((l) -> l.model.getNicknames().contains(nickname));
    }
    
}
