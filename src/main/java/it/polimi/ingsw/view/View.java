package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.utils.observer.Observable;

import java.util.List;


public abstract class View extends Observable<View.Action> implements Runnable {
    
    public enum Action {
        PASS_TURN,
        LAST_TURN,
        INSERT_SELECTION,
    }
    
    private int viewID;
    
    
    public void setViewID(int viewID) {
        this.viewID = viewID;
    }
    
    public int getViewID() {
        return this.viewID;
    }
    
    private List<Coordinate> selection;
    private int column;
    public void setSelection(List<Coordinate> selection) {
        this.selection = selection;
    }
    
    public List<Coordinate> getSelection() {
        return this.selection;
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
