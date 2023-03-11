package it.polimi.ingsw.model;

public class Player {

    final private String nickname;
    private int score;
    final private int pgID;
    Player (String nickname, int pgID){
        this.nickname = nickname;
        this.pgID = pgID;
        score = 0;
    };
    public String getNickname(){
        return nickname;
    }
    public int getScore(){
        return score;
    }
    public int getPg(){
        return pgID;
    }
    public Shelf getShelf(){};
    public int addScore(int score){
        this.score = this.score+score;
        return this.score;
    }
}
