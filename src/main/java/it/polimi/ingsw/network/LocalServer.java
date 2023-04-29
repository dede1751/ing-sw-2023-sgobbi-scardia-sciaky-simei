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
    
    //TODO change RemoteException to responses;
    @Override
    public Response update(ViewMessage<?> message) throws RemoteException {
        int clientID = message.getClientId();
        if( !lobbyController.checkRegistration(clientID) ) {
            throw new RemoteException("Client is not registered to the server", new LoginException());
        }
        try {
            Method m = this.getClass().getMethod("onMessage", message.getMessageType());
            return (Response) m.invoke(this, message);
        }catch( NoSuchMethodException e){
            GameController controller = gameControllers.get(clientID);
            if(controller != null){
                try {
                    return controller.update(message);
                }catch( NoSuchMethodException f ){
                    return new Response(-1, "Illegal message, no operation defined. Refere to the network manual");
                }
            }else{
                return new Response(-1, "Ignoring view Events until game is started!");
            }
        }
        catch( IllegalAccessException e ) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return new Response(128, "Server is acting up, please be patient...");
        }catch( InvocationTargetException e ){
            e.printStackTrace();
            return new Response(127, e.getMessage());
        }
    }
    
    public Response onMessage(CreateLobbyMessage message) {
        int id = lobbyController.createLobby(message.getPayload(), message.getPlayerNickname(), message.getClientId());
        return new Response(id,
                                           "Created new Lobby with name : " + message.getPayload().name() +
                                           " and id : " + id);
    }
    
    public Response onMessage(JoinLobbyMessage message) throws RemoteException {
        Map<Integer, GameController> mapping = lobbyController.joinLobby(message);
        if( mapping != null){
            this.gameControllers.putAll(mapping);
            return Response.Ok();
        }
        //TODO proper message
        else return new Response(-1, "proper message");
    }
    
    //FIXME sketchy
    public Response onMessage(RequestLobby requestLobby) throws RemoteException{
        if( lobbyController.checkRegistration(requestLobby.getClientId()) ){
            Client c = lobbyController.getClient(requestLobby.getClientId());
            c.update(new AvailableLobbyMessage(lobbyController.searchForLobbies(requestLobby.getPayload())));
            return Response.Ok();
        } else{
            throw new RemoteException("Client is not registered to the server");
        }
    
    }
    
    
    
    
}
