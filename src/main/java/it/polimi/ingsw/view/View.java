package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.network.LocalClient;
import it.polimi.ingsw.view.messages.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract class View defines the methods that must be implemented by each view.
 * Each view must override the onMessage methods to handle the messages received from the server
 * Methods to notify the server are already defined.
 */
public abstract class View implements Runnable {
    
    protected final LocalModel model = LocalModel.getInstance();
    
    protected LocalClient client;
    
    protected String nickname;
    
    protected List<LobbyController.LobbyView> lobbies = new ArrayList<>();
    
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
    
    
    public List<LobbyController.LobbyView> getLobbies() {
        return lobbies;
    }
    
    /**
     * Define view behaviour upon receiving a BoardMessage
     * @param msg received BoardMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(BoardMessage msg);
    
    /**
     * Define view behaviour upon receiving a AvailableLobbyMessage
     * @param msg received AvailableLobbyMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(AvailableLobbyMessage msg);
    
    /**
     * Define view behaviour upon receiving a EndGameMessage
     * @param msg received EndGameMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(EndGameMessage msg);
    
    
    /**
     * Define view behaviour upon receiving a StartGameMessage
     * @param msg received StartGameMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(StartGameMessage msg);
    
    /**
     * Define view behaviour upon receiving a ServerResponseMessage
     * @param msg received ServerResponseMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(ServerResponseMessage msg);
    
    /**
     * Define view behaviour upon receiving a ShelfMessage
     * @param msg received StartGameMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(ShelfMessage msg);
    
    /**
     * Define view behaviour upon receiving a IncomingChatMessage
     * @param msg received IncomingChatMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(IncomingChatMessage msg);
    
    /**
     * Define view behaviour upon receiving a UpdateScoreMessage
     * @param msg received UpdateScoreMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(UpdateScoreMessage msg);
    
    /**
     * Define view behaviour upon receiving a CommonGoalMessage
     * @param msg received CommonGoalMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(CommonGoalMessage msg);
    
    /**
     * Define view behaviour upon receiving a CurrentPlayerMessage
     * @param msg received CurrentPlayerMessage
     */
    @SuppressWarnings("unused")
    public abstract void onMessage(CurrentPlayerMessage msg);
    
    /**
     * Set the view's client. This method is set at client startup.
     *
     * @param client Client conencting the view to the server
     */
    public void setClient(LocalClient client) {
        this.client = client;
    }
    
    /**
     * Request the lobby list from the server
     *
     * @param size Desired lobby size
     */
    public void notifyRequestLobby(Integer size) {
        client.update(new RequestLobbyMessage(size, this.nickname));
    }
    
    /**
     * Notify the server of a client's request to join a recovering lobby
     */
    public void notifyRecoverLobby() {
        client.update(new RecoverLobbyMessage(this.nickname));
    }
    
    /**
     * Notify the server of a client's request to create a lobby
     *
     * @param size The new lobby's number of player
     */
    public void notifyCreateLobby(Integer size) {
        client.update(new CreateLobbyMessage(size, this.nickname));
    }
    
    /**
     * Notify the server of a client's request to join a lobby
     *
     * @param lobbyId The lobby to join's id
     */
    public void notifyJoinLobby(int lobbyId) {
        client.update(new JoinLobbyMessage(lobbyId, this.nickname));
    }
    
    /**
     * Notify the server of a client's public chat message
     *
     * @param message Message contents
     */
    public void notifyChatMessage(String message) {
        client.update(new ChatMessage(message, this.nickname));
    }
    
    /**
     * Notify the server of a client's private chat message
     *
     * @param message Message contents
     * @param dst     Message recipient
     */
    public void notifyChatMessage(String message, String dst) {
        client.update(new ChatMessage(message, this.nickname, dst));
    }
    
    /**
     * Notify the server of a client move
     *
     * @param move A move, represented by the selected tiles and how to insert them in the shelf
     */
    public void notifyMove(Move move) {
        client.update(new MoveMessage(move, this.nickname));
    }
    
    public void notifyDebugMessage(String info) {
        client.update(new DebugMessage(info, this.nickname));
    }
    
}