package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.goals.common.CommonGoal;
import it.polimi.ingsw.view.VirtualView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;


public class GameController implements PropertyChangeListener {
    
    private final GameModel model;
    
    private final VirtualView view;
    
    private int numPlayers;
    
    private CommonGoal commonGoalx;
    private CommonGoal commonGoaly;
    
    
    /* constructor tobe used for paused game*/
    public GameController(GameModel model, VirtualView view) {
        this.model = model;
        this.view = view;
        
        model.addPropertyChangeListener(view);
    }
    
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch( evt.getPropertyName() ) {
            case "LOGIN" -> {
                this.model.addPlayer((String) evt.getNewValue(), 0);
            }
            default -> System.out.println("Unrecognized property change");
        }
    }
    
    
    //function to be written, need to check if the board need to be refilled
    public Boolean needRefill() {
        Board toBeChecked = model.getBoard();
        return true;
    }
    
    
    public void turnController(List<Tile> selection, int col) {
        
        
        //check if the current player is the last in the list of players, if it is, set current player to the first in the list
        if( model.getCurrentPlayerIndex() == model.getNumPlayers() ) {
            model.setCurrentPlayerIndex(0);
        }else {
            model.setCurrentPlayerIndex(model.getCurrentPlayerIndex() + 1);
        }
        
        
        Player currentPlayer = model.getCurrentPlayer();
        
        
        this.model.shelveSelection(selection, col);
        
        
        if( getCommonGoal )
            
            
            if( needRefill() == true ) {
                model.getBoard().refill(model.getTileBag());
            }
        
        
        if( currentPlayer.getShelf().isFull() ) {
            model.setGameOver();
        }
        
        
    }
    
}
