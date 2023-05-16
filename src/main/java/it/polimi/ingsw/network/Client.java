package it.polimi.ingsw.network;

import it.polimi.ingsw.model.messages.ModelMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface Client defines the methods that must be implemented by each client.
 * Each client must override the update method to handle the model changes received from the server
 */
public interface Client extends Remote {
    
    /**
     * Sets the internal client ID. Should only be called once inside a Server thread.
     * The ID is unique and is used by the server to identify each client.
     *
     * @param clientID The ID number assigned by the server
     * @throws RemoteException If the clientID cannot be set
     */
    void setClientID(int clientID) throws RemoteException;
    
    /**
     * Notify the client of a model message
     *
     * @param msg Message from the server or describing a model change
     * @throws RemoteException If an error occurs while updating the client
     */
    void update(ModelMessage<?> msg) throws RemoteException;
    
}
