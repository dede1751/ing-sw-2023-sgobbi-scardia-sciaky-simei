package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.controller.LobbyController.LobbyView;
import it.polimi.ingsw.view.LocalModel;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUIUtils;
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

import java.util.List;

public class LoginController {
    
    private static final View gui = AppClient.getViewInstance();
    @FXML
    private Text nicknameConfirmBanner;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Text selectNumberBanner;
    @FXML
    private Text createLobbiesBanner;
    @FXML
    private Text joinLobbiesBanner;
    @FXML
    private Text orBanner;
    @FXML
    private Text insertNicknameBanner;
    @FXML
    private ScrollPane scrollPaneLobbies;
    @FXML
    private Text gameWaitingText;
    
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

            if(!lobbyView.isRecovery()) {
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
            }

            this.setSpacing(20);
            this.setBackground(new Background(new BackgroundFill(color, new CornerRadii(1), new Insets(1))));
        }
    }
    
    @FXML
    public void initialize() {
        
        nicknameConfirmBanner.setOpacity(0);
        gameWaitingText.setOpacity(0);
        nPlayerChoice.getItems().add(2);
        nPlayerChoice.getItems().add(3);
        nPlayerChoice.getItems().add(4);
        
        nPlayerChoice.setOnAction((event -> playerNSelected = nPlayerChoice.getSelectionModel().getSelectedItem()));
        
        createLobbyButton.setOnAction(event -> {
            if( waitToJoin )
                return;
            waitToJoin = true;
            gui.notifyCreateLobby(playerNSelected);
        });
        
        scrollPaneLobbies.setOpacity(0.90);
        AppClient.getViewInstance().notifyRequestLobby(null);
        
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
        
        gameWaitingText.setFill(Color.LIGHTGRAY);
        nicknameConfirmBanner.setFill(Color.LIGHTGRAY);
        
        //Update background thread
        GUIUtils.threadPool.submit(() -> {
                                       int i = 0;
                                       while( !LocalModel.getInstance().isStarted() ) {
                                           
                                           int j = i + 1;
                                           dialogPane.setBackground(new Background(
                                                   new BackgroundImage(new Image("gui/assets/Publisher_material/Display_" + j + ".jpg"),
                                                                       BackgroundRepeat.SPACE, BackgroundRepeat.REPEAT,
                                                                       BackgroundPosition.DEFAULT,
                                                                       BackgroundSize.DEFAULT)));
                                           
                                           //noinspection BusyWait
                                           try {
                                               Thread.sleep(10000);
                                           }
                                           catch( InterruptedException ignored ) {
                                           }
                                           i = (i + 1) % 5;
                                       }
                                   }
        );
        
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
        GUIUtils
                .threadPool
                .submit(() -> {
                            if( waitToJoin )
                                return;
                            String nickname = this.nickname.getText();
                            if( nickname == null || nickname.equals("") )
                                return;
                            gui.setNickname(nickname);
                            Platform.runLater(() -> {
                                nicknameConfirmBanner.setText("Nickname :" + nickname);
                                nicknameConfirmBanner.setOpacity(1);
                                this.nickname.setText("");
                            });
                            if( gui.getLobbies().stream().anyMatch(
                                    (l) -> l.isRecovery() && l.nicknames().contains(nickname)) ) {
                                waitToJoin = true;
                                gui.notifyRecoverLobby();
                            }
                        }
                );
    }
    
    @FXML
    public void refreshLobbies(ActionEvent event) {
        gui.notifyRequestLobby(null);
    }
    
    public void waitingGameAnimation() {
        try {
            int i = 0;
            Platform.runLater(() -> {
                gameWaitingText.setOpacity(1);
                gameWaitingText.setFill(Color.DARKBLUE);
                
            });
            while( !LocalModel.getInstance().isStarted() ) {
                
                String waiting = "Waiting for other players";
                String points = " .";
                String repeated = new String(new char[i * 2]).replace("\0", points);
                Platform.runLater(() -> gameWaitingText.setText(waiting + repeated));
                i = (i + 1) % 4;
                //noinspection BusyWait
                Thread.sleep(800);
            }
        }
        catch( InterruptedException ignored ) {
        
        }
    }
    
}
