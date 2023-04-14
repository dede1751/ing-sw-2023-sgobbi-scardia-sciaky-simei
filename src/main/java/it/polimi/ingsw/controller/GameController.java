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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GameController {
    
    private final GameModel model;
    
    private final List<Client> clients;
    
    private boolean isPaused;
    
    /* constructor tobe used for paused game*/
    public GameController(GameModel model, List<Client> clients) {
        this.model = model;
        this.clients = clients;
        
        model.startGame();
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
    
    
    public void turn(List<Tile> selection, int col) {
        
        //reference to the current player
        Player currentPlayer = model.getCurrentPlayer();
        
        //remove the selection from the board and add it to the current player shelfe
        //TODO wrong function, it needs the coordinates to be removed from the board
        model.shelveSelection(selection, col);
        
        
        if( CommonGoal.getCommonGoal(model.getCommonGoalX()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerScore(model.popStackCGX());
            model.getCurrentPlayer().setCompletedGoalX(true);
        }
        if( CommonGoal.getCommonGoal(model.getCommonGoalY()).checkGoal(currentPlayer.getShelf()) ) {
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
    
    public void initGame() {
        model.getBoard().refill(model.getTileBag());
        game();
    }
    
    
    public void game() {
        
        
        while( !isPaused ) {
            while( !model.isFinalTurn() ) {
                //turn();
            }
            do {
                //turn();
            }
            while( model.getCurrentPlayerIndex() != clients.size() - 1 );
            
            
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
        
        for( int i = 0; i < model.getNumPlayers() - 1; i++ ) {
            if( model.getPlayers().get(i).getScore() > model.getPlayers().get(winnerIndex).getScore() ) {
                winnerIndex = i;
            }
        }
        
        model.setWinner(model.getPlayers().get(winnerIndex).getNickname());
    }
    
    
    public void update(ViewMessage o, View.Action evt) {
        int currentPlayerIndex = model.getCurrentPlayerIndex();
        if( o.getViewID() != currentPlayerIndex ) {
            System.err.println("Ignoring event from view:" + o.getViewID() + ": " + evt + ". Not the current Player.");
            return;
        }
        
        List<Tile> selectionTiles = new ArrayList<Tile>();
        for( int i = 0; i < o.getSelection().size(); i++ ) {
            selectionTiles.add(model.getBoard().getTile(o.getSelection().get(i)));
        }
        model.removeSelection(o.getSelection());
        turn(selectionTiles, o.getColumn());
        
        if( model.isFinalTurn() && currentPlayerIndex == 3 ) {
            endGame();
            
            //check if the current player is the last in the list of players, if it is, set current player to the first in the list
        }else if( model.getCurrentPlayerIndex() == model.getNumPlayers() ) {
            model.setCurrentPlayerIndex(0);
        }else {
            model.setCurrentPlayerIndex(model.getCurrentPlayerIndex() + 1);
        }
        
       /* switch( evt ) {
            case PASS_TURN -> {
                System.out.println("Player " + currentPlayerIndex + " passed his turn.");
                
                int nextPlayer = currentPlayerIndex < model.getNumPlayers() - 1 ? currentPlayerIndex + 1 : 0;
                model.setCurrentPlayerIndex(nextPlayer);
            }
            case INSERT_SELECTION -> {
            }
            default -> System.err.println("Ignoring event from View:" + o.getViewID() + ": " + evt);
        }*/
    }
}
