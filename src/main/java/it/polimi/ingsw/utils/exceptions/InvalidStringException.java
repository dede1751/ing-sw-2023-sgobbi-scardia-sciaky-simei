package it.polimi.ingsw.utils.exceptions;

public class InvalidStringException extends CommonException {
    public InvalidStringException() {
        super();
    }
    
    public void printMessage(String msg) {
        System.err.println("Exception: Invalid string\n" + msg);
        this.stackPrintOrigin();
    }
}
