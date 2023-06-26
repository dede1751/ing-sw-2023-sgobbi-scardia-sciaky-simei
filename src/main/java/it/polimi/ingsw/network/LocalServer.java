package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;
import it.polimi.ingsw.utils.files.ServerLogger;
import it.polimi.ingsw.view.messages.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class LocalServer is the main network interface for the server application. <br>
 * It communicates to either a LocalClient via RMI or a ClientStub via Socket. <br>
 * This class acts as a router for messages to either the LobbyController or a specific GameController.
 */
public class LocalServer extends UnicastRemoteObject implements Server {
    
    private final Map<Client, GameController> gameControllers = new ConcurrentHashMap<>();
    
    /**
     * Initialize a LocalServer object.
     *
     * @throws RemoteException if an error occurs while exporting the object
     */
    public LocalServer() throws RemoteException {
        super();
        LobbyController.getInstance().setServer(this);
    }
    
    /**
     * Initialize a LocalServer object from port information.
     *
     * @param port the port to use for rmi communication
     * @throws RemoteException if an error occurs while exporting the object
     */
    public LocalServer(int port) throws RemoteException {
        super(port);
        LobbyController.getInstance().setServer(this);
        
    }
    
    /**
     * Initialize a LocalServer object from port and client socket factory information.
     *
     * @param port the port to use for rmi communication
     * @param csf  the client socket factory to use for rmi communication
     * @param ssf the server socket factory to use for rmi communication
     * @throws RemoteException if an error occurs while exporting the object
     */
    public LocalServer(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        LobbyController.getInstance().setServer(this);
    }
    
    /**
     * Add clientID->controller mapping to current map. <br>
     * Called by the LobbyController each time a new game starts. <br>
     * Synchronization is over the lobby controller instance. (the lock is redundant)
     *
     * @param mapping client mapping to add
     */
    public void addGameControllers(Map<Client, GameController> mapping) {
        synchronized( LobbyController.getInstance() ) {
            gameControllers.putAll(mapping);
        }
    }
    
    /**
     * Remove controller mapping from current map for the given clients. <br>
     * Called by the LobbyController each time a game ends. <br>
     * Synchronization is over the lobby controller instance. (the lock is redundant)
     *
     * @param clients clients to remove
     */
    public void removeGameControllers(List<Client> clients) {
        synchronized( LobbyController.getInstance() ) {
            clients.forEach(gameControllers.keySet()::remove);
        }
    }
    
    @Override
    public void update(Client client, ViewMessage<?> message) {
        
        // First try to see if it's a method handled by the LobbyController
        if( LobbyController.getInstance().update(client, message) ) {
            return;
        }
        
        // Otherwise, try forwarding it to a GameController
        GameController controller = gameControllers.get(client);
        Response r = null;
        
        if( controller != null ) {
            if( !controller.update(message) ) {
                r = new Response(-1, "Illegal message, no operation defined. Refer to the network manual",
                                 message.getClass().getSimpleName());
            }
        }else {
            r = new Response(-1, "Ignoring view Events until game is started!", message.getClass().getSimpleName());
        }
        
        // If needed, send error message to client
        if( r != null ) {
            try {
                client.update(new ServerResponseMessage(r));
            }
            catch( RemoteException e ) {
                ServerLogger.errorLog(e);
            }
        }
        
    }
    
}
