package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.controller.LobbyController.LobbyView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class loginController {
    
    
    private class LobbyViewGraphical extends HBox{
        
        public LobbyViewGraphical(LobbyView lobbyView){
           this.getChildren().add(new Text(lobbyView.toString()));
           
           Button joinButton = new Button("JOIN");
           joinButton.setOnAction(actionEvent -> {
               AppClient.getViewInstance().
           });
           this.getChildren().add(joinButton);
        }
    }
    
    
    
    
    
    @FXML
    public void updateLobbies(List<LobbyView> lobbyViewList){
    
    }
    
    
    
}
