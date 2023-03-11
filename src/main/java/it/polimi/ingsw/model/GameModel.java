package it.polimi.ingsw.model;

public class GameModel {
    final int commonGoalNumX;
    final int commonGoalNumY;

    public GameModel(int numPlayers, int commonGoalX, int commonGoalY) {
        this.commonGoalNumX = commonGoalX;
        this.commonGoalNumY = commonGoalY;

        System.out.println("Initialized game with " + numPlayers +  " players");
    }
}
