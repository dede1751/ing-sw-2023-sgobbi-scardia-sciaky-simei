package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.messages.*;

import java.rmi.RemoteException;
import java.util.List;


/**
 * Abstract class View defines the methods that must be implemented by each view.
 * Each view must override the onMessage methods to handle the messages received from the server
 * Methods to notify the server are already defined.
 */
public abstract class View implements Runnable {
    
    protected final LocalModel model = LocalModel.INSTANCE;
    
    protected int clientID;
    
    protected String nickname;
    
    protected List<LobbyController.LobbyView> lobbies;
    
    /**
     * Set the client's ID. This method is called by the server when the client connects.
     * @param clientID The new client's ID
     */
    public void setClientID(int clientID) { this.clientID = clientID; }
    
    /**
     * Get the client's ID
     * @return The client's ID
     */
    public int getClientID() { return this.clientID; }
    
    /**
     * Set the client's nickname. This method needs to be called by the view.
     * The client's nickname is used by the server to determine who is playing.
     * @param nickname The new nickname
     */
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    /**
     * Get the client's nickname
     * @return The client's nickname
     */
    public String getNickname() { return nickname; }
    
    @SuppressWarnings("unused")
    public abstract void onMessage(BoardMessage msg);
    @SuppressWarnings("unused")
    public abstract void onMessage(AvailableLobbyMessage msg);
    @SuppressWarnings("unused")
    public abstract void onMessage(EndGameMessage msg);
    @SuppressWarnings("unused")
    public abstract void onMessage(StartGameMessage msg);
    
    
    private Server server;
    
    /**
     * Set the view's server. This method is set at client startup.
     * @param server Upstream server
     */
    public void setServer(Server server){ this.server = server; }
    
    private <T extends ViewMessage<?>> Response notifyServer(T msg){
        try{
            return server.update(msg);
        }catch( RemoteException e ){
            e.printStackTrace(System.err);
            return new Response(1, e.getMessage(), msg.getClass().getSimpleName());
        }
    }
    
    /**
     * Request the lobby list from the server
     * @param info Lobby filters
     * @return The server's response
     */
    protected Response notifyRequestLobby(LobbyInformation info){
        return notifyServer(new RequestLobby(info, this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's request to create a lobby
     * @param info The new lobby's information
     * @return The server's response
     */
    protected Response notifyCreateLobby(LobbyInformation info){
        return notifyServer(new CreateLobbyMessage(info, this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's request to join a lobby
     * @param info The lobby to join's information
     * @return The server's response
     */
    protected Response notifyJoinLobby(JoinLobby info){
        return notifyServer(new JoinLobbyMessage(info, this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's public chat message
     * @param message Message contents
     * @return The server's response
     */
    protected Response notifyChatMessage(String message){
        return notifyServer(new ChatMessage(message, this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's private chat message
     * @param message Message contents
     * @param dst Message recipient
     * @return The server's response
     */
    protected Response notifyChatMessage(String message, String dst){
        return notifyServer(new ChatMessage(message, this.nickname, dst, this.clientID));
    }
    
    /**
     * Notify the server of a client move
     * @param move A move, represented by the selected tiles and how to insert them in the shelf
     * @return The server's response
     */
    protected Response notifyMove(Move move){
        return notifyServer(new MoveMessage(move, this.nickname, this.clientID));
    }
    
    protected Response notifyDebugMessage(String info){
        return notifyServer(new DebugMessage(info, this.nickname, this.clientID));
    }
    
}
