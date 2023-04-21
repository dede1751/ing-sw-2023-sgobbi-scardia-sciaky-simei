package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.observer.Observable;
import it.polimi.ingsw.view.messages.*;

import java.rmi.RemoteException;
import java.util.List;


public abstract class View extends Observable<View.Action> implements Runnable {
    
    public enum Action {
        CREATE_LOBBY,
        JOIN_LOBBY,
        PASS_TURN,
        MOVE,
        CHAT
    }
    
    Server server;
    

    private int clientID;
    
    String nickname;
    
    int selectedPlayerCount;
    
    private List<Coordinate> selectedCoordinates;
    
    private List<Tile> selectedTiles;
    
    private int column;
    
    public void setClientID(int clientID) { this.clientID = clientID; }
    
    public abstract void setAvailableLobbies(List<LobbyController.LobbyView> lobbies);
    
    public int getClientID() { return this.clientID; }
    
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public String getNickname() { return nickname; }
    
    public void setSelectedPlayerCount(int selectedPlayerCount) { this.selectedPlayerCount = selectedPlayerCount; }
    
    public int getSelectedPlayerCount() { return selectedPlayerCount; }
    
    public void setSelectedCoordinates(List<Coordinate> selection) {
        this.selectedCoordinates = selection;
    }
    
    public List<Coordinate> getSelectedCoordinates() { return this.selectedCoordinates; }
    
    public void setSelectedTiles(List<Tile> selectedTiles){ this.selectedTiles = selectedTiles; }
    
    public List<Tile> getSelectedTiles() { return this.selectedTiles; }
    
    public int getColumn() {
        return this.column;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }
    
    public void setServer(Server server){
        this.server = server;
    }
    
    public abstract void update(GameModelView model, GameModel.Event evt);
    
    protected void setChangedAndNotifyObservers(View.Action evt) {
        setChanged();
        notifyObservers(evt);
    }
    
    protected <T extends ViewMsg<?>> GameController.Response notifyServer( T msg){
        try{
            return server.update(msg);
        }catch( RemoteException e ){
            e.printStackTrace();
            return new GameController.Response(1, e.getMessage());
        }
    }
    //FIXME proper error and response messages handling
    protected GameController.Response notifyMove(Move move){
        return notifyServer(new MoveMessage(move, this.nickname, this.clientID));
    }
    protected GameController.Response notifyCreateLobby(LobbyInformation info){
        return notifyServer(new CreateLobbyMessage(info, this.nickname, this.clientID));
    }
    protected GameController.Response notifyJoinLobby(JoinLobby info){
        return notifyServer(new JoinLobbyMessage(info, this.nickname, this.clientID));
    }
    protected GameController.Response notifyChatMessage(String message){
        return notifyServer(new ChatMessage(message, this.nickname, this.clientID));
    }
    protected GameController.Response notifyChatMessage(String message, String dst){
        return notifyServer(new ChatMessage(message, dst, this.nickname, this.clientID));
    }
    protected GameController.Response notifyDebugMessage(String info){
        return notifyServer(new DebugMessage(info, this.nickname, this.clientID));
    }
    
}
