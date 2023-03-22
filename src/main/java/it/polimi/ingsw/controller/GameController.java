package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.view.VirtualView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class GameController implements PropertyChangeListener {
    
    private final GameModel model;
    
    private final VirtualView view;
    
    private int numPlayers;
    
    
    /* constructor tobe used for paused game*/
    public GameController(GameModel model, VirtualView view) {
        this.model = model;
        this.view=view;
        
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
}
