package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.messages.*;

import java.rmi.RemoteException;
import java.util.List;


public abstract class View implements Runnable {
    
    protected final LocalModel model = LocalModel.INSTANCE;
    
    protected int clientID;
    
    protected String nickname;
    
    protected List<LobbyController.LobbyView> lobbies;
    
    public void setClientID(int clientID) { this.clientID = clientID; }
    
    public int getClientID() { return this.clientID; }
    
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public String getNickname() { return nickname; }
    
    @SuppressWarnings("unused")
    public abstract void onMessage(BoardMessage msg);
    @SuppressWarnings("unused")
    public abstract void onMessage(AvailableLobbyMessage msg);
    @SuppressWarnings("unused")
    public abstract void onMessage(EndGameMessage msg);
    @SuppressWarnings("unused")
    public abstract void onMessage(StartGameMessage msg);
    
    
    protected Server server;
    public void setServer(Server server){ this.server = server; }
    
    protected <T extends ViewMessage<?>> Response notifyServer(T msg){
        try{
            return server.update(msg);
        }catch( RemoteException e ){
            e.printStackTrace(System.err);
            return new Response(1, e.getMessage(), msg.getClass().getSimpleName());
        }
    }
    
    protected Response notifyMove(Move move){
        return notifyServer(new MoveMessage(move, this.nickname, this.clientID));
    }
    protected Response notifyCreateLobby(LobbyInformation info){
        return notifyServer(new CreateLobbyMessage(info, this.nickname, this.clientID));
    }
    protected Response notifyJoinLobby(JoinLobby info){
        return notifyServer(new JoinLobbyMessage(info, this.nickname, this.clientID));
    }
    protected Response notifyChatMessage(String message){
        return notifyServer(new ChatMessage(message, this.nickname, this.clientID));
    }
    protected Response notifyChatMessage(String message, String dst){
        return notifyServer(new ChatMessage(message, this.nickname, dst, this.clientID));
    }
    protected Response notifyDebugMessage(String info){
        return notifyServer(new DebugMessage(info, this.nickname, this.clientID));
    }
    protected Response notifyRequestLobby(LobbyInformation info){
        return notifyServer(new RequestLobby(info, this.nickname, this.clientID));
    }
    
}
