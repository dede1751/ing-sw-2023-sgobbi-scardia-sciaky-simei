package it.polimi.ingsw.network;

import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.view.View;

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
    
    @Override
    public void setClientID(int clientID) throws RemoteException { this.view.setClientID(clientID); }
    
    @Override
    public int getClientID() throws RemoteException {
        return this.view.getClientID();
    }
    
    public void connectServer() {
        try {
            server.register(this);
        } catch( RemoteException e ) {
            System.err.println("Unable to register to server: " + e.getMessage() + ". Exiting...");
            System.exit(1);
        }
    }
    
    
    @Override
    public void update(ModelMessage<?> msg){
        view.update(msg);
    }
    
}
