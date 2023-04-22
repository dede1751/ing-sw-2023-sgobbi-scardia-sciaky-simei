package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.messages.ViewMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public class ClientSkeleton implements Client {
    
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    
    
    public ClientSkeleton(Socket socket) throws RemoteException {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot create output stream", e);
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot create input stream", e);
        }
    }
    
    @Override
    public void setClientID(int clientID) throws RemoteException {
        try {
            oos.writeObject(clientID);
            oos.reset();
            oos.flush();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot send client id", e);
        }
    }
    
    @Override
    public void setAvailableLobbies(List<LobbyController.LobbyView> lobbies) throws RemoteException {
        try {
            oos.writeObject(lobbies);
            oos.reset();
            oos.flush();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot send lobby list", e);
        }
    }
    
    @Override
    public void update(GameModelView o, GameModel.Event arg) throws RemoteException {
        try {
            oos.writeObject(o);
            oos.reset();
            oos.flush();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot send model view", e);
        }
        try {
            oos.writeObject(arg);
            oos.reset();
            oos.flush();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot send event", e);
        }
    }
    
    /*
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
    }*/
    public void receive(Server server) throws RemoteException {
        ViewMsg<?> message;
        Response response;
        try {
            message = (ViewMsg<?>) ois.readObject();
            response = server.update(message);
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot receive message from client", e);
        }
        catch( ClassNotFoundException e ) {
            throw new RemoteException("Sent message doesn't have the correct type", e);
        }
        try {
            oos.writeObject(response);
            oos.reset();
            oos.flush();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot send response", e);
        }
    }
    
    
}
