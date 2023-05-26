package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.files.ResourcesManager.GraphicalResources;
import it.polimi.ingsw.view.gui.controllers.LoginController;
import it.polimi.ingsw.view.gui.controllers.MainController;
import it.polimi.ingsw.view.gui.controllers.LocalPlayerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GUIApp extends Application {
    
    
    private static Stage mainStage;
    private static Parent loginRoot;
    private static Parent mainRoot;
    private static LoginController loginControllerInstance;
    
    private static LocalPlayerController localPlayerControllerInstance;
    
    private static MainController mainControllerInstance;
    
    
    private static List<Tile> tileSelection;
    private static List<Coordinate> coordinateSelection;
    
    public static MainController getMainControllerInstance() {
        return mainControllerInstance;
    }
    
    public static List<Coordinate> getCoordinateSelection() {
        return coordinateSelection;
    }
    
    public static void setCoordinateSelection(List<Coordinate> coordinateSelection) {
        GUIApp.coordinateSelection = coordinateSelection;
    }
    
    public static List<Tile> getTileSelection() {
        return tileSelection;
    }
    
    public static void setTileSelection(List<Tile> tileSelection) {
        GUIApp.tileSelection = tileSelection;
    }
    
    
    /**
     * @param stage
     *
     * @throws IOException
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
        FXMLLoader mainLoader = new FXMLLoader(GraphicalResources.getFXML("Main.fxml"));
        mainRoot = mainLoader.load();
        mainControllerInstance = mainLoader.getController();
        
        //set and show stage
        stage.setTitle("My Shelfie");
        stage.setScene(new Scene(loginRoot));
        stage.show();
    }
    
    @Override
    public void init() {
    
    }
    public static Parent getMainRoot() {
        return mainRoot;
    }
    /**
     *
     * @return
     */
    public static Stage getMainStage() {
        return mainStage;
    }
    
    /**
     *
     * @return
     */
    public static LoginController getLoginController() {
        return loginControllerInstance;
    }
    
    
}
