package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;
import it.polimi.ingsw.utils.files.ServerLogger;
import it.polimi.ingsw.utils.mvc.ReflectionUtility;
import it.polimi.ingsw.view.messages.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalServer extends UnicastRemoteObject implements Server {
    
    private final Map<Client, GameController> gameControllers = new ConcurrentHashMap<>();
    
    public LocalServer() throws RemoteException {
        super();
        LobbyController.getInstance().setServer(this);
    }
    
    public LocalServer(int port) throws RemoteException {
        super(port);
        LobbyController.getInstance().setServer(this);
        
    }
    
    public LocalServer(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        LobbyController.getInstance().setServer(this);
    }
    
    /**
     * Add clientID->controller mapping to current map
     *
     * @param mapping client mapping to add
     */
    public void addGameController(Map<Client, GameController> mapping) {
        gameControllers.putAll(mapping);
    }
    
    @Override
    public void update(Client client, ViewMessage<?> message) {
        LobbyController lobbyController = LobbyController.getInstance();
        
        // First try to see if it's a method handled by the LobbyController
        if( ReflectionUtility.hasMethod(lobbyController, "onMessage", message) ) {
            synchronized(lobbyController) {
                lobbyController.setServedClient(client);
                try {
                    ReflectionUtility.invokeMethod(LobbyController.getInstance(), "onMessage", message);
                }
                catch( NoSuchMethodException ignored ) {
                } // impossible
            }
            return;
        }
        
        // Otherwise, try forwarding it to a GameController
        GameController controller = gameControllers.get(client);
        Response r = null;
        
        if( controller != null ) {
            try {
                ReflectionUtility.invokeMethod(controller, "onMessage", message);
            }
            catch( NoSuchMethodException e ) {
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
