package it.polimi.ingsw.utils.exceptions;

public class CommonException extends Exception {
    
    public CommonException() {
        super();
    }
    
    public CommonException(String message) {
        super(message);
    }
    
    //not sure that it will always work
    public void stackPrintOrigin() {
        int depth_level = 1;
        System.err.println("===============");
        System.err.println("ORIGIN : " + this);
        System.err.println("\nTrace: " + "\n" +
                           " file : " + new Throwable().getStackTrace()[depth_level].getFileName() + "\n" +
                           " class : " + new Throwable().getStackTrace()[depth_level].getClassName() + "\n" +
                           " method : " + new Throwable().getStackTrace()[depth_level].getMethodName() + "\n" +
                           " line : " + new Throwable().getStackTrace()[depth_level].getLineNumber() + "\n");
    }
}
