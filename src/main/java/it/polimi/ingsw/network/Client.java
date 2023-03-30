package it.polimi.ingsw.network;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.view.View;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    
    /**
     * Notify the client of a model change
     * @param o     The resulting model view
     * @param evt   The causing event
     */
    void update(GameView o, GameModel.Event evt) throws RemoteException;
    
}
