package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.utils.exceptions.LoginException;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LocalServer extends UnicastRemoteObject implements Server {
    
    private record ClientInfo(Client client, Integer lobbyID) {};
    
    private final Map<Integer, ClientInfo> clientMapping = new HashMap<>();
    
    private final LobbyController lobbyController = LobbyController.getInstance();
    
    private final Map<Integer, GameController> gameControllers = new HashMap<>();
    
    private int clientIDCounter = 0;
    
    public LocalServer() throws RemoteException {
        super();
    }
    
    public LocalServer(int port) throws RemoteException {
        super(port);
    }
    
    public LocalServer(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }
    
    @Override
    public synchronized void register(Client client) throws RemoteException {
        client.setClientID(clientIDCounter);
        this.clientMapping.put(clientIDCounter, new ClientInfo(client, null));
        clientIDCounter++;
    }
    
    private int[] randDistinctIndices(int count) {
        return new Random().ints(0, 12)
                .distinct()
                .limit(count)
                .toArray();
    }
    
    private void initGameController(int lobbyID) {
        int[] commonGoalIndices = randDistinctIndices(2);
        int[] personalGoalIndices = randDistinctIndices(4);
        
        LobbyController.Lobby lobby = lobbyController.getLobby(lobbyID);
        GameModel model = new GameModel(lobby.lobbySize(), commonGoalIndices[0], commonGoalIndices[1]);
        model.getBoard().refill(model.getTileBag());
        
        for (int i = 0; i < lobby.lobbySize(); ++i) {
            int clientID = lobby.clientIDs().get(i);
            
            ClientInfo clientInfo = clientMapping.get(clientID);
            model.addObserver((o, evt) -> {
                try {
                    clientInfo.client.update(new GameModelView((GameModel) o), evt);
                } catch ( RemoteException e) {
                    System.err.println("Unable to update the client: " + e.getMessage() + ". Skipping the update...");
                }
            });
            
            model.addPlayer(lobby.nicknames().get(i), personalGoalIndices[i]);
        }
        
        gameControllers.put(lobby.lobbyID(), new GameController(model, lobbyID));
    }
    
    private void addLobbyID(int clientID, int lobbyID) {
        ClientInfo info = this.clientMapping.get(clientID);
        this.clientMapping.replace(clientID, new ClientInfo(info.client, lobbyID));
    }
    
    @Override
    public synchronized void update(ViewMessage msg, View.Action evt) throws RemoteException {
        ClientInfo info = clientMapping.get(msg.getClientID());
        if ( info == null ) {
            throw new RemoteException("View is not registered to the server", new LoginException());
        }
        
        switch (evt) {
            case CREATE_LOBBY -> {
                int lobbyID = lobbyController.createLobby(msg);
                this.addLobbyID(msg.getClientID(), lobbyID);
            }
            
            case JOIN_LOBBY -> {
                if (!lobbyController.availableLobby()) {
                    this.clientMapping.remove(msg.getClientID());
                    throw new RemoteException("No available lobby to join, please create one yourself!", new LoginException());
                }
                if (!lobbyController.availableNickname(msg.getNickname())) {
                    this.clientMapping.remove(msg.getClientID());
                    throw new RemoteException("Nickname '" + msg.getNickname() + "' is already taken!", new LoginException());
                }
                
                int lobbyID = lobbyController.joinLobby(msg);
                this.addLobbyID(msg.getClientID(), lobbyID);
                
                if (lobbyController.isFull(lobbyID)) {
                    this.initGameController(lobbyID);
                }
            }
            
            default -> {
                GameController controller = gameControllers.get(info.lobbyID);
                
                if ( controller != null ) {
                    controller.update(msg, evt);
                } else {
                    throw new RemoteException("Ignoring View Events until game is started!");
                }
            }
        }
    }
    
}
