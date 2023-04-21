package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.utils.exceptions.LoginException;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;
import it.polimi.ingsw.view.messages.CreateLobbyMessage;
import it.polimi.ingsw.view.messages.JoinLobby;
import it.polimi.ingsw.view.messages.JoinLobbyMessage;
import it.polimi.ingsw.view.messages.ViewMsg;

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
    public synchronized void update(ViewMessage msg, View.Action evt) throws RemoteException {
        int clientID = msg.getClientID();
        if( !lobbyController.checkRegistration(clientID) ) {
            throw new RemoteException("View is not registered to the server", new LoginException());
        }
        
        switch( evt ) {
            case CREATE_LOBBY -> lobbyController.createLobby(msg);
            
            case JOIN_LOBBY -> {
                Map<Integer, GameController> mapping = lobbyController.joinLobby(msg);
                
                if( mapping != null ) {
                    this.gameControllers.putAll(mapping);
                }
            }
            
            default -> {
                GameController controller = gameControllers.get(clientID);
                
                if( controller != null ) {
                    controller.update(msg, evt);
                }else {
                    throw new RemoteException("Ignoring View Events until game is started!");
                }
            }
        }
    }
    
    @Override
    public GameController.Response update(ViewMsg<?> message) throws RemoteException {
        int clientID = message.getClientId();
        if( !lobbyController.checkRegistration(clientID) ) {
            throw new RemoteException("View is not registered to the server", new LoginException());
        }
        try {
            Method m = this.getClass().getMethod("onMessage", message.getMessageType());
            return (GameController.Response) m.invoke(this, message);
        }catch( NoSuchMethodException e){
            GameController controller = gameControllers.get(clientID);
            if(controller != null){
                return controller.update(message);
            }else{
                throw new RemoteException("Ingoring view Events until game is started!");
            }
        }
        catch( IllegalAccessException e ) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return new GameController.Response(128, "Server is acting up, please be patient...");
        }catch( InvocationTargetException e ){
            e.printStackTrace();
            return new GameController.Response(127, e.getMessage());
        }
    }
    
    public GameController.Response onMessage(CreateLobbyMessage message) {
        int id = lobbyController.createLobby(message.getPayload(), message.getPlayerNickname(), message.getClientId());
        return new GameController.Response(id,
                                           "Created new Lobby with name : " + message.getPayload().name() +
                                           " and id : " + id);
    }
    
    public GameController.Response onMessage(JoinLobbyMessage message) throws RemoteException {
        Map<Integer, GameController> mapping = lobbyController.joinLobby(message);
        if( mapping != null){
            this.gameControllers.putAll(mapping);
            return GameController.Response.Ok();
        }
        //TODO proper message
        else return new GameController.Response(-1, "proper message");
    }
}
