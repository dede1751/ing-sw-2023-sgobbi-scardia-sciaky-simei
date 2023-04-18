package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.observer.Observable;

import java.util.HashSet;
import java.util.List;


public abstract class View extends Observable<View.Action> implements Runnable {
    
    public enum Action {
        CREATE_LOBBY,
        JOIN_LOBBY,
        PASS_TURN,
        MOVE,
        CHAT
    }
    
    private int clientID;
    
    String nickname;
    
    int selectedPlayerCount;
    
    private List<Coordinate> selectedCoordinates;
    
    private List<Tile> selectedTiles;
    
    private int column;
    
    public void setClientID(int clientID) { this.clientID = clientID; }
    
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
    
    
    public abstract void update(GameModelView model, GameModel.Event evt);
    
    protected void setChangedAndNotifyObservers(View.Action evt) {
        setChanged();
        notifyObservers(evt);
    }
    
}
