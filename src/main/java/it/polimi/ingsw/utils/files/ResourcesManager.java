package it.polimi.ingsw.utils.files;

import com.google.gson.*;
import it.polimi.ingsw.model.GameModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;


/**
 * Class ResourcesManager is responsible for all interactions with the file system and generic server resources.
 */
public final class ResourcesManager {
    
    public static final String testRootDir = Paths.get("src/test/java/it/polimi/ingsw").toString();
    
    public static final String mainResourcesDir = Paths.get("resources").toString();
    
    public static final String recoveryDir = mainResourcesDir + "/recovery";
    
    public static final String serverLoggerDir = mainResourcesDir + "/server";
    
    public static final String clientLoggerDir = mainResourcesDir + "/client";
    
    
    /**
     * Get the name of the method currently being executed.<br>
     * Used to fetch resource files for specific testing methods.
     * @return the name of the method from which it was called
     */
    public static String getCurrentMethod() {
        StackWalker walker = StackWalker.getInstance();
        return walker.walk((x) -> x.toList().get(1).getMethodName());
    }
    
    /**
     * Open a file for writing, creating the directory if necessary
     *
     * @param dir  the directory in which to create the file
     * @param file the name of the file to create
     *
     * @return the FileChannel of the created file
     *
     * @throws IOException if the file cannot be created (not if the directory already exists)
     */
    public static FileChannel openFileWrite(String dir, String file) throws IOException {
        Files.createDirectories(Paths.get(dir));
        return FileChannel.open(Path.of(dir, file), CREATE, WRITE);
    }
    
    /**
     * Fetch the directory containing recovery files. If it does not exist, create it.
     * @return the recovery directory if it exists or was created, null otherwise
     */
    private static File getRecoveryDir() {
        File dir = new File(ResourcesManager.recoveryDir);
        
        if( !dir.exists() && !dir.mkdir() ) {
            ServerLogger.errorLog(new IOException("Unable to create recovery directory"));
            return null;
        }
        return dir;
    }
    
    /**
     * Get a list of all saved models within the recovery directory
     *
     * @return a list of all saved models, at most empty if none are found
     */
    public static List<GameModel> getSavedModels() {
        List<GameModel> models = new ArrayList<>();
        File dir = getRecoveryDir();
        if( dir == null ) {
            return models;
        }
        
        for( File file : Objects.requireNonNull(dir.listFiles()) ) {
            try {
                if( !file.getName().equals(".gitignore") ) {
                    String modelJson = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(GameModel.class, new GameModel.ModelDeserializer())
                            .create();
                    
                    models.add(gson.fromJson(modelJson, GameModel.class));
                    
                    // delete old recovery files
                    if( !file.delete() ) {
                        ServerLogger.errorLog(new IOException("Unable to delete recovery file: " + file.getName()));
                    }
                }
            }
            catch( IOException e ) {
                ServerLogger.errorLog(e, "Unable to read recovery file: " + file.getName());
            }
        }
        return models;
    }
    
    /**
     * Save a model to the recovery directory
     *
     * @param model   the model to save
     * @param lobbyID the lobbyID of the model (used as filename)
     */
    public static void saveModel(GameModel model, int lobbyID) {
        try {
            ResourcesManager.getRecoveryDir();
            model.toJson(ResourcesManager.recoveryDir + "/" + lobbyID + ".json");
        }
        catch( IOException e ) {
            ServerLogger.errorLog(e, "Unable to save model");
        }
    }
    
    /**
     * Delete a model from the recovery directory
     *
     * @param lobbyID the lobbyID of the model to delete (used as filename)
     */
    public static void deleteModel(int lobbyID) {
        File dir = getRecoveryDir();
        if( dir == null ) {
            return;
        }
        
        File file = new File(dir, lobbyID + ".json");
        if( !file.delete() ) {
            ServerLogger.errorLog(new IOException("Unable to delete recovery file: " + file.getName()));
        }
    }
    
    /**
     * Utility function to manage Json objects, powered by GSON.
     */
    public static class JsonManager {
        
        /**
         * Search for the attribute in the first layer of the json's tree and return the entire subtree.
         * Wrapper for {@link #getElementByAttribute(String, String)}.
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
        
        /**
         * Get json element by attribute name from the first level of the json tree.
         *
         * @param json json string to search in
         * @param attribute parameter name to search for
         * @return the json element of the attribute
         * @throws JsonParseException if the attribute is not found in the json root node
         */
        public static JsonElement getElementByAttribute(String json, String attribute) throws JsonParseException {
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
        
        /**
         * Get json element by attribute name from the first level of the json tree.
         *
         * @param json json element to search in
         * @param attribute parameter name to search for
         * @return the json element of the attribute
         * @throws JsonParseException if the attribute is not found in the json root node or the element is not a json object
         */
        public static JsonElement getElementByAttribute(JsonElement json, String attribute) throws JsonParseException {
            if( json.isJsonObject() ) {
                if( json.getAsJsonObject().has(attribute) ) {
                    return json.getAsJsonObject().get(attribute);
                }else
                    throw new JsonParseException("Json object doesn't have this attribute : " + attribute);
            }else
                throw new JsonParseException("Json element is not an object");
        }
    }
    
    /**
     * Utility class to manage graphical resources used in the GUI.
     */
    public static class GraphicalResources {
        
        /**
         * Get the valid URL object of the indicated fxml file. <br>
         * The name must have the format [name].fxml. The method throws a RuntimeException if the file doesn't exist or the name is malformed.
         *
         * @param name Name of the .fxml file
         *
         * @return The absolute URL referencing the [name].fxml file
         */
        public static URL getFXML(String name) {
                return ResourcesManager.class.getClassLoader().getResource("gui/javafx/" + name);
        }

    }
    
    
}
