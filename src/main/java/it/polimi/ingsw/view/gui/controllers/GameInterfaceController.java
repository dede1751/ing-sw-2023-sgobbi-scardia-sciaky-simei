package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.utils.files.ResourcesManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Objects;

public class GameInterfaceController {
    @FXML
    public AnchorPane chat;
    @FXML
    private HBox rootHBox;
    
    
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
    public OtherShelfController otherShelf1Controller;
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
