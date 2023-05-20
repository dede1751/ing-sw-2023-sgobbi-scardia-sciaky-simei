package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class GUIApp extends Application {
    
    
    private GUI guiContext;
    
    /**
     * @param guiContext
     */
    public GUIApp(GUI guiContext) {
        this.guiContext = guiContext;
    }
    
    public GUIApp() {
    
    }
    
    /**
     * @param stage
     *
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/myshelfie.fxml"));
        stage.setTitle("My Shelfie");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    
}
