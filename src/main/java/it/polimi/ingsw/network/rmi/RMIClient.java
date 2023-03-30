package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements Client, Runnable {
    
    View view = new TUI();
    
    public RMIClient(Server server) throws RemoteException {
        super();
        initialize(server);
    }
    
    public RMIClient(Server server, int port) throws RemoteException {
        super(port);
        initialize(server);
    }
    
    public RMIClient(Server server, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        initialize(server);
    }
    
    private void initialize(Server server) throws RemoteException {
        int viewId = server.register(this);
        this.view.setViewID(viewId);
        
        view.addObserver((o, evt) -> {
            try {
                server.update(new ViewMessage(view), evt);
            } catch (RemoteException e) {
                System.err.println("Unable to update the server: " + e.getMessage() + ". Skipping the update...");
            }
        });
    }
    
    @Override
    public void update(GameView o, GameModel.Event evt) {
        view.update(o, evt);
    }
    
    @Override
    public void run() {
        view.run();
    }
    
}
