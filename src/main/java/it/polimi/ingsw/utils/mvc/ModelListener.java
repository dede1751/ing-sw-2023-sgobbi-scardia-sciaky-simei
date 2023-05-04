package it.polimi.ingsw.utils.mvc;

import it.polimi.ingsw.model.messages.ModelMessage;


/**
 * Interface ModelListener defines the methods that must be implemented by each model listener.
 */
public interface ModelListener {
    
    void update(ModelMessage<?> msg);
    
}
