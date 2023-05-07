package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.exceptions.InvalidStringException;
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
     *
     * @param clientID The new client's ID
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
    
    /**
     * Get the client's ID
     *
     * @return The client's ID
     */
    public int getClientID() {
        return this.clientID;
    }
    
    /**
     * Set the client's nickname. This method needs to be called by the view.
     * The client's nickname is used by the server to determine who is playing.
     *
     * @param nickname The new nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    /**
     * Get the client's nickname
     *
     * @return The client's nickname
     */
    public String getNickname() {
        return nickname;
    }
    
    @SuppressWarnings("unused")
    
    public abstract void onMessage(BoardMessage msg);
    
    
    @SuppressWarnings("unused")
    public abstract void onMessage(AvailableLobbyMessage msg);
    
    @SuppressWarnings("unused")
    public abstract void onMessage(EndGameMessage msg);
    
    @SuppressWarnings("unused")
    public abstract void onMessage(StartGameMessage msg);
    
    public abstract void onMessage(ServerResponseMessage msg);
    
    public abstract void onMessage(ShelfMessage msg);
    
    public abstract void onMessage(IncomingChatMessage msg);
    
    public abstract void onMessage(UpdateScoreMessage msg);
    
    public abstract void onMessage(CommonGoalMessage msg);
    
    public abstract void onMessage(CurrentPlayerMessage msg);
    
    
    private Server server;
    
    /**
     * Set the view's server. This method is set at client startup.
     *
     * @param server Upstream server
     */
    public void setServer(Server server) {
        this.server = server;
    }
    
    private <T extends ViewMessage<?>> void notifyServer(T msg) {
        try {
            server.update(msg);
        }
        catch( RemoteException e ) {
            System.err.println("Error notifying server: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
    
    /**
     * Request the lobby list from the server
     *
     * @param size Desired lobby size
     */
    protected void notifyRequestLobby(Integer size) {
        notifyServer(new RequestLobbyMessage(size, this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's request to join a recovering lobby
     */
    protected void notifyRecoverLobby() {
        notifyServer(new RecoverLobbyMessage(this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's request to create a lobby
     *
     * @param size The new lobby's number of player
     */
    protected void notifyCreateLobby(Integer size) {
        notifyServer(new CreateLobbyMessage(size, this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's request to join a lobby
     *
     * @param lobbyId The lobby to join's id
     */
    protected void notifyJoinLobby(int lobbyId) {
        notifyServer(new JoinLobbyMessage(lobbyId, this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's public chat message
     *
     * @param message Message contents
     */
    protected void notifyChatMessage(String message) {
        notifyServer(new ChatMessage(message, this.nickname, this.clientID));
    }
    
    /**
     * Notify the server of a client's private chat message
     *
     * @param message Message contents
     * @param dst     Message recipient
     */
    protected void notifyChatMessage(String message, String dst) {
        notifyServer(new ChatMessage(message, this.nickname, dst, this.clientID));
    }
    
    /**
     * Notify the server of a client move
     *
     * @param move A move, represented by the selected tiles and how to insert them in the shelf
     */
    protected void notifyMove(Move move) {
        notifyServer(new MoveMessage(move, this.nickname, this.clientID));
    }
    
    protected void notifyDebugMessage(String info) {
        notifyServer(new DebugMessage(info, this.nickname, this.clientID));
    }
    
}
