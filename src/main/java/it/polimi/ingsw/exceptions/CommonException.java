package it.polimi.ingsw.exceptions;

public class CommonException extends Exception {
    // it doesn't work, I know
    public static void stackPrint() {
        System.err.println("Trace: " +
                           "file " + new Throwable().getStackTrace()[1].getFileName() +
                           " class " + new Throwable().getStackTrace()[1].getClassName() +
                           " method " + new Throwable().getStackTrace()[1].getMethodName() +
                           " line " + new Throwable().getStackTrace()[1].getLineNumber());
    }
}
