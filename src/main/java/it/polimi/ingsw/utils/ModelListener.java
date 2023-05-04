package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.messages.ModelMessage;

import java.rmi.RemoteException;

/**
 * Interface ModelListener defines the methods that must be implemented by each model listener.
 */
public interface ModelListener {
    
    void update(ModelMessage<?> msg) throws RemoteException;
    
}
