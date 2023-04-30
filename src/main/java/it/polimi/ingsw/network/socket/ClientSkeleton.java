package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.messages.ViewMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientSkeleton implements Client {
    
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    
    private int clientID = -1;
    
    public ClientSkeleton(Socket socket) throws RemoteException {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch( IOException e ) {
            throw new RemoteException("Cannot create output stream", e);
        }
        
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch( IOException e ) {
            throw new RemoteException("Cannot create input stream", e);
        }
    }
    
    @Override
    public void setClientID(int clientID) throws RemoteException {
        try {
            if( this.clientID == -1 ) {
                oos.writeObject(clientID);
                oos.reset();
                oos.flush();
                this.clientID = clientID;
            } else {
                throw new RemoteException("ID already setted for this client");
            }
        } catch( IOException e ) {
            throw new RemoteException("Cannot send client id", e);
        }
    }
    
    public void receive(Server server) throws RemoteException {
        ViewMessage<?> message;
        Response response;
        
        try {
            message = (ViewMessage<?>) ois.readObject();
            response = server.update(message);
        } catch( IOException e ) {
            throw new RemoteException("Cannot receive message from client", e);
        } catch( ClassNotFoundException e ) {
            throw new RemoteException("Sent message doesn't have the correct type", e);
        }
        
        try {
            oos.writeObject(response);
            oos.reset();
            oos.flush();
        } catch( IOException e ) {
            throw new RemoteException("Cannot send response", e);
        }
    }
    
    @Override
    public void update(ModelMessage<?> msg) throws RemoteException {
        try {
            oos.writeObject(msg);
            oos.reset();
            oos.flush();
        } catch( IOException e ) {
            throw new RemoteException("Cannot send message", e);
        }
    }
    
}
