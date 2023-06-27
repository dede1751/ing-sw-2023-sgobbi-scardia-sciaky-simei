package it.polimi.ingsw.utils.exceptions;

/**
 * Exception thrown when a tile is inserted in an already occupied coordinate. <br>
 * Thrown when inserting tiles into the board.
 */
public class OccupiedTileException extends CommonException {
    
    /**
     * Initialize an empty OccupiedTileException object.
     */
    public OccupiedTileException() {
        System.err.println("Exception : Trying to insert a tile in an already occupied coordinate");
        this.printStackTrace(System.err);
    }
    
}
