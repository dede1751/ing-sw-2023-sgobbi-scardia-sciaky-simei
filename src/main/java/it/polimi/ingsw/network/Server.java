package it.polimi.ingsw.network;

import it.polimi.ingsw.view.messages.ViewMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    /**
     * Register a client to the server
     * @param client the client to register
     */
    void register(Client client) throws RemoteException;
    
    /**
     * Notify the server that a client has made a choice
     * @param message the message sent by the view
     */
    Response update(ViewMessage<?> message) throws RemoteException;
    
}
