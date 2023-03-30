package it.polimi.ingsw.view;

import java.io.Serializable;

public class ViewMessage implements Serializable {
    
    private final long serialVersionUID = 1L;
    
    private final int viewID;
    
    private final String nickname;
    
    public ViewMessage(View view) {
        this.viewID = view.getViewID();
        this.nickname = view.getNickname();
    }
    
    public int getViewID() { return this.viewID; }
    
    public String getNickname() { return this.nickname; }
    
}
