package it.polimi.ingsw.utils.exceptions;

import it.polimi.ingsw.model.Coordinate;

public class OutOfBoundCoordinateException extends CommonException {
    
    private Coordinate coordinate;
    public OutOfBoundCoordinateException(Coordinate coor) {
        this.coordinate = coor;
    }
    
    public void printCoordinate(){
        System.err.println("Exception: Coordinate out of Bound\n Invalid Coordinate: " + coordinate.toString());
    }
    
    public OutOfBoundCoordinateException() {
        this.printStackTrace(System.err);
    }
}
