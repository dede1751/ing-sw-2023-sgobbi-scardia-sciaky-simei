package it.polimi.ingsw.utils.files;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.*;


/**
 * Collection of utitility function to manage resources
 * All function should be static
 */
public final class ResourcesManager {
    
    public static final String mainResourcesDir = Paths.get("").toAbsolutePath() + "/src/resources";
    public static final String mainRootDir = Paths.get("").toAbsolutePath() + "/src/main/java";
    public static final String testRootDir = Paths.get("").toAbsolutePath() + "/src/test/java";
    
    public static final String recoveryDir = mainResourcesDir + "/controller/recovery";
    
    /**
     * @return the name of the method from which it was called
     */
    public static String getCurrentMethod() {
        StackWalker walker = StackWalker.getInstance();
        return walker.walk((x) -> x.toList().get(1).getMethodName());
    }
    
    public static FileChannel openFileWrite(String path) throws IOException {
        return FileChannel.open(Path.of(path), CREATE, WRITE);
    }
    
    public static FileChannel openFileRead(String path) throws IOException {
        return FileChannel.open(Path.of(path), READ);
    }
    
    public static void closeChannel(Channel c) throws IOException{
        c.close();
    }
    
    /**
     * Utility function to manage Json objects, powered by GSON
     */
    public static class JsonManager {
        
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
            return getElementByAttribute(json, attribute).toString();
        }
        public static JsonElement getElementByAttribute(String json, String attribute) throws JsonParseException{
            var jsonTree = JsonParser.parseString(json).getAsJsonObject();
            if( jsonTree.has(attribute) ) {
                var value = jsonTree.get(attribute);
                var result = new JsonObject();
                result.add(attribute, value);
                return result;
            }else {
                throw new JsonParseException("Attribute not found : " + attribute);
            }
        }
        public static JsonElement getElementByAttribute(JsonElement json, String attribute) throws JsonParseException{
            if( json.isJsonObject() ){
                if(json.getAsJsonObject().has(attribute)){
                    return json.getAsJsonObject().get(attribute);
                }else throw new JsonParseException("Json object doesn't have this attribute : " + attribute);
            }else throw new JsonParseException("Json element is not an object");
        }
    }
    
    
}
