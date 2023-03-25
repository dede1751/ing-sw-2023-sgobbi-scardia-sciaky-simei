package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Tile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class TurnMessageListener implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        
        String propertyName = evt.getPropertyName();
        Object oldValue = evt.getOldValue();
        Object newValue = evt.getNewValue();
        System.out.println("Property " + propertyName + " changed from " + oldValue + " to " + newValue);
    }
    
    
}

