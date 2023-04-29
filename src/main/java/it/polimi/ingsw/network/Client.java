package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.model.messages.ModelMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Client extends Remote {
    
    void setClientID(int clientID) throws RemoteException;
    
    int getClientID() throws RemoteException;
    /**
     * Notify the client of a model change
     * @param o     The resulting model view
     * @param evt   The causing event
     */
    void update(GameModelView o, GameModel.Event evt) throws RemoteException;
    
    void update(ModelMessage<?> msg) throws RemoteException;
    
}
