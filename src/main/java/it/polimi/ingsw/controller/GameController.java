package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.view.VirtualView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;


public class GameController implements PropertyChangeListener {
    
    private final GameModel model;
    
    private final VirtualView view;
    
    private int numPlayers;
    
    
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
    
    public void turnController(List<Coordinate> selection, int col) {
        
        //check if the current player is the last in the list of players, if it is, set current player to the first in the list
        if( model.getCurrentPlayer() == model.getPlayers().get(model.getNumPlayers()) ) {
            model.setCurrentPlayer(0);
        }else {
            model.setCurrentPlayer(model.getCurrentPlayer());
        }
        
        
        Player currentPlayer = model.getCurrentPlayer();
        
        
        for( Coordinate c : selection ) {
            Tile t = this.model.getTile(c);
            currentPlayer.getShelf().addTiles(t, col);
        }
        this.model.removeSelection(selection);
        
        
    }
    
}
