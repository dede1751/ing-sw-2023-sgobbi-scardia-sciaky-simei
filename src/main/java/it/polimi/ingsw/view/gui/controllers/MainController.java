package it.polimi.ingsw.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

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
