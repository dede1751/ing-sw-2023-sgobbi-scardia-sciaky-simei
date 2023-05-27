package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.controller.LobbyController.LobbyView;
import it.polimi.ingsw.view.LocalModel;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUIApp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.scene.paint.Paint;

import java.util.List;

public class LoginController {
    
    private static final View gui = AppClient.getViewInstance();
    public DialogPane dialogPane;
    public Text selectNumberBanner;
    public Text createLobbiesBanner;
    public Text joinLobbiesBanner;
    public Text orBanner;
    public Text insertNicknameBanner;
    public ScrollPane scrollPaneLobbies;
    
    private boolean waitToJoin = false;
    
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
    
    public void setWaitToJoin(boolean b) {
        waitToJoin = b;
    }
    
    
    private class LobbyViewGraphical extends HBox {
        
        public LobbyViewGraphical(LobbyView lobbyView, Color color) {
            this.getChildren().add(new Text(lobbyView.toString()));
            
            Button joinButton = new Button("JOIN");
            joinButton.setOnAction(actionEvent -> {
                if( waitToJoin )
                    return;
                waitToJoin = true;
                gui.notifyJoinLobby(lobbyView.lobbyID());
            });
            joinButton.setLayoutY(20);
            joinButton.setLayoutX(this.getHeight() / 2);
            this.getChildren().add(joinButton);
            this.setSpacing(20);
            this.setBackground(new Background(new BackgroundFill(color, new CornerRadii(1), new Insets(1))));
        }
    }
    
    @FXML
    public void initialize() {
        nPlayerChoice.getItems().add(2);
        nPlayerChoice.getItems().add(3);
        nPlayerChoice.getItems().add(4);
        nPlayerChoice.setOnAction((event -> {
            playerNSelected = nPlayerChoice.getSelectionModel().getSelectedItem();
        }));
        createLobbyButton.setOnAction(event -> {
            if( waitToJoin )
                return;
            waitToJoin = true;
            gui.notifyCreateLobby(playerNSelected);
        });
        scrollPaneLobbies.setOpacity(0.90);
        AppClient.getViewInstance().notifyRequestLobby(null);
        new Thread(() -> {
            Object obj = new Object();
            Platform.runLater(() -> {
                selectNumberBanner.setFill(Color.LIGHTGRAY);
                selectNumberBanner.setFont(new Font("Noto Sans", 15));
                createLobbiesBanner.setFill(Color.LIGHTGRAY);
                createLobbiesBanner.setFont(new Font("Noto Sans", 15));
                orBanner.setFill(Color.LIGHTGRAY);
                orBanner.setFont(new Font("Noto Sans", 15));
                joinLobbiesBanner.setFill(Color.LIGHTGRAY);
                joinLobbiesBanner.setFont(new Font("Noto Sans", 15));
                insertNicknameBanner.setFill(Color.LIGHTGRAY);
                insertNicknameBanner.setFont(new Font("Noto Sans", 15));
            });
            int i = 0;
            synchronized(obj) {
                while( !LocalModel.getInstance().isStarted() ) {
                    

                    int j = i+1;
                    dialogPane.setBackground(new Background(
                            new BackgroundImage(new Image("gui/assets/Publisher_material/Display_" + j + ".jpg"),
                                                BackgroundRepeat.SPACE, BackgroundRepeat.REPEAT,
                                                BackgroundPosition.DEFAULT,
                                                BackgroundSize.DEFAULT)));
                    
                    try {
                        obj.wait(10000);
                    }
                    catch( InterruptedException ignored ) {
                    }
                    i = (i+1)%5;
                }
            }
        }).start();
        
    }
    
    @FXML
    public void updateLobbies(List<LobbyView> lobbyViewList) {
        this.lobbyViewList.getChildren().clear();
        int color = 0;
        for( var l : lobbyViewList ) {
            this.lobbyViewList.getChildren().add(new LobbyViewGraphical(l, backgroundColorArray.get(color % 2)));
            color++;
        }
    }
    
    @FXML
    public void setNickname(ActionEvent event) {
        String nickname = this.nickname.getText();
        gui.setNickname(nickname);
        if( gui.getLobbies().stream().anyMatch((l) -> l.isRecovery() && l.nicknames().contains(nickname)) ) {
            gui.notifyRecoverLobby();
        }
    }
    
    @FXML
    public void refreshLobbies(ActionEvent event) {
        gui.notifyRequestLobby(null);
    }
    
}
