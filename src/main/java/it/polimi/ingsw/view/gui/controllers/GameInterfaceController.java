package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.view.gui.GUIApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.control.Slider;
import javafx.event.ActionEvent;
import javafx.scene.transform.Scale;

import javax.annotation.processing.Generated;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.Objects;

public class GameInterfaceController {
    @FXML
    private AnchorPane chat;
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
    
    @FXML
    public void initialize() {
        x075.setOnAction(event -> changeSize(0.75));
        x1.setOnAction(event -> changeSize(1));
        x125.setOnAction(event -> changeSize(1.25));
        x150.setOnAction(event -> changeSize(1.5));
        otherShelf1.setOpacity(0);
        otherShelf2.setOpacity(0);
        otherShelf3.setOpacity(0);
        shelfList = List.of(otherShelf1, otherShelf2, otherShelf3);
        shelfControllerList = List.of(otherShelf1Controller, otherShelf2Controller, otherShelf3Controller);
        rootHBox.setBackground(new Background(new BackgroundImage(
                new Image(ResourcesManager.GraphicalResources.getGraphicalAsset("misc/sfondo_parquet.png")),
                BackgroundRepeat.SPACE, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
   
        
       
   
    }
    

    
    
    
    
    
    /**
     * Initialize shelves
     *
     * @param nicknames Ordered list of all other players
     * @param shelves
     * @param scores
     */
    public void initializeShelves(List<String> nicknames, List<Shelf> shelves, List<Integer> scores) {
        for( int i = 0; i < nicknames.size(); i++ ) {
            shelfControllerList.get(i).setPlayerName(nicknames.get(i));
            shelfControllerList.get(i).updateOtherShelf(shelves.get(i));
            shelfControllerList.get(i).setScore(scores.get(i));
            
            shelfList.get(i).setOpacity(1);
        }
        localPlayerController.setScore(0);
    }
    
    public void updateShelf(Shelf shelf, String nickname) {
        shelfControllerList.stream().filter((x) -> x.getPlayerNickname().equals(nickname)).forEach(
                (x) -> x.updateOtherShelf(shelf));
    }
    
    
    private void changeSize(double x){
        Scale scale = new Scale();
        scale.setPivotX(0);
        scale.setPivotY(0);
        scale.setX(x);
        scale.setY(x);
        Pane gameInterface=GUIApp.getMainControllerInstance().getGameInterface();
        gameInterface.getTransforms().setAll(scale);
        double contentWidth = gameInterface.getBoundsInLocal().getWidth();
        double contentHeight = gameInterface.getBoundsInLocal().getHeight();
        GUIApp.getMainControllerInstance().setScrollPane(contentWidth,contentHeight);
    }
    
    /**
     * @param score
     * @param nickname
     */
    public void updateScore(int score, String nickname) {
        
        Boolean flag = true;
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
    
    public LocalPlayerController getLocalPlayerController() {
        return localPlayerController;
    }
    
    public BoardController getBoardController() {
        return boardController;
    }
    
    
}
