package it.polimi.ingsw.model;

import it.polimi.ingsw.network.Message;

import java.io.Serializable;
import java.util.List;

public class GameModelView implements Serializable, Message {
    
    static final long serialVersionUID = 1L;
    
    private final List<Player> players;
    
    private final int currentPlayerIndex;
    
    private final String winner;
    
    
    private final Board board;
    
    public GameModelView(GameModel model) {
        this.players = model.getPlayers();
        this.currentPlayerIndex = model.getCurrentPlayerIndex();
        this.board = model.getBoard();
        this.winner=model.getWinner();
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public String getWinner(){return this.winner;}
    
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    public Board getBoard() {
        return board;
    }
    
}
