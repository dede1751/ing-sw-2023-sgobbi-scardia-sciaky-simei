package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.messages.EndGameMessage;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.List;

public class EndgameController {
    @FXML
    private ImageView PC1;
    @FXML
    private ImageView PC4;
    @FXML
    private ImageView PC3;
    @FXML
    private ImageView PC2;
    @FXML
    private ImageView P3;
    @FXML
    private ImageView P4;
    @FXML
    private ImageView P2;
    @FXML
    private ImageView P1;
    @FXML
    private Text player3;
    @FXML
    private Text player2;
    @FXML
    private Text player4;
    @FXML
    private Text player1;
    
    
    private final List<Text> players = List.of(player1, player2, player3, player4);
    private final List<ImageView> chairs = List.of(PC1, PC2, PC3, PC4);
    private final List<ImageView> playersGif = List.of(P1, P2, P3, P4);
    private static EndGameMessage.EndGamePayload payload;
    
    public static void setEndgame(EndGameMessage.EndGamePayload payload){
        EndgameController.payload = payload;
    }
    @FXML
    public void initialize(){
        for(var x : payload.points().entrySet()){
            players.iterator().next().setText(x.getKey() + " : " + x.getValue());
            chairs.iterator().next().setOpacity(1);
            playersGif.iterator().next().setOpacity(1);
        }
    }
}
