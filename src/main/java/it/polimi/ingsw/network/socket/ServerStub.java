package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.files.ClientLogger;
import it.polimi.ingsw.view.messages.ViewMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ServerStub implements Server {
    
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    private Socket socket;
    
    private Client client;
    
    public ServerStub(String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            
            try {
                this.oos = new ObjectOutputStream(socket.getOutputStream());
            }
            catch( IOException e ) {
                ClientLogger.errorLog(e, "Cannot create output stream");
                System.exit(1);
            }
            
            try {
                this.ois = new ObjectInputStream(socket.getInputStream());
            }
            catch( IOException e ) {
                ClientLogger.errorLog(e, "Cannot create input stream");
                System.exit(1);
            }
        }
        catch( IOException e ) {
            ClientLogger.errorLog(e, "Unable to connect to server");
            System.exit(1);
        }
        
    }
    
    public void setClient(Client client) throws RemoteException { this.client = client; }
    
    @Override
    public void update(Client client, ViewMessage<?> message) throws RemoteException {
        try {
            oos.writeObject(message);
            oos.reset();
            oos.flush();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot send message", e);
        }
    }
    
    /**
     * Reads the input stream and forwards the messages to the client
     *
     * @throws RemoteException if the message cannot be read or is not a ModelMessage
     */
    public void receive() throws RemoteException {
        
        Object o;
        try {
            o = ois.readObject();
        }
        catch( EOFException e ) {
            return;
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot receive model view from server", e);
        }
        catch( ClassNotFoundException e ) {
            throw new RemoteException("Cannot deserialize model view from server", e);
        }
        
        try {
            this.client.update((ModelMessage<?>) o);
        }
        catch( ClassCastException e ) {
            throw new RemoteException("Server responded with illformed object", e);
        }
        
    }
    
    public void close() throws RemoteException {
        try {
            socket.close();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot close socket", e);
        }
    }
    
}
