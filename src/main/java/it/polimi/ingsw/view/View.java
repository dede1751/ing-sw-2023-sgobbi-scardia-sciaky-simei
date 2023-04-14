package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.observer.Observable;

import java.util.List;


public abstract class View extends Observable<View.Action> implements Runnable {
    
    
    public enum Action {
        PASS_TURN,
        MOVE,
        CHAT
    }
    
    private int viewID;
    
    
    public void setViewID(int viewID) {
        this.viewID = viewID;
    }
    
    public int getViewID() {
        return this.viewID;
    }
    
    private List<Coordinate> selectedCoordinates;
    private List<Tile> selectedTiles;
    private int column;
    public void setSelectedCoordinates(List<Coordinate> selection) {
        this.selectedCoordinates = selection;
    }
    public void setSelectedTiles(List<Tile> selectedTiles){
        this.selectedTiles = selectedTiles;
    }
    public List<Coordinate> getSelectedCoordinates() {
        return this.selectedCoordinates;
    }
    
    public List<Tile> getSelectedTiles() {
        return this.selectedTiles;
    }
    
    public int getColumn() {
        return this.column;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }
    
    public abstract LobbyController.LoginInfo userLogin(LobbyController.LobbyInfo info);
    
    public abstract void update(GameModelView model, GameModel.Event evt);
    
    protected void setChangedAndNotifyObservers(View.Action evt) {
        setChanged();
        notifyObservers(evt);
    }
    
}
