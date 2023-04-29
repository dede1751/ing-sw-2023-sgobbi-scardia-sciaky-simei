package it.polimi.ingsw.network;

import it.polimi.ingsw.model.messages.ModelMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    
    void setClientID(int clientID) throws RemoteException;
    
    int getClientID() throws RemoteException;
    /**
     * Notify the client of a model change
     * @param msg   The object that contains the model changes information
     */
    void update(ModelMessage<?> msg) throws RemoteException;
    
}
