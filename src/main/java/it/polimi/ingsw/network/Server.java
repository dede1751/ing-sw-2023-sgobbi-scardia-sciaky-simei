package it.polimi.ingsw.network;

import it.polimi.ingsw.view.messages.ViewMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface Server defines the methods that must be implemented by each server.
 * Each server must override the register and update methods to handle the client registration and the client actions.
 */
public interface Server extends Remote {
    
    /**
     * Notify the server of a client's message
     *
     * @param client  the client that sent the message
     * @param message the message sent by the client
     *
     * @throws RemoteException any connection error
     */
    void update(Client client, ViewMessage<?> message) throws RemoteException;
    
}
