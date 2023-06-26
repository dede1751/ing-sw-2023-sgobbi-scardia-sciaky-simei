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

/**
 * ClientSkeleton used by a Socket Server to simulate RMI communication with the client.
 */
public class ClientSkeleton implements Client {
    
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    
    
    /**
     * Initialize a ClientSkeleton object with a Socket object
     *
     * @param socket Socket object
     *
     * @throws RemoteException If an error occurs while creating the In/Out streams
     */
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
    
    /**
     * Send a ModelMessage object to the remote client via TCP-IP socket. <br>
     * Default java serialization is used.
     *
     * @param msg Message from the server or describing a model change
     *
     * @throws RemoteException If an error occur during the TCP-IP communication
     */
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
