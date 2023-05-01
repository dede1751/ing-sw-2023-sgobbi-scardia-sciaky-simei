package it.polimi.ingsw.network;

import it.polimi.ingsw.model.messages.ModelMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    
    /**
     * Sets the internal client ID. Should only be called once inside a Server thread.
     * The ID is unique and is used by the server to identify each client.
     * @param clientID The ID number assigned by the server
     */
    void setClientID(int clientID) throws RemoteException;
    
    
    /**
     * Returns the ID client
     */
    int getClientID() throws RemoteException;
    
    /**
     * Notify the client of a model change
     * @param msg   The object that contains the model changes information
     */
    void update(ModelMessage<?> msg) throws RemoteException;
}
