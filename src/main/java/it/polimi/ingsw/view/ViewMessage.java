package it.polimi.ingsw.view;

import java.io.Serializable;

public class ViewMessage implements Serializable {
    
    private final long serialVersionUID = 1L;
    
    private final int viewID;
    
    public ViewMessage(View view) {
        this.viewID = view.getViewID();
    }
    
    public int getViewID() { return this.viewID; }
    
}
