package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.utils.observer.Observable;


public abstract class View extends Observable<View.Action> implements Runnable {
    
    public enum Action {
        PASS_TURN,
        REMOVE_SELECTION,
        INSERT_SELECTION,
    }
    
    private int viewID;
    
    public void setViewID(int viewID) { this.viewID = viewID; }
    
    public int getViewID() { return this.viewID; }
    
    public abstract LobbyController.LoginInfo userLogin(LobbyController.LobbyInfo info);
    
    public abstract void update(GameModelView model, GameModel.Event evt);
    
    protected void setChangedAndNotifyObservers(View.Action evt) {
        setChanged();
        notifyObservers(evt);
    }
    
}
