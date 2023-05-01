package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.AvailableLobbyMessage;
import it.polimi.ingsw.view.messages.CreateLobbyMessage;
import it.polimi.ingsw.view.messages.JoinLobbyMessage;
import it.polimi.ingsw.view.messages.RequestLobby;
import it.polimi.ingsw.view.messages.ViewMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class LocalServer extends UnicastRemoteObject implements Server {
    
    private final LobbyController lobbyController = LobbyController.getInstance();
    
    private final Map<Integer, GameController> gameControllers = new HashMap<>();
    
    public LocalServer() throws RemoteException {
        super();
        lobbyController.setServer(this);
    }
    
    public LocalServer(int port) throws RemoteException {
        super(port);
        lobbyController.setServer(this);
        
    }
    
    public LocalServer(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
        lobbyController.setServer(this);
    }
    
    @Override
    public synchronized void register(Client client) throws RemoteException {
        lobbyController.register(client);
    }
    
    /**
     * Add clientID->controller mapping to current map
     * @param mapping client mapping to add
     */
    public void addGameController(Map<Integer, GameController> mapping) {
        gameControllers.putAll(mapping);
    }
    
    @Override
    public Response update(ViewMessage<?> message) {
        int clientID = message.getClientId();
        
        // Ignore messages from unregistered clients
        if( !lobbyController.checkRegistration(clientID) ) {
            return new Response(1, "Client is not registered to the server", message.getClass().getSimpleName());
        }
        
        try {
            // First try to see if it's a method handled by the LobbyController
            Method m = lobbyController.getClass().getMethod("onMessage", message.getMessageType());
            return (Response) m.invoke(lobbyController, message);
            
        } catch( NoSuchMethodException e){
            // Then, try forwarding it to a GameController
            GameController controller = gameControllers.get(clientID);
            
            if ( controller != null ){
                try {
                    Method m = controller.getClass().getMethod("onMessage", message.getMessageType());
                    return (Response) m.invoke(controller, message);
                } catch( NoSuchMethodException f ){
                    return new Response(-1, "Illegal message, no operation defined. Refere to the network manual", message.getClass().getSimpleName());
                } catch( InvocationTargetException | IllegalAccessException f ) {
                    return Response.ServerError(message.getClass().getSimpleName());
                }
            } else{
                return new Response(-1, "Ignoring view Events until game is started!", message.getClass().getSimpleName());
            }
            
        } catch( IllegalAccessException | InvocationTargetException e ) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return Response.ServerError(message.getClass().getSimpleName());
        }
    }
    
}
