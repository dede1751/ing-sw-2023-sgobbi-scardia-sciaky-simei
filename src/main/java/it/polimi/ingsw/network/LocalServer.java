package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.AvailableLobbyMessage;
import it.polimi.ingsw.utils.exceptions.LoginException;
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
    }
    
    public LocalServer(int port) throws RemoteException {
        super(port);
    }
    
    public LocalServer(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }
    
    @Override
    public synchronized void register(Client client) throws RemoteException {
        lobbyController.register(client);
    }
    
    @Override
    public Response update(ViewMessage<?> message) {
        int clientID = message.getClientId();
        
        // Ignore messages from unregistered clients
        if( !lobbyController.checkRegistration(clientID) ) {
            return new Response(1, "Client is not registered to the server", message.getClass().getSimpleName());
        }
        
        try {
            // First try to see if it's a method handled by the server/lobby controller
            Method m = this.getClass().getMethod("onMessage", message.getMessageType());
            return (Response) m.invoke(this, message);
            
        } catch( NoSuchMethodException e){
            // Then, try forwarding it to a game controller
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
    
    @SuppressWarnings("unused")
    public Response onMessage(RequestLobby requestLobby) {
        Client c = lobbyController.getClient(requestLobby.getClientId());
        try {
            c.update(new AvailableLobbyMessage(lobbyController.searchForLobbies(requestLobby.getPayload())));
            return Response.Ok(RequestLobby.class.getSimpleName());
        } catch (RemoteException e) {
            return Response.ServerError(RequestLobby.class.getSimpleName());
        }
    }
    
    @SuppressWarnings("unused")
    public Response onMessage(CreateLobbyMessage message) {
        try {
            int id = lobbyController.createLobby(message.getPayload(), message.getPlayerNickname(), message.getClientId());
            return Response.Ok(CreateLobbyMessage.class.getSimpleName());
        } catch( LoginException e ) {
            return new Response(-1, e.getMessage(), CreateLobbyMessage.class.getSimpleName());
        }
    }
    
    @SuppressWarnings("unused")
    public Response onMessage(JoinLobbyMessage message) {
        try {
            Map<Integer, GameController> mapping = lobbyController.joinLobby(message);
            if( mapping != null){
                this.gameControllers.putAll(mapping);
            }
            return Response.Ok(message.getClass().getSimpleName());
        } catch (LoginException e) {
            return new Response(-1, e.getMessage(), message.getClass().getSimpleName());
        }
    }
    
}
