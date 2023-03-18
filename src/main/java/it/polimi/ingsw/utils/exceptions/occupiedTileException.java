package it.polimi.ingsw.utils.exceptions;

import it.polimi.ingsw.exceptions.CommonException;

public class occupiedTileException extends CommonException {
    public occupiedTileException() {
        System.err.println("Trying to insert a tile in an already occupied coordinate");
        this.printStackTrace(System.err);
    }
    
}
