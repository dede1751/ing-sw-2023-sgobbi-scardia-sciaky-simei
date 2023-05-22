package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.utils.files.ResourcesManager.GraphicalResources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApp extends Application {
    
    
    /**
     * @param stage
     *
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(GraphicalResources.getFXML("main.fxml"));
        stage.setTitle("My Shelfie");
        stage.setScene(new Scene(root));
        stage.show();
        
    }
    
    @Override
    public void init() {
    
    }
    
}
