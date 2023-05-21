package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;
import it.polimi.ingsw.utils.mvc.ReflectionUtility;
import it.polimi.ingsw.view.messages.ViewMessage;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalServer extends UnicastRemoteObject implements Server {
    
    private final Map<Integer, GameController> gameControllers = new ConcurrentHashMap<>();
    
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
    
    @Override
    public void register(Client client) throws RemoteException {
        LobbyController.getInstance().register(client);
    }
    
    /**
     * Add clientID->controller mapping to current map
     *
     * @param mapping client mapping to add
     */
    public void addGameController(Map<Integer, GameController> mapping) {
        gameControllers.putAll(mapping);
    }
    
    @Override
    public void update(ViewMessage<?> message) {
        LobbyController.ClientContext client = LobbyController.ClientContext.getCC(message);
        
        // Ignore messages from unregistered clients
        if( client.client() == null ) {
            return;
        }
        
        try {
            // First try to see if it's a method handled by the LobbyController
            ReflectionUtility.invokeMethod(client, LobbyController.getInstance(), "onMessage", message);
        }
        catch( NoSuchMethodException e ) {
            // Then, try forwarding it to a GameController
            GameController controller = gameControllers.get(client.id());
            
            if( controller != null ) {
                try {
                    ReflectionUtility.invokeMethod(client, controller, "onMessage", message);
                }
                catch( NoSuchMethodException f ) {
                    client.update(new ServerResponseMessage(
                            new Response(-1, "Illegal message, no operation defined. Refer to the network manual",
                                         message.getClass().getSimpleName())));
                }
            }else {
                client.update(new ServerResponseMessage(new Response(-1, "Ignoring view Events until game is started!",
                                                                     message.getClass().getSimpleName())));
            }
        }
        
    }
    
}
