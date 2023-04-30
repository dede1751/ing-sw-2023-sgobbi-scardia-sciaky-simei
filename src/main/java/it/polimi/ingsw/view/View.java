package it.polimi.ingsw.view;

import it.polimi.ingsw.model.messages.ModelMessage;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.messages.*;

import java.rmi.RemoteException;


public abstract class View implements Runnable {
    
    
    protected Server server;
    
    protected final LocalModel model = LocalModel.INSTANCE;
    
    protected int clientID;
    
    protected String nickname;
    
    
    protected int column;
    
    public void setClientID(int clientID) { this.clientID = clientID; }
    
    public int getClientID() { return this.clientID; }
    
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public String getNickname() { return nickname; }
    
    //True for socket, false for rmi
    protected boolean service;
    
    public void setServer(Server server){
        this.server = server;
    }
    
    public abstract void update(ModelMessage<?> msg);
    
    
    protected <T extends ViewMessage<?>> Response notifyServer(T msg){
        try{
            return server.update(msg);
        }catch( RemoteException e ){
            e.printStackTrace(System.err);
            return new Response(1, e.getMessage(), msg.getClass().getSimpleName());
        }
    }
    //FIXME proper error and response messages handling
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
        return notifyServer(new ChatMessage(message, dst, this.nickname, this.clientID));
    }
    protected Response notifyDebugMessage(String info){
        return notifyServer(new DebugMessage(info, this.nickname, this.clientID));
    }
    protected Response notifyRequestLobby(LobbyInformation info){
        return notifyServer(new RequestLobby(info, this.nickname, this.clientID));
    }
    
    public void setService(boolean service) {
        this.service = service;
    }
}
