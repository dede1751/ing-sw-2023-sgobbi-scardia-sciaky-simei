package it.polimi.ingsw.network.socket;


import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ServerStub implements Server {
    
    String ip;
    int port;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    private Socket socket;
    
    public ServerStub(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    @Override
    public void register(Client client) throws RemoteException {
        try {
            this.socket = new Socket(ip, port);
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
        } catch (IOException e) {
            throw new RemoteException("Unable to connect to the server", e);
        }
        
        try {
            Integer clientID = (Integer) ois.readObject();
            client.setClientID(clientID);
        } catch (IOException e) {
            throw new RemoteException("Cannot receive lobby info from server", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize lobby info from server", e);
        }
    }
    
    @Override
    public void update(ViewMessage o, View.Action arg) throws RemoteException {
        try {
            oos.writeObject(o);
        } catch (IOException e) {
            throw new RemoteException("Cannot send message", e);
        }
        
        try {
            oos.writeObject(arg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send action", e);
        }
    }
    
    public void receive(Client client) throws RemoteException {
        GameModelView o;
        try {
            o = (GameModelView) ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive model view from server", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize model view from server", e);
        }
        
        GameModel.Event arg;
        try {
            arg = (GameModel.Event) ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive event from server", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize event from server", e);
        }
        
        client.update(o, arg);
    }
    
    public void close() throws RemoteException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket", e);
        }
    }
    
}
