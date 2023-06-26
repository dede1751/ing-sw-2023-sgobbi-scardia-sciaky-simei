package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.files.ResourcesManager.GraphicalResources;
import it.polimi.ingsw.view.gui.controllers.LocalPlayerController;
import it.polimi.ingsw.view.gui.controllers.LoginController;
import it.polimi.ingsw.view.gui.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * The GUIApp class extends the Application class and represents the main entry point for the GUI application.
 */
public class GUIApp extends Application {
    
    
    private static Stage mainStage;
    private static Parent loginRoot;
    private static Parent mainRoot;
    private static LoginController loginControllerInstance;
    
    private static LocalPlayerController localPlayerControllerInstance;
    
    private static MainController mainControllerInstance;
    
    
    private static List<Tile> tileSelection;
    private static List<Coordinate> coordinateSelection;
    
    /**
     * Returns the instance of the MainController.
     *
     * @return The MainController instance.
     */
    public static MainController getMainControllerInstance() {
        return mainControllerInstance;
    }
    /**
     * Returns the coordinate selection.
     *
     * @return The list of selected coordinates.
     */
    public static List<Coordinate> getCoordinateSelection() {
        return coordinateSelection;
    }
    /**
     * Sets the coordinate selection.
     *
     * @param coordinateSelection The list of selected coordinates.
     */
    public static void setCoordinateSelection(List<Coordinate> coordinateSelection) {
        GUIApp.coordinateSelection = coordinateSelection;
    }/**
     * Returns the tile selection.
     *
     * @return The list of selected tiles.
     */
    
    public static List<Tile> getTileSelection() {
        return tileSelection;
    }
    /**
     * Sets the tile selection.
     *
     * @param tileSelection The list of selected tiles.
     */
    public static void setTileSelection(List<Tile> tileSelection) {
        GUIApp.tileSelection = tileSelection;
    }
    
    
    /**
     * Starts the GUI application.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an I/O exception occurs.
     */
    @Override
    public void start(Stage stage) throws IOException {
        
        //set main stage
        mainStage = stage;
        
        //load login page
        FXMLLoader loginLoader = new FXMLLoader(GraphicalResources.getFXML("login.fxml"));
        loginRoot = loginLoader.load();
        loginControllerInstance = loginLoader.getController();
        
        //load main game page
        FXMLLoader mainLoader = new FXMLLoader(GraphicalResources.getFXML("main.fxml"));
        mainRoot = mainLoader.load();
        mainControllerInstance = mainLoader.getController();
        
        
        Scene scene = new Scene(loginRoot);
        
        //set and show stage
        stage.setTitle("My Shelfie");
        stage.setScene(scene);
        
        stage.setMaxHeight(490.0);
        stage.setMaxWidth(936.0);
        stage.show();
    }
    
    /**
     * Initializes the application.
     */
    @Override
    public void init() {
    
    }
    /**
     * Returns the main root.
     *
     * @return The main root.
     */
    public static Parent getMainRoot() {
        return mainRoot;
        
    }
    
    /**
     * Returns the main stage.
     *
     * @return The main stage.
     */
    public static Stage getMainStage() {
        return mainStage;
    }
    
    /**
     * Returns the login controller instance.
     *
     * @return The LoginController instance.
     */
    public static LoginController getLoginController() {
        return loginControllerInstance;
    }
    
    
}
