package it.polimi.ingsw.utils.mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Class ReflectionUtility provides utility methods for reflection. <br>
 * Reflection is mainly used to call the correct message-handling methods when receiving data from the network.
 */
public class ReflectionUtility {
    
    /**
     * Private unused constructor to appease Javadoc.
     */
    private ReflectionUtility(){}
    
    /**
     * Check if the given object has a method with the given name and arguments
     *
     * @param obj        object to check
     * @param methodName name of the method to check
     * @param args       arguments to pass to the method
     *
     * @return true if the object has a method with the given name and arguments, false otherwise
     */
    public static boolean hasMethod(Object obj, String methodName, Object... args) {
        try {
            Class<?>[] types = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
            obj.getClass().getMethod(methodName, types);
            return true;
        }
        catch( NoSuchMethodException e ) {
            return false;
        }
    }
    
    /**
     * Call the method with the given name on the given object, passing the given arguments
     *
     * @param obj        object on which to call the method
     * @param methodName name of the method to call
     * @param args       arguments to pass to the method
     *
     * @throws NoSuchMethodException if the method does not exist
     */
    public static void invokeMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException {
        try {
            Class<?>[] types = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
            Method method = obj.getClass().getMethod(methodName, types);
            method.invoke(obj, args);
        }
        // OnMessage methods do not throw exceptions and are public. None of these exceptions should ever be raised.
        catch( IllegalAccessException | InvocationTargetException e ) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
