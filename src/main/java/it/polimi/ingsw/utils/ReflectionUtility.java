package it.polimi.ingsw.utils;

import java.lang.reflect.Method;

public class ReflectionUtility {
    
    public static Method GetMethod(Class<?> origin, String name, Class<?> object) throws NoSuchMethodException {
        var list = origin.getMethods();
        for( var x : list ) {
            if( x.getName().equals(name) &&
                x.getParameterCount() == 1 && (
                        x.getParameterTypes()[0].equals(object) ||
                        x.getParameterTypes()[0].equals(object.getSuperclass())
                )
            )
                return x;
        }
        throw new NoSuchMethodException(origin.getCanonicalName() + name + "(" + object.getCanonicalName() + ")");
    }
}
