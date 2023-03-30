package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements Server {
    
    private GameModel model;
    
    private GameController controller;
    
    private int clientIDCounter = 0;
    
    public RMIServer() throws RemoteException {
        super();
    }
    
    public RMIServer(int port) throws RemoteException {
        super(port);
    }
    
    public RMIServer(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }
    
    @Override
    public int register(Client client) {
        this.model = new GameModel(4, 0, 0);
        this.model.addObserver((o, evt) -> {
            try {
                client.update(new GameView(model), evt);
            } catch (RemoteException e) {
                System.err.println("Unable to update the client: " + e.getMessage() + ". Skipping the update...");
            }
        });
        
        this.clientIDCounter += 1;
        this.controller = new GameController(model, client, this.clientIDCounter);
        
        return this.clientIDCounter;
    }
    
    @Override
    public void update(ViewMessage msg, View.Action evt) {
        this.controller.update(msg, evt);
    }
    
}
