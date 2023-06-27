package it.polimi.ingsw.utils.exceptions;

/**
 * CommonException is the base class for all exceptions in the project.
 */
public class CommonException extends Exception {
    
    /**
     * Initialize an empty CommonException object.
     */
    public CommonException() {
        super();
    }
    
    /**
     * Print a stack trace of the exception.
     */
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
