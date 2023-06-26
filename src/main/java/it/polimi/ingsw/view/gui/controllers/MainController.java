package it.polimi.ingsw.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
/**
 
 The MainController class is responsible for managing the main game interface.
 
 It controls the scroll pane, game interface, and game interface controller.
 */
public class MainController {
    
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private HBox gameInterface;
    
    @FXML
    private GameInterfaceController gameInterfaceController;
    
    @FXML
    private AnchorPane anchorPane;
    /**
     
     Initializes the main game interface.
     Configures the anchor pane to fit the scroll pane.
     */
    @FXML
    public void initialize() {
        
        
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
    }
    /**
     
     Returns the game interface controller.
     @return The game interface controller.
     */
    public GameInterfaceController getGameInterfaceController() {
        return gameInterfaceController;
    }
    /**
     
     Returns the scroll pane.
     @return The scroll pane.
     */
    public ScrollPane getScrollPane() {
        return scrollPane;
    }
    
    
}
