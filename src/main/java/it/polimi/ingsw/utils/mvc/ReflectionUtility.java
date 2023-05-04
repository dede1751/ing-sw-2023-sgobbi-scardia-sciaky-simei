package it.polimi.ingsw.utils.mvc;

import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

public class ReflectionUtility {
    
    /**
     * Call the method with the given name on the given object, passing the given arguments
     *
     * @param errorListener listener to notify in case of error, null if no error logging is needed
     * @param obj           object on which to call the method
     * @param methodName    name of the method to call
     * @param args          arguments to pass to the method
     */
    public static void invokeMethod(ModelListener errorListener, Object obj, String methodName, Object... args) throws NoSuchMethodException {
        try {
            Method method = obj.getClass().getMethod(methodName, args.getClass());
            method.invoke(obj, args);
        }
        catch( IllegalAccessException | InvocationTargetException e ) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            
            try {
                if( errorListener != null ) {
                    errorListener.update(
                            new ServerResponseMessage(Response.ServerError(args.getClass().getSimpleName())));
                }
            }
            catch( RemoteException ex ) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
}
