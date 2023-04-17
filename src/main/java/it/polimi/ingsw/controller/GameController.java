package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.goals.common.CommonGoal;
import it.polimi.ingsw.model.goals.personal.PersonalGoal;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.util.List;
import java.util.Map;


public class GameController {
    
    private final GameModel model;
    
    private final List<Client> clients;
    private Integer currentPlayerIndex;
    private final Integer playerNumber;
    
    
    public GameController(GameModel model, List<Client> clients) {
        this.model = model;
        this.clients = clients;
        playerNumber = clients.size();
        currentPlayerIndex = 0;
    }
    
    public Boolean needRefill() {
        Map<Coordinate, Tile> toBeChecked = model.getBoard().getTiles();
        
        for( var entry : toBeChecked.entrySet() ) {
            if( !(entry.getValue().equals(Tile.NOTILE)) ) {
                if( !(model.getBoard().getTile(entry.getKey().getDown()) == Tile.NOTILE) ) {
                    return false;
                }
                if( !(model.getBoard().getTile(entry.getKey().getUp()) == Tile.NOTILE) ) {
                    return false;
                }
                if( !(model.getBoard().getTile(entry.getKey().getLeft()) == Tile.NOTILE) ) {
                    return false;
                }
                if( !(model.getBoard().getTile(entry.getKey().getRight()) == Tile.NOTILE) ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public int checkAdjacency(Shelf shelf){
        var shelfTiles=shelf.getAllShelf();
        for(int i=0;i<Shelf.N_ROW;i++){
            for (int j=0;j<Shelf.N_COL;j++){
            
            }
        }
    }
    
    
    public void turnManager(List<Tile> selection, int col) {
        
        //reference to the current player
        Player currentPlayer = model.getCurrentPlayer();
        
        //remove the selection from the board and add it to the current player shelf
        model.shelveSelection(selection, col);
        
        if( !model.getCurrentPlayer().isCompletedGoalX() &&
            CommonGoal.getCommonGoal(model.getCommonGoalX()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerCommongGoalScore(model.popStackCGX());
            model.getCurrentPlayer().setCompletedGoalX(true);
        }
        if( !model.getCurrentPlayer().isCompletedGoalY() &&
            CommonGoal.getCommonGoal(model.getCommonGoalY()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerCommongGoalScore(model.popStackCGY());
            model.getCurrentPlayer().setCompletedGoalY(true);
        }
        
       
        currentPlayer.setPersonalGoalScore(
                PersonalGoal.getPersonalGoal(currentPlayer.getPg()).checkGoal(currentPlayer.getShelf()));
        currentPlayer.setAdjacentScore( checkAdjacency(currentPlayer.getShelf()));
        
        if( needRefill() ) {
            model.getBoard().refill(model.getTileBag());
        }
        
        if( currentPlayer.getShelf().isFull() ) {
            model.setLastTurn();
        }
    }
    
    public void nextPlayerSetter() {
        if( model.isFinalTurn() && model.getCurrentPlayerIndex() == 3 ) {
            endGame();
            //check if the current player is the last in the list of players, if it is, set current player to the first in the list
        }else {
            currentPlayerIndex = (++currentPlayerIndex) % playerNumber;
            model.setCurrentPlayerIndex(currentPlayerIndex);
        }
    }
    
    public void endGame() {
        int winnerIndex = 0;
        
        for( int i = 0; i < model.getNumPlayers() - 1; i++ ) {
            if( model.getPlayers().get(i).getScore() > model.getPlayers().get(winnerIndex).getScore() ) {
                winnerIndex = i;
            }
        }
        
        model.setWinner(model.getPlayers().get(winnerIndex).getNickname());
    }
    
    //TODO change name to ViewMessage
    public void update(ViewMessage o, View.Action evt) {
        int currentPlayerIndex = model.getCurrentPlayerIndex();
        if( o.getViewID() != currentPlayerIndex ) {
            System.err.println("Ignoring event from view:" + o.getViewID() + ": " + evt + ". Not the current Player.");
            return;
        }
        switch( evt ) {
            case MOVE -> {
                model.removeSelection(o.getSelection());
                turnManager(o.getTiles(), o.getColumn());
                nextPlayerSetter();
            }
            case CHAT -> {
                //TODO chat functions;
            }
            case PASS_TURN -> {
                //FIXME only for debug porpouses, to be removed in final version
                System.out.println("Player " + currentPlayerIndex + " passed his turn.");
                nextPlayerSetter();
            }
        }
    }
}
