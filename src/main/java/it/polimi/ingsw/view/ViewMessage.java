package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Coordinate;

import java.util.List;
import java.io.Serializable;

public class ViewMessage implements Serializable {
    
    private final long serialVersionUID = 1L;
    
    private final int viewID;
    
    private List<Coordinate> selection;
    private int column;
    
    
    public ViewMessage(View view) {
        
        this.viewID = view.getViewID();
        this.selection = view.getSelection();
        this.column=view.getColumn();
    }
    
    public int getViewID() {
        return this.viewID;
    }
    
    public List<Coordinate> getSelection(){return this.selection;}
    
    public int getColumn(){return this.column;}
}
