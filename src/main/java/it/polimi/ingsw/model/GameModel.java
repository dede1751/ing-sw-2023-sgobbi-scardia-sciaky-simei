package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.exceptions.occupiedTileException;

import java.util.*;

public class GameModel {

    private final int commonGoalNumX;
    private final int commonGoalNumY;

    private Stack<Integer> commonGoalStackX;
    private Stack<Integer> commonGoalStackY;

    private int currentPlayerIndex;
    private boolean gameOver;
    private List<Player> players;
    private Board board;

    private TileBag tileBag;

    public GameModel(int numPlayers, int commonGoalX, int commonGoalY) {
        this.commonGoalNumX = commonGoalX;
        this.commonGoalNumY = commonGoalY;
        this.tileBag=new TileBag();
this.board=new Board();
this.players=new ArrayList<Player>();
 System.out.println("Initialized game with " + numPlayers +  " players");
    }

    public int getCommonGoalX(){
        return commonGoalNumX;
    };

    public int getCommonGoalY(){
        return commonGoalNumY;
    };

    public int popStackCGX(){
        return commonGoalStackX.pop();
    }

    public int popStackCGY(){
        return commonGoalStackY.pop();
    }

    public boolean isFinalTurn() {
        return this.gameOver;
    };

    public void addPlayer(String nickname, int pgID){
        players.add(new Player(nickname,pgID));
        System.out.println("Player " + nickname +  " with id: "+ pgID);
    };

    public List<Player> getPlayers(){
            return players;
    };

    public Player getCurrentPlayer(){
        return players.get(currentPlayerIndex);
    };

    public Player setCurrentPlayer(int index){
        return players.get(index);
    };

    public Tile getTile(Coordinate coordinates){
        return board.getTile(coordinates);
    };

    public List<Coordinate> getAllCoordinates(){
        return board.getTiles().keySet().stream().toList();
    };

    public List<Coordinate> getOccupied(){
        List<Coordinate> occupied= board.getTiles()
                .entrySet()
                .stream()
                .filter(x-> !(Tile.NOTILE.equals(x.getValue())))
                .map(Map.Entry::getKey).toList();
        return occupied;
    };

    public void insertTile(Coordinate coordinates, Tile tile) throws occupiedTileException{
        if (board.getTile(coordinates).equals(Tile.NOTILE)){
            board.insertTile(coordinates,tile);
        }
        else{
            throw new occupiedTileException();
        }

    };

    public void shelveSelection(List<Coordinate> tiles, int column){


    };

    public void addCurrentPlayerScore(int score){
        players.get(currentPlayerIndex).addScore(score);
    };

    public int getTileAmount(Tile tile){return tileBag.getTileAmount(tile);}

}
