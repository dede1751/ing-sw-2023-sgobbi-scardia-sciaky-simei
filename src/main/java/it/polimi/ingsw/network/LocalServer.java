package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class LocalServer extends UnicastRemoteObject implements Server {
    
    private final LobbyController lobbyController = new LobbyController();
    
    private GameController gameController = null;
    
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
        if (gameController != null) {
            throw new RemoteException("Server is busy!");
        }
        
        lobbyController.addClient(client);

        if (lobbyController.isFull()) {
            gameController = lobbyController.initGame();
        }
    }
    
    @Override
    public void sendLoginInfo(LobbyController.LoginInfo info) throws RemoteException {
        if (!lobbyController.login(info)) {
            throw new RemoteException("Unable to login. Lobby is full!");
        }
    }
    
    @Override
    public synchronized void update(ViewMessage msg, View.Action evt) throws RemoteException {
        if ( gameController != null ) {
            gameController.update(msg, evt);
        } else {
            throw new RemoteException("Ignoring View Events until game is started!");
        }
    }
    
}
