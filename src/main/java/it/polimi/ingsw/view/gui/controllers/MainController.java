package it.polimi.ingsw.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class MainController {
    @FXML
    public Pane gameInterface;
    @FXML
    private GameInterfaceController gameInterfaceController;
    
    @FXML
    public void initialize(){
    
    }
    
    public GameInterfaceController getGameInterfaceController() {
        return gameInterfaceController;
    }
}
