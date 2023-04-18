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
    
    private final LobbyController lobbyController = LobbyController.getInstance();
    
    private final Map<Integer, GameController> gameControllers = new HashMap<>();
    
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
        lobbyController.register(client);
    }
    
    @Override
    public synchronized void update(ViewMessage msg, View.Action evt) throws RemoteException {
        int clientID = msg.getClientID();
        if ( !lobbyController.checkRegistration(clientID) ) {
            throw new RemoteException("View is not registered to the server", new LoginException());
        }
        
        switch (evt) {
            case CREATE_LOBBY -> lobbyController.createLobby(msg);
            
            case JOIN_LOBBY -> {
                Map<Integer, GameController> mapping = lobbyController.joinLobby(msg);
                
                if ( mapping != null) {
                    this.gameControllers.putAll(mapping);
                }
            }
            
            default -> {
                GameController controller = gameControllers.get(clientID);
                
                if ( controller != null ) {
                    controller.update(msg, evt);
                } else {
                    throw new RemoteException("Ignoring View Events until game is started!");
                }
            }
        }
    }
    
}
