package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.LobbyController;
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

public class LocalClient extends UnicastRemoteObject implements Client {
    
    private final Server server;
    private final View view = new TUI();
    
    public LocalClient(Server server) throws RemoteException {
        super();
        this.server = server;
    }
    
    public LocalClient(Server server, int port) throws RemoteException {
        super(port);
        this.server = server;
    }
    
    public LocalClient(Server server, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        this.server = server;
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
        LobbyController.LoginInfo loginInfo = view.userLogin(lobbyInfo);
        
        if (loginInfo != null) {
            try {
                server.sendLoginInfo(loginInfo);
            } catch( RemoteException e ) {
                System.err.println("Unable to send login to server: " + e.getMessage() + ". Exiting...");
            }
        }
    }
    
    @Override
    public void update(GameView o, GameModel.Event evt) {
        view.update(o, evt);
    }
    
}
