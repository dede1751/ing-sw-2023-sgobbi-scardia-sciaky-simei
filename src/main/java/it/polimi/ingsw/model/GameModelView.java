package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.List;

public class GameModelView implements Serializable {
    
    static final long serialVersionUID = 1L;

    private final List<Player> players;
    
    private final int currentPlayerIndex;
    
    public GameModelView(GameModel model) {
        this.players = model.getPlayers();
        this.currentPlayerIndex = model.getCurrentPlayerIndex();
    }
    
    public List<Player> getPlayers() { return players; }
    
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    
}
