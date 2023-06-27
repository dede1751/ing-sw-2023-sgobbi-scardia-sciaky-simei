package it.polimi.ingsw.utils.mvc;

import it.polimi.ingsw.model.messages.ModelMessage;


/**
 * Interface ModelListener defines the methods that must be implemented by each model listener.
 */
public interface ModelListener {
    
    /**
     * Notify the listener of a model message
     *
     * @param msg Message from the server or describing a model change
     */
    void update(ModelMessage<?> msg);
    
}
