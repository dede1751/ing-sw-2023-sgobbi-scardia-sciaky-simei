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
    private HBox gameInterface;
    
    @FXML
    private GameInterfaceController gameInterfaceController;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    public void initialize() {
        
        
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
    }
    
    public GameInterfaceController getGameInterfaceController() {
        return gameInterfaceController;
    }
    
    public ScrollPane getScrollPane() {
        return scrollPane;
    }
    
    
}
