package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.utils.files.ResourcesManager;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class MainController {
    
    @FXML
    private ScrollPane scrollPane;
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
        scrollPane.setBackground(new Background(new BackgroundImage(
                new Image(ResourcesManager.GraphicalResources.getGraphicalAsset("misc/sfondo_parquet.png")),
                BackgroundRepeat.SPACE, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
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
