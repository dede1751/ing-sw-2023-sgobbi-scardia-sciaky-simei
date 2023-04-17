package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    
    void setClientID(int clientID) throws RemoteException;
    
    /**
     * Notify the client of a model change
     * @param o     The resulting model view
     * @param evt   The causing event
     */
    void update(GameModelView o, GameModel.Event evt) throws RemoteException;
    
}
