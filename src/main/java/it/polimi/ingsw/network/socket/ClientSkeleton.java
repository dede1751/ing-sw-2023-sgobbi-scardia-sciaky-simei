package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.network.Client;
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
    
    /**
     * Reads the input stream for ViewMessages and forwards them to the server
     *
     * @param server the server to forward the messages to
     *
     * @throws RemoteException if the message cannot be read or is not a ViewMessage
     */
    public void receive(Server server) throws RemoteException {
        
        try {
            server.update(this, (ViewMessage<?>) ois.readObject());
        }
        catch( ClassNotFoundException | ClassCastException e ) {
            throw new RemoteException("Sent message doesn't have the correct type", e);
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot read message from client", e);
        }
        
    }
    
    @Override
    public void update(ModelMessage<?> msg) throws RemoteException {
        try {
            oos.writeObject(msg);
            oos.reset();
            oos.flush();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot send message", e);
        }
    }
    
}
