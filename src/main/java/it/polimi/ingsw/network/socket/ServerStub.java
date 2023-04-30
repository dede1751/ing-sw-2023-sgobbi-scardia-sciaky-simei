package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.ReflectionUtility;
import it.polimi.ingsw.view.messages.ViewMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public class ServerStub implements Server {
    
    final String ip;
    final int port;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    private Socket socket;
    
    private Client clientContext;
    
    
    private final Object queueLock = new Object();
    private final Queue<Response> responseQueue = new LinkedList<>();
    
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
        catch( IOException e ) {
            throw new RemoteException("Unable to connect to the server", e);
        }
        
        try {
            Integer clientID = (Integer) ois.readObject();
            client.setClientID(clientID);
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot receive lobby info from server", e);
        }
        catch( ClassNotFoundException e ) {
            throw new RemoteException("Cannot deserialize lobby info from server", e);
        }
        
        this.clientContext = client;
    }
    
    @Override
    public Response update(ViewMessage<?> message) throws RemoteException {
        try {
            oos.writeObject(message);
            oos.reset();
            oos.flush();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot send message", e);
        }
        synchronized(queueLock) {
            while( responseQueue.peek() == null ||
                   !responseQueue.peek().Action().equals(message.getClass().getSimpleName()) ) {
                try {
                    queueLock.wait();
                }
                catch( InterruptedException ignored ) {
                }
            }
            return responseQueue.poll();
        }
    }
    
    public void receive() throws RemoteException {
        
        Object o;
        try {
            o = ois.readObject();
        }
        catch( IOException e ) {
            throw new RemoteException("Cannot receive model view from server", e);
        }
        catch( ClassNotFoundException e ) {
            throw new RemoteException("Cannot deserialize model view from server", e);
        }
        try {
            Method m = ReflectionUtility.GetMethod(this.getClass(), "onMessageReceived", o.getClass());
            m.invoke(this, o);
        }
        catch( NoSuchMethodException e ) {
            throw new RemoteException("Server responded with illformed object", e);
        }
        catch( InvocationTargetException | IllegalAccessException e ) {
            throw new RemoteException("Something that shouldn't happen did indeed happen, tough luck", e);
        }
        
    }
    
    @SuppressWarnings("unused")
    public void onMessageReceived(Response response) {
        synchronized(queueLock) {
            responseQueue.add(response);
            queueLock.notifyAll();
        }
    }
    
    @SuppressWarnings("unused")
    public <T extends ModelMessage<?>> void onMessageReceived(T mgs) throws RemoteException {
        this.clientContext.update(mgs);
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
