package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class LocalClient extends UnicastRemoteObject implements Client {
    
    private final Server server;
    private final View view;
    
    public LocalClient(Server server, View view) throws RemoteException {
        super();
        this.server = server;
        this.view = view;
    }
    
    public LocalClient(Server server, View view, int port) throws RemoteException {
        super(port);
        this.server = server;
        this.view = view;
    }
    
    public LocalClient(Server server, View view, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        this.server = server;
        this.view = view;
    }
    
    public void setViewID(int viewID) throws RemoteException { this.view.setViewID(viewID); }
    
    public void connectServer() {
        try {
            System.out.println("Connecting to server. This may take a while... ");
            server.register(this);
        } catch( RemoteException e ) {
            System.err.println("Unable to register to server: " + e.getMessage() + ". Exiting...");
            System.exit(1);
        }
        
        view.addObserver((o, evt) -> {
            try {
                server.update(new ViewMessage(view), evt);
            } catch (RemoteException e) {
                System.err.println("Unable to update the server: " + e.getMessage() + ". Skipping the update...");
            }
        });
    }
    
    @Override
    public void sendLobbyInfo(LobbyController.LobbyInfo lobbyInfo) throws RemoteException {
        if ( lobbyInfo.lobbyState() == LobbyController.State.FULL_LOBBY ) {
            System.err.println("Lobby is full. Exiting...");
            System.exit(1);
        }
        
        try {
            server.sendLoginInfo(view.userLogin(lobbyInfo));
        } catch( RemoteException e ) {
            System.err.println("Unable to send login to server: " + e.getMessage() + ". Exiting...");
        }
    }
    
    @Override
    public void update(GameModelView o, GameModel.Event evt) {
        view.update(o, evt);
    }
    
}
