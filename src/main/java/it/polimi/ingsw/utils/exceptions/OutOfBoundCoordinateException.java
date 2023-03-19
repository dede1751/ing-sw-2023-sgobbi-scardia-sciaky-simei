package it.polimi.ingsw.utils.exceptions;

import it.polimi.ingsw.model.Coordinate;

public class OutOfBoundCoordinateException extends CommonException {
    public OutOfBoundCoordinateException(Coordinate coor) {
        System.err.println("Exception: Coordinate out of Bound\n Invalid Coordinate: " + coor.toString());
        this.printStackTrace(System.err);
    }
    
    public OutOfBoundCoordinateException() {
        this.printStackTrace(System.err);
    }
}
