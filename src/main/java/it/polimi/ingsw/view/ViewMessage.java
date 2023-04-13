package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Coordinate;

import java.util.List;
import java.io.Serializable;

public class ViewMessage implements Serializable {
    
    private final long serialVersionUID = 1L;
    
    private final int viewID;
    
    private List<Coordinate> selection;
    
    
    public ViewMessage(View view) {
        
        this.viewID = view.getViewID();
        this.selection = view.getSelection();
    }
    
    public int getViewID() {
        return this.viewID;
    }
    
}
