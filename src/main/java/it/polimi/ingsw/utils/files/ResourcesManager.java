package it.polimi.ingsw.utils.files;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.nio.file.Paths;


/**
 * Collection of utitility function to manage resources
 * All function should be static
 */
public final class ResourcesManager {
    public static final String mainRootDir = Paths.get("").toAbsolutePath() + "/src/main/java";
    public static final String testRootDir = Paths.get("").toAbsolutePath() + "/src/test/java";
    
    /**
     * @return the name of the method from which it was called
     */
    public static String getCurrentMethod() {
        StackWalker walker = StackWalker.getInstance();
        return walker.walk((x) -> x.toList().get(1).getMethodName());
    }
    
    /**
     * Utility function to manage Json objects, powered by GSON
     */
    public class JsonManager {
        //TODO fixing this comment
        
        /**
         * search for the attribute in the first layer of the json's tree and return the child node
         *
         * @param json      json string to search in
         * @param attribute parameter name to search for
         *
         * @return the subtree of the attribute inside the json string
         *
         * @throws JsonParseException if the attribute is not found in the json root node
         */
        public static String getObjectByAttribute(String json, String attribute) throws JsonParseException {
            var jsonTree = JsonParser.parseString(json).getAsJsonObject();
            if( jsonTree.has(attribute) ) {
                var value = jsonTree.get(attribute);
                var result = new JsonObject();
                result.add(attribute, value);
                return result.toString();
            }else {
                throw new JsonParseException("Attribute not found");
            }
        }
    }
    
    
}
