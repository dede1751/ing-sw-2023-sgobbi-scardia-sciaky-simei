package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.controller.LobbyController.LobbyView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUIApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.scene.paint.Paint;
import java.util.List;

public class LoginController {
    
    private static final View gui = AppClient.getViewInstance();
    
    private int playerNSelected = 2;
    
    @FXML
    private VBox lobbyViewList;
    
    @FXML
    private TextField nickname;
    
    @FXML
    private Button nicknameButton;
    
    @FXML
    private Button refreshLobbies;
    
    @FXML
    private Button createLobbyButton;
    
    @FXML
    private ChoiceBox<Integer> nPlayerChoice;
    
    private final List<Color> backgroundColorArray = List.of(Color.CYAN, Color.LIGHTBLUE);
    
    
    private static class LobbyViewGraphical extends HBox{
        
        public LobbyViewGraphical(LobbyView lobbyView, Color color){
           this.getChildren().add(new Text(lobbyView.toString()));
           
           Button joinButton = new Button("JOIN");
           joinButton.setOnAction(actionEvent -> {
               gui.notifyJoinLobby(lobbyView.lobbyID());
           });
           joinButton.setLayoutY(20);
           joinButton.setLayoutX(this.getHeight()/2);
           this.getChildren().add(joinButton);
           this.setSpacing(20);
           this.setBackground(new Background(new BackgroundFill(color, new CornerRadii(1), new Insets(1))));
        }
    }
    
    @FXML
    public void initialize(){
        nPlayerChoice.getItems().add(2);
        nPlayerChoice.getItems().add(3);
        nPlayerChoice.getItems().add(4);
        nPlayerChoice.setOnAction((event -> {
            playerNSelected = nPlayerChoice.getSelectionModel().getSelectedItem();
        }));
        createLobbyButton.setOnAction(event -> gui.notifyCreateLobby(playerNSelected));
        AppClient.getViewInstance().notifyRequestLobby(null);
    }
    
    @FXML
    public void updateLobbies(List<LobbyView> lobbyViewList){
        this.lobbyViewList.getChildren().clear();
        int color = 0;
        for(var l : lobbyViewList){
            this.lobbyViewList.getChildren().add(new LobbyViewGraphical(l, backgroundColorArray.get(color%2)));
            color++;
        }
    }
    
    @FXML
    public void setNickname(ActionEvent event){
        String nickname = this.nickname.getText();
        gui.setNickname(nickname);
        if( gui.getLobbies().stream().anyMatch((l) -> l.isRecovery() && l.nicknames().contains(nickname)) ) {
            gui.notifyRecoverLobby();
        }
    }
    @FXML
    public void refreshLobbies(ActionEvent event){
        gui.notifyRequestLobby(null);
    }
    
    
    
    
    
    
    
}
