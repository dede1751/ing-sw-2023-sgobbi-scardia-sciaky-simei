package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;

import java.util.List;
import java.io.Serializable;

public class ViewMessage implements Serializable {
    
    private final int clientID;
    
    private final String nickname;
    
    private final int lobbySize;
    
    private final List<Coordinate> selection;
    
    private final List<Tile> tiles;
    
    private final int column;
    
    public ViewMessage(View view) {
        this.clientID = view.getClientID();
        this.nickname = view.getNickname();
        this.lobbySize = view.getSelectedPlayerCount();
        
        this.selection = view.getSelectedCoordinates();
        this.column = view.getColumn();
        this.tiles = view.getSelectedTiles();
    }
    
    public int getClientID() {
        return this.clientID;
    }
    
    public String getNickname() { return nickname; }
    
    public int getLobbySize() { return lobbySize; }
    
    public List<Coordinate> getSelection(){return this.selection;}
    
    public List<Tile> getTiles(){return this.tiles;}
    
    public int getColumn(){return this.column;}
    
}
