package it.polimi.ingsw.utils.exceptions;

import it.polimi.ingsw.model.Coordinate;

/**
 * Class OutOfBoundCoordinateException is thrown when a coordinate is out of bound.
 */
public class OutOfBoundCoordinateException extends CommonException {
    
    private final Coordinate coordinate;
    
    /**
     * Initialize the exception with the coordinate that caused it.
     *
     * @param coor the coordinate that caused the exception
     */
    public OutOfBoundCoordinateException(Coordinate coor) {
        this.coordinate = coor;
    }
    
    /**
     * Print the coordinate that caused the exception.
     */
    public void printCoordinate() {
        System.err.println("Exception: Coordinate out of Bound\n Invalid Coordinate: " + coordinate.toString());
    }
}
