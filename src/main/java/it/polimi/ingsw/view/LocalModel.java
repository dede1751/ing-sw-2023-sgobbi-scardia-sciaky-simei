package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Shelf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalModel {
    
    public static final LocalModel INSTANCE = new LocalModel();
    
    private List<String> playersNicknames;
    
    private final Map<String, Shelf> shelves = new HashMap<>();
    
    private final Map<String, Integer> points = new HashMap<>();
    
    private String CGXdescription = "";
    private String CGYdescription = "";
    
    private int topCGXscore = 0;
    private int topCGYscore = 0;
    private Board board;
    
    private LocalModel() {}
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public void setPoints(Integer points, String nickname){
        if(this.points.containsKey(nickname)){
            this.points.replace(nickname, points);
        }else{
            this.points.put(nickname, points);
        }
    }
    
    public int getPoints(String nickname){
        return this.points.get(nickname);
    }
    
    public void setShelves(Shelf shelf, String nickname) {
        if(this.shelves.containsKey(nickname)){
            this.shelves.replace(nickname, shelf);
        }else{
            this.shelves.put(nickname, shelf);
        }
    }
    
    public Shelf getShelf(String nickname){
        return this.shelves.get(nickname);
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
    
    public String getCGXdescription() {
        return CGXdescription;
    }
    
    public void setCGXdescription(String CGXdescription) {
        this.CGXdescription = CGXdescription;
    }
    
    public String getCGYdescription() {
        return CGYdescription;
    }
    
    public void setCGYdescription(String CGYdescription) {
        this.CGYdescription = CGYdescription;
    }
    
    public List<String> getPlayersNicknames() {
        return playersNicknames;
    }
    
    public void setPlayersNicknames(List<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
    }
    
}
