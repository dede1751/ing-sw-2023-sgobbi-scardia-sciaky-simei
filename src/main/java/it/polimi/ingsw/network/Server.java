package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    /**
     * Register a client to the server
     * @param client the client to register
     */
    void register(Client client) throws RemoteException;
    
    /**
     * Notify the server that a client has made a choice
     * @param msg  the message sent by the client
     * @param evt  the action performed by the client
     */
    void update(ViewMessage msg, View.Action evt) throws RemoteException;
    
    void sendLoginInfo(LobbyController.LoginInfo info) throws RemoteException;

}
