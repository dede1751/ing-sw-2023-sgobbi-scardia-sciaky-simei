package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Shelf;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class GameInterfaceController {
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
    public void initialize(){
        otherShelf1.setOpacity(0);
        otherShelf2.setOpacity(0);
        otherShelf3.setOpacity(0);
        shelfList = List.of(otherShelf1, otherShelf2, otherShelf3);
        shelfControllerList = List.of(otherShelf1Controller, otherShelf2Controller, otherShelf3Controller);
    }
    
    /**
     * Initialize shelves
     * @param nicknames Ordered list of all other players
     * @param shelves
     * @param scores
     */
    public void initializeShelves(List<String> nicknames, List<Shelf> shelves, List<Integer> scores){
        for(int i = 0; i < nicknames.size(); i++){
            shelfControllerList.get(i).setPlayerName(nicknames.get(i));
            shelfControllerList.get(i).updateOtherShelf(shelves.get(i));
            shelfControllerList.get(i).setScore(scores.get(i));
            shelfList.get(i).setOpacity(1);
        }
    }
    
    /**
     *
     * @param score
     * @param playerNumber
     */
    public void updateScore(int score, int playerNumber){
        shelfControllerList.get(playerNumber).setScore(score);
    }
    
    public LocalPlayerController getLocalPlayerController() {
        return localPlayerController;
    }
}
