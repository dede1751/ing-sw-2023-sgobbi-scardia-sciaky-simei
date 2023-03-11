package it.polimi.ingsw.model;
import java.util.*;
public class GameModel {
    private final int commonGoalNumX;
    private final int commonGoalNumY;

    private Stack<Integer> commonGoalStackX;
    private Stack<Integer> commonGoalStackY;

    private int currentPlayerIndex;
    private boolean gameOver;
    private List<Player>;
    public GameModel(int numPlayers, int commonGoalX, int commonGoalY) {
        this.commonGoalNumX = commonGoalX;
        this.commonGoalNumY = commonGoalY;

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

    public boolean isFinalTurn() {};
    public void addPlayer(String nickname, int pgID){};
    public List<Player> getPlayers(){};
    public Player getCurrentPlayer(){};
    public Player setCurrentPlayer(int index){};
    public Tile getTile(Coordinates coordinates){};
    public List<Coordinates> getAllCoordinates(){};
    public List<Coordinates> getOccupied(){};
    public void insertTile(Coordinate coordinates, Tile tiles){};
    public void shelveSelection(List<Coordinate> tiles, int column){};
    public void addCurrentPlayerScore(int score){};
    int getTileAmount(Tile tile){};

    public <T> void subscribe (T observer){};
    public void notifyAllObs(){};

}
