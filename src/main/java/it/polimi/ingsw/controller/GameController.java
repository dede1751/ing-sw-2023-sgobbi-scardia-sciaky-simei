package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.goals.common.CommonGoal;
import it.polimi.ingsw.model.goals.personal.PersonalGoal;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.util.List;


public class GameController {
    
    private final GameModel model;
    
    private final Client view;
    
    private final int viewID;
    
    private int numPlayers;
    
    private boolean isPaused;
    
    
    /* constructor tobe used for paused game*/
    public GameController(GameModel model, Client view, int viewID) {
        this.model = model;
        this.view = view;
        this.viewID = viewID;
    }
    
    //TODO
    public Boolean needRefill() {
        Board toBeChecked = model.getBoard();
        return true;
    }
    
    //TODO we need to understand how to take the coordinate and column from player
    public void turn(List<Tile> selection, int col) {
    
        //reference to the current player
        Player currentPlayer = model.getCurrentPlayer();
        
        //remove teh selection from the board and add it to the current player shelfe
        model.shelveSelection(selection, col);
        
        
        if( CommonGoal.getCommonGoal(model.getCommonGoalX()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerScore(model.popStackCGX());
        }
        if( CommonGoal.getCommonGoal(model.getCommonGoalY()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerScore(model.popStackCGY());
        }
        
        
        if( needRefill() ) {
            model.getBoard().refill(model.getTileBag());
        }
        
        
        if( currentPlayer.getShelf().isFull() ) {
            model.setGameOver();
        }
        
        
        //check if the current player is the last in the list of players, if it is, set current player to the first in the list
        if( model.getCurrentPlayerIndex() == model.getNumPlayers() ) {
            model.setCurrentPlayerIndex(0);
        }else {
            model.setCurrentPlayerIndex(model.getCurrentPlayerIndex() + 1);
        }
    

        
    }
    
    public void initGame() {
        model.getBoard().refill(model.getTileBag());
        game();
    }
    
    //TODO once understood how to take coordinates and column, update the turn call accordingly
    public void game() {
        
        
        while( !isPaused ) {
            while( !model.isFinalTurn() ) {
                //turn();
            }
            do {
                //turn();
            }
            while( model.getCurrentPlayerIndex() != numPlayers - 1 );
            
            
            endGame();
        }
    }
    

    
    public void endGame() {
        int winnerIndex = 0;
        for( int i = 0; i < model.getNumPlayers(); i++ ) {
            model.getPlayers().get(i).addScore(
                    PersonalGoal.getPersonalGoal(model.getPlayers().get(i).getPg()).checkGoal(
                            model.getPlayers().get(i).getShelf()));
        }
        
        for( int i = 0; i < numPlayers - 1; i++ ) {
            if( model.getPlayers().get(i).getScore() > model.getPlayers().get(winnerIndex).getScore() ) {
                winnerIndex = i;
            }
        }
        
        System.out.println("The winner is" + model.getPlayers().get(winnerIndex) + "!!!");
        
    }
    
    
    public void pause() {
        isPaused = true;
        //TODO save teh model into the server
    }
    
    public void resume() {
        isPaused = false;
        game();
    }
    
    public void update(ViewMessage o, View.Action evt) {
        if ( o.getViewID() != this.viewID ) {
            System.err.println("Discarding notification from " + o);
            return;
        }
        
        switch ( evt ) {
            case LOGIN -> {
                String nickname = o.getNickname();
                model.addPlayer(nickname, 0);
            }
            default -> System.err.println("Ignoring event from " + view + ": " + evt);
        }
        
    }
    
}
