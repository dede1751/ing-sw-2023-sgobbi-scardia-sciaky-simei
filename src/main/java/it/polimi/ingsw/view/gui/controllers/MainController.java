package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.utils.files.ResourcesManager;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class MainController {
    
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane gameInterface;
 
    @FXML
    private GameInterfaceController gameInterfaceController;
    
    
    
    @FXML
    public void initialize() {
        scrollPane.setBackground(new Background(new BackgroundImage(
                new Image(ResourcesManager.GraphicalResources.getGraphicalAsset("misc/sfondo_parquet.png")),
                BackgroundRepeat.SPACE, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        
    }
    
    public GameInterfaceController getGameInterfaceController() {
        return gameInterfaceController;
    }
    

    
    public Pane getGameInterface(){
        return gameInterface;
    }
    public ScrollPane getScrollPane(){
        return scrollPane;
    }
    
    public void setScrollPane(double x, double y){
        var content =scrollPane.getContent();
        scrollPane.setContent(new Group(content));
        scrollPane.setPrefViewportWidth(x);
        scrollPane.setPrefViewportHeight(y);
    }
    

}
