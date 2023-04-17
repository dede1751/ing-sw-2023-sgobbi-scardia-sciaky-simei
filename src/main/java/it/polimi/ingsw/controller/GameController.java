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
    
    // Needed to close the lobby when the game ends
    private final int lobbyID;
    
    private Integer currentPlayerIndex;
    
    private final Integer playerNumber;
    
    
    public GameController(GameModel model, int lobbyID) {
        this.model = model;
        this.lobbyID = lobbyID;
        this.playerNumber = model.getPlayers().size();
        currentPlayerIndex = 0;
        
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
    
    public void turnManager(List<Tile> selection, int col) {
        
        //reference to the current player
        Player currentPlayer = model.getCurrentPlayer();
        
        //remove the selection from the board and add it to the current player shelf
        model.shelveSelection(selection, col);
        
        if( !currentPlayer.isCompletedGoalX() &&
            CommonGoal.getCommonGoal(model.getCommonGoalX()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerScore(model.popStackCGX());
            currentPlayer.setCompletedGoalX(true);
        }
        if( !currentPlayer.isCompletedGoalY() &&
            CommonGoal.getCommonGoal(model.getCommonGoalY()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerScore(model.popStackCGY());
            currentPlayer.setCompletedGoalY(true);
        }
        
        if( needRefill() ) {
            model.getBoard().refill(model.getTileBag());
        }
        
        if( currentPlayer.getShelf().isFull() ) {
            model.setLastTurn();
        }
    }
    
    /**
     * Update the current player, handling game termination
     */
    public void nextPlayerSetter() {
        if( model.isLastTurn() && model.getCurrentPlayerIndex() == 3 ) {
            endGame();
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
        
        for ( int i = 0; i < model.getNumPlayers() - 1; i++ ) {
            if( model.getPlayers().get(i).getScore() > model.getPlayers().get(winnerIndex).getScore() ) {
                winnerIndex = i;
            }
        }
        
        model.setWinner(model.getPlayers().get(winnerIndex).getNickname());
    }
    
    //TODO change name to ViewMessage
    public void update(ViewMessage o, View.Action evt) {
        String currentPlayerNick = model.getCurrentPlayer().getNickname();
        if( !o.getNickname().equals(currentPlayerNick) ) {
            System.err.println("Ignoring event from player '" + o.getNickname() + "': " + evt + ". Not the current Player.");
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
