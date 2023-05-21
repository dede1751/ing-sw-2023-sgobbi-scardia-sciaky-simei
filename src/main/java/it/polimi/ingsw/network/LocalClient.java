package it.polimi.ingsw.network;

import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.utils.files.ClientLogger;
import it.polimi.ingsw.utils.mvc.ReflectionUtility;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class LocalClient is the main network interface for the client application.
 * It communicates to either a LocalServer via RMI or a ServerStub via Socket.
 */
public class LocalClient extends UnicastRemoteObject implements Client {
    
    private final Server server;
    private final View view;
    
    public LocalClient(Server server, View view) throws RemoteException {
        super();
        this.server = server;
        this.view = view;
    }
    
    public LocalClient(Server server, View view, int port) throws RemoteException {
        super(port);
        this.server = server;
        this.view = view;
    }
    
    public LocalClient(Server server, View view, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        this.server = server;
        this.view = view;
    }
    
    @Override
    public void update(ModelMessage<?> msg) {
        ClientLogger.messageLog(msg);
        
        try {
            ReflectionUtility.invokeMethod(view, "onMessage", msg);
        }
        catch( NoSuchMethodException e ) {
            ClientLogger.errorLog(e, "Error invoking method onMessage" + msg.getClass().getSimpleName());
        }
    }
    
    /**
     * Notify the server of a view's message
     * @param msg the message sent by the view
     */
    public void update(ViewMessage<?> msg) {
        try {
            server.update(this, msg);
        }
        catch( RemoteException e ) {
            ClientLogger.errorLog(e, "Error notifying server");
        }
    }
    
}
