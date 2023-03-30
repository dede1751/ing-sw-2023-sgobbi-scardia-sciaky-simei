package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.utils.observer.Observable;


public abstract class View extends Observable<View.Action> implements Runnable {
    
    public enum Action {
        LOGIN,
        REMOVE_SELECTION,
        INSERT_SELECTION,
    }
    
    private int viewID;
    
    private String nickname;
    
    public void setViewID(int viewID) { this.viewID = viewID; }
    
    public int getViewID() { return this.viewID; }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
        setChangedAndNotifyObservers(View.Action.LOGIN);
    };
    
    public String getNickname() { return this.nickname; };
    
    public abstract void update(GameView model, GameModel.Event evt);
    
    private void setChangedAndNotifyObservers(View.Action evt) {
        setChanged();
        notifyObservers(evt);
    }
}
