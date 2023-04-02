package it.polimi.ingsw.network.socket;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientSkeleton implements Client {
    
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    
    public ClientSkeleton(Socket socket) throws RemoteException {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create output stream", e);
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create input stream", e);
        }
    }
    
    @Override
    public void setViewID(int viewID) throws RemoteException {
    
    }
    
    @Override
    public void sendLobbyInfo(LobbyController.LobbyInfo info) throws RemoteException {
        try {
            oos.writeObject(info);
        } catch (IOException e) {
            throw new RemoteException("Cannot send lobby info", e);
        }
    }
    
    @Override
    public void update(GameView o, GameModel.Event arg) throws RemoteException {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(arg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
    }
    
    public void receive(Server server) throws RemoteException {
        ViewMessage o;
        try {
            o = (ViewMessage) ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive message from client", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize message from client", e);
        }
        
        View.Action arg;
        try {
            arg = (View.Action) ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive action from client", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize action from client", e);
        }
        
        server.update(o, arg);
    }
}
