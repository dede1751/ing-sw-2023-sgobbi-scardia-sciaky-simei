package it.polimi.ingsw.utils.exceptions;

public class InvalidStringException extends CommonException {
    public InvalidStringException() {
        System.err.println("Exception : Invalid String");
        this.printStackTrace(System.err);
    }
}
