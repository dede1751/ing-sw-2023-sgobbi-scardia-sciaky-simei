package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.messages.UpdateScorePayload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Local model representation used by the running view.
 * Updated when a message is received from the server.
 */
public class LocalModel {
    
    public static final LocalModel INSTANCE = new LocalModel();
    
    private List<String> playersNicknames;
    
    private String currentPlayer;
    
    private final Map<String, Shelf> shelves = new HashMap<>();
    
    private final Map<String, Integer> points = new HashMap<>();
    
    private final Map<String,Integer> cgScore = new HashMap<>();
    private final Map<String,Integer> pgScore = new HashMap<>();
    private final Map<String,Integer> adjacencyScore = new HashMap<>();
    private final Map<String,Integer> bonusScore = new HashMap<>();
    
    
    private int pgid = 0;
    
    private int CGXindex = 0;
    private int CGYindex = 0;
    
    private int topCGXscore = 0;
    private int topCGYscore = 0;
    
    private Board board;
    
    private final List<String> chat = new ArrayList<>();
    
    
    private LocalModel() {
    }
    
    public void addChatMessage(String nickname, String message) {
        chat.add(nickname + " -> " + message + "\n");
    }
    
    public String getChat() {
        StringBuilder sb = new StringBuilder();
        for( String s : chat ) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    public Board getBoard() {
        return board;
    }
    
    
    /**
     * Methods to access and modify values of a Map that associates
     * a player with his respective score.
     */
    public void setPoints(UpdateScorePayload.Type type, String nickname, int score){
        switch( type ){
            case Adjacency :setadjacencyScore(score,nickname);
            case CommonGoal: setCgScore(score,nickname);
            case PersonalGoal:setPgScore(score,nickname);
            case Bonus:setBonusScore(score,nickname);
        }
        
        int points = getadjacencyScore(nickname) + getCgScore(nickname) + getPgScore(nickname) + getBonusScore(nickname);
        if( this.points.containsKey(nickname) ) {
            this.points.replace(nickname, points);
        }else {
            this.cgScore.put(nickname, points);
        }
    }
    
    
    public int getPoints(String nickname) {
        return this.points.get(nickname);
    }
    public void setCgScore(Integer points, String nickname) {
        if( this.cgScore.containsKey(nickname) ) {
            this.cgScore.replace(nickname, points);
        }else {
            this.cgScore.put(nickname, points);
        }
        
    }
    public int getCgScore(String nickname) {
        return this.cgScore.get(nickname);
    }
    public void setPgScore(Integer points, String nickname) {
        if( this.pgScore.containsKey(nickname) ) {
            this.pgScore.replace(nickname, points);
        }else {
            this.pgScore.put(nickname, points);
        }
       
    
    }
    public int getPgScore(String nickname) {
        return this.pgScore.get(nickname);
    }
    
    public void setadjacencyScore(Integer points, String nickname) {
        if( this.adjacencyScore.containsKey(nickname) ) {
            this.adjacencyScore.replace(nickname, points);
        }else {
            this.adjacencyScore.put(nickname, points);
        }
        
    
    }
    
    public int getadjacencyScore(String nickname) {
        return this.adjacencyScore.get(nickname);
    }
    
    public void setBonusScore(Integer points, String nickname) {
        if( this.bonusScore.containsKey(nickname) ) {
            this.bonusScore.replace(nickname, points);
        }else {
            this.bonusScore.put(nickname, points);
        }
        
    
    }
    public int getBonusScore(String nickname) {
        return this.bonusScore.get(nickname);
    }
    
    /**
     * Methods to access and modify values of a Map that associates
     * a player with his respective shelf.
     */
    public void setShelf(Shelf shelf, String nickname) {
        if( this.shelves.containsKey(nickname) ) {
            this.shelves.replace(nickname, shelf);
        }else {
            this.shelves.put(nickname, shelf);
        }
    }
    
    public Shelf getShelf(String nickname) {
        return this.shelves.get(nickname);
    }
    
    
    /**
     * Method to access and add values to a Map that associates
     * a player with his respective personal score.
     */
    public void setPgid(int pgid) {
        this.pgid = pgid;
    }
    
    public int getPgid() {
        return this.pgid;
    }
    
    
    public int getTopCGXscore() {
        return topCGXscore;
    }
    
    public void setTopCGXscore(int topCGXscore) {
        this.topCGXscore = topCGXscore;
    }
    
    public int getTopCGYscore() {
        return topCGYscore;
    }
    
    public void setTopCGYscore(int topCGYscore) {
        this.topCGYscore = topCGYscore;
    }
    
    public int getCGXindex() {
        return CGXindex;
    }
    
    public int getCGYindex() {
        return CGYindex;
    }
    
    public List<String> getPlayersNicknames() {
        return playersNicknames;
    }
    
    public void setPlayersNicknames(List<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
    }
    
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    public void setCGXindex(int CGXindex) {
        this.CGXindex = CGXindex;
    }
    
    public void setCGYindex(int CGYindex) {
        this.CGYindex = CGYindex;
    }
    
}
