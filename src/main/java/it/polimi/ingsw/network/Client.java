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
     * Notify the client of a model message
     *
     * @param msg Message from the server or describing a model change
     * @throws RemoteException If an error occurs while updating the client
     */
    void update(ModelMessage<?> msg) throws RemoteException;
    
}
