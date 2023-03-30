package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.List;

public class GameView implements Serializable {
    
    static final long serialVersionUID = 1L;
    
    public enum Event {
        ADDED_PLAYER,
        FINISHED_GAME,
    }
    
    private final List<Player> players;
    
    public GameView(GameModel model) {
        this.players = model.getPlayers();
    }
    
    public List<Player> getPlayers() { return players; }
    
}
