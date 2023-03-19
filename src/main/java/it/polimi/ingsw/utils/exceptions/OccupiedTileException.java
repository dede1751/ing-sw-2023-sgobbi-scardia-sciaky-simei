package it.polimi.ingsw.utils.exceptions;

public class OccupiedTileException extends CommonException {
    public OccupiedTileException() {
        System.err.println("Exception : Trying to insert a tile in an already occupied coordinate");
        this.printStackTrace(System.err);
    }
    
}
