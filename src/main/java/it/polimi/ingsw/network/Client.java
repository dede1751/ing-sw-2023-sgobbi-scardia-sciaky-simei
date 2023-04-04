package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Client extends Remote {
    
    void setViewID(int viewID) throws RemoteException;
    
    /**
     * Notify the client of a model change
     * @param o     The resulting model view
     * @param evt   The causing event
     */
    void update(GameView o, GameModel.Event evt) throws RemoteException;
    
    void sendLobbyInfo(LobbyController.LobbyInfo info) throws RemoteException;
    
}
