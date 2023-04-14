package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;

import java.util.List;
import java.io.Serializable;

public class ViewMessage implements Serializable {
    
    
    private final int viewID;
    
    private final List<Coordinate> selection;
    private final List<Tile> tiles;
    private final int column;
    
    
    public ViewMessage(View view) {
        
        this.viewID = view.getViewID();
        this.selection = view.getSelectedCoordinates();
        this.column=view.getColumn();
        this.tiles = view.getSelectedTiles();
    }
    
    public int getViewID() {
        return this.viewID;
    }
    
    public List<Coordinate> getSelection(){return this.selection;}
    public List<Tile> getTiles(){return this.tiles;}
    
    public int getColumn(){return this.column;}
}
