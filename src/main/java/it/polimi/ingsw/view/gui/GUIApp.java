package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.files.ResourcesManager.GraphicalResources;
import it.polimi.ingsw.view.gui.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApp extends Application {
    
    
    private static Stage mainStage;
    private static LoginController loginControllerInstance;
    
    /**
     * @param stage
     *
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(GraphicalResources.getFXML("main.fxml"));
        Parent root = fxmlLoader.load();
      //  loginControllerInstance = fxmlLoader.getController();
        stage.setTitle("My Shelfie");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @Override
    public void init() {
    
    }
    
    public static Stage getMainStage() {
        return mainStage;
    }
    
    public static LoginController getLoginController() {
        return loginControllerInstance;
    }
    
    
}
