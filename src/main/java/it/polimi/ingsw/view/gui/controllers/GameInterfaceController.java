package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.view.LocalModel;
import it.polimi.ingsw.view.gui.GUIApp;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

import java.util.List;
import java.util.Objects;
/**
 
 The GameInterfaceController class is responsible for managing the game interface screen in the GUI.
 
 It handles various components such as the game board, player shelves, and chat.
 */
public class GameInterfaceController {
    
    @FXML
    private ChatController chatController;
    @FXML
    private VBox boardAndPlayer;
    @FXML
    private VBox chatVbox;
    @FXML
    private AnchorPane chat;
    @FXML
    private Text currentPlayer;
    @FXML
    private Button x075;
    @FXML
    private HBox rootHBox;
    @FXML
    private Button x1;
    @FXML
    private Button x125;
    @FXML
    private Button x150;
    @FXML
    private VBox otherShelfVbox;
    @FXML
    private AnchorPane board;
    @FXML
    private BoardController boardController;
    @FXML
    private Pane otherShelf1;
    @FXML
    private Pane otherShelf2;
    @FXML
    private Pane otherShelf3;
    @FXML
    private OtherShelfController otherShelf1Controller;
    @FXML
    private OtherShelfController otherShelf2Controller;
    @FXML
    private OtherShelfController otherShelf3Controller;
    @FXML
    private AnchorPane localPlayer;
    @FXML
    private LocalPlayerController localPlayerController;
    
    
    private List<OtherShelfController> shelfControllerList;
    private List<Pane> shelfList;
    
    private double scale;
    
    /**
     * Constructs a new GameInterfaceController.
     */
    public GameInterfaceController() {
    
    }
    
    
    /**
     
     Initializes the game interface screen.
     Configures button actions, opacity, and player labels.
     */
    @FXML
    public void initialize() {
        scale = 1;
        x075.setOnAction(event -> changeSize(0.75));
        x1.setOnAction(event -> changeSize(1));
        x125.setOnAction(event -> changeSize(1.25));
        x150.setOnAction(event -> changeSize(1.5));
        otherShelf1.setOpacity(0);
        otherShelf2.setOpacity(0);
        otherShelf3.setOpacity(0);
        shelfList = List.of(otherShelf1, otherShelf2, otherShelf3);
        shelfControllerList = List.of(otherShelf1Controller, otherShelf2Controller, otherShelf3Controller);
        currentPlayer.setFont(Font.font("Noto Sans", FontWeight.MEDIUM, 18));
        currentPlayer.setFill(Color.GOLD);
    }
    
    /**
     
     Initializes the player shelves with their respective nicknames, shelves, and scores.
     @param nicknames Ordered list of all other players' nicknames.
     @param shelves List of shelves for each player.
     */
    public void initializeShelves(List<String> nicknames, List<Shelf> shelves) {
        LocalModel model = LocalModel.getInstance();
        for( int i = 0; i < nicknames.size(); i++ ) {
            shelfControllerList.get(i).setPlayerName(nicknames.get(i));
            shelfControllerList.get(i).updateOtherShelf(shelves.get(i));
            shelfControllerList.get(i).setScore(model.getPoints(nicknames.get(i)));
            
            shelfList.get(i).setOpacity(1);
        }
        localPlayerController.setScore(model.getPoints(AppClient.getViewInstance().getNickname()));
    }
    /**
     
     Updates the shelf of a specific player with the given shelf and nickname.
     @param shelf The updated shelf for the player.
     @param nickname The nickname of the player.
     */
    public void updateShelf(Shelf shelf, String nickname) {
        shelfControllerList.stream().filter((x) -> x.getPlayerNickname().equals(nickname)).forEach(
                (x) -> x.updateOtherShelf(shelf));
    }
    
    /**
     
     Changes the scale of the game interface.
     
     @param x The new scale value.
     */
    private void changeSize(double x) {
        
        if( x != scale ) {
            scale = x;
            Scale scale = new Scale();
            scale.setPivotX(0);
            scale.setPivotY(0);
            scale.setX(x);
            scale.setY(x);
            
            rootHBox.getTransforms().setAll(scale);
            rootHBox.layout();
            
            Group scaledContentGroup = new Group(rootHBox);
            ScrollPane scrollPane = GUIApp.getMainControllerInstance().getScrollPane();
            scrollPane.setContent(scaledContentGroup);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);
        }
    }
    
    /**
     
     Updates the score of a player with the given score and nickname.
     @param score The updated score.
     @param nickname The nickname of the player.
     */
    public void updateScore(int score, String nickname) {
        
        boolean flag = true;
        for( OtherShelfController otherShelfController : shelfControllerList ) {
            if( Objects.equals(otherShelfController.getPlayerNickname(), nickname) ) {
                otherShelfController.setScore(score);
                flag = false;
            }
            
        }
        if( flag ) {
            localPlayerController.setScore(score);
        }
        
        
    }
    /**
     
     Returns the local player controller.
     @return The local player controller.
     */
    public LocalPlayerController getLocalPlayerController() {
        return localPlayerController;
    }
    /**
     
     Returns the board controller.
     @return The board controller.
     */
    public BoardController getBoardController() {
        return boardController;
    }
    /**
     
     Returns the chat controller.
     @return The chat controller.
     */
    public ChatController getChatController() {
        return chatController;
    }
    /**
     
     Returns the current player text.
     @return The current player text.
     */
    public Text getCurrentPlayer() {
        return currentPlayer;
    }
    
    
}
