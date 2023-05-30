package it.polimi.ingsw.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

public class MainController {
    @FXML
    private Pane gameInterface;
    @FXML
    private Tab gameTab;
    @FXML
    private GameInterfaceController gameInterfaceController;
    
    @FXML
    public ChatController chatController;
    
    @FXML
    public void initialize() {
    
    }
    
    public GameInterfaceController getGameInterfaceController() {
        return gameInterfaceController;
    }
    
    public ChatController getChatController() {
        return chatController;
    }
    
    
    public Tab getGameTab() {
        return gameTab;
    }
}
