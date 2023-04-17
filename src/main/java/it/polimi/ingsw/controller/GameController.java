package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;
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
    
    public void turnManager() {
        
        //reference to the current player
        Player currentPlayer = model.getCurrentPlayer();
        
        if( !model.getCurrentPlayer().isCompletedGoalX() &&
            CommonGoal.getCommonGoal(model.getCommonGoalX()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerScore(model.popStackCGX());
            model.getCurrentPlayer().setCompletedGoalX(true);
        }
        if( !model.getCurrentPlayer().isCompletedGoalY() &&
            CommonGoal.getCommonGoal(model.getCommonGoalY()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerScore(model.popStackCGY());
            model.getCurrentPlayer().setCompletedGoalY(true);
        }
        
        if( needRefill() ) {
            model.getBoard().refill(model.getTileBag());
        }
        
        if( currentPlayer.getShelf().isFull() ) {
            model.setGameOver();
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
        for( int i = 0; i < model.getNumPlayers(); i++ ) {
            model.getPlayers().get(i).addScore(
                    PersonalGoal.getPersonalGoal(model.getPlayers().get(i).getPg()).checkGoal(
                            model.getPlayers().get(i).getShelf()));
        }
        
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
                model.shelveSelection(o.getTiles(), o.getColumn());
                turnManager();
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
