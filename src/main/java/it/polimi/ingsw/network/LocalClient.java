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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class LocalClient is the main network interface for the client application. <br>
 * It communicates to either a LocalServer via RMI or a ServerStub via Socket.
 */
public class LocalClient extends UnicastRemoteObject implements Client {
    
    private final Server server;
    private final View view;
    
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    
    /**
     * Initialize a LocalClient object from server and view information.
     * @param server the server to connect to
     * @param view the view to connect to
     * @throws RemoteException if an error occurs while exporting the object
     */
    public LocalClient(Server server, View view) throws RemoteException {
        super();
        this.server = server;
        this.view = view;
    }
    
    /**
     * Initialize a LocalClient object from server, view and port information.
     * @param server the server to connect to
     * @param view the view to connect to
     * @param port the port to use for rmi communication
     * @throws RemoteException if an error occurs while exporting the object
     */
    public LocalClient(Server server, View view, int port) throws RemoteException {
        super(port);
        this.server = server;
        this.view = view;
    }
    
    /**
     * Initialize a LocalClient object from server, view, port, client socket factory and server socket factory information.
     * @param server the server to connect to
     * @param view the view to connect to
     * @param port the port to use for rmi communication
     * @param csf the client socket factory to use for rmi communication
     * @param ssf the server socket factory to use for rmi communication
     * @throws RemoteException if an error occurs while exporting the object
     */
    public LocalClient(Server server, View view, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        this.server = server;
        this.view = view;
    }
    
    @Override
    public void update(ModelMessage<?> msg) {
        threadPool.submit(() -> {
            try {
                ReflectionUtility.invokeMethod(view, "onMessage", msg);
                ClientLogger.messageLog(msg);
            }
            catch( NoSuchMethodException e ) {
                ClientLogger.errorLog(e, "Error invoking method onMessage" + msg.getClass().getSimpleName());
            }
        });
    }
    
    /**
     * Notify the server of a view's message. <br>
     * Updates are sent asynchronously as to not hang the client in case of a long server response time.
     *
     * @param msg the message sent by the view
     */
    public void update(ViewMessage<?> msg) {
        threadPool.submit(() -> {
            try {
                server.update(this, msg);
            }
            catch( RemoteException e ) {
                ClientLogger.errorLog(e, "Error notifying server, shutting down!");
                System.exit(1); // kill client when server becomes unreachable
            }
            
        });
    }
    
}
