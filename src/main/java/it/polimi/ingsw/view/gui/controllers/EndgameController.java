package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.model.messages.EndGameMessage;
import it.polimi.ingsw.view.gui.GUIUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.List;
/**
 
 The EndgameController class is responsible for managing the endgame screen in the GUI.
 
 It displays the final scores and winners of the game.
 */
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
    @FXML
    private ImageView victoryRoyale;
    
    private static EndGameMessage.EndGamePayload payload;
    /**
     
     Sets the endgame payload containing the final scores and winners.
     @param payload The endgame payload.
     */
    public static void setEndgame(EndGameMessage.EndGamePayload payload) {
        EndgameController.payload = payload;
    }
    /**
     
     Initializes the endgame screen by displaying the final scores and winners.
     */
    @FXML
    public void initialize() {
        GUIUtils.threadPool.submit(() -> {
            List<Text> players = List.of(player1, player2, player3, player4);
            List<ImageView> chairs = List.of(PC1, PC2, PC3, PC4);
            List<ImageView> playersGif = List.of(P1, P2, P3, P4);
            Iterator<Text> p = players.iterator();
            Iterator<ImageView> c = chairs.iterator();
            Iterator<ImageView> pG = playersGif.iterator();
            
            if( payload.winner().equals(AppClient.getViewInstance().getNickname()) )
                victoryRoyale.setOpacity(1);
            for( var x : payload.points().entrySet() ) {
                Text t = p.next();
                Platform.runLater(() -> {
                    t.setText(x.getKey() + " : " + x.getValue());
                    t.setOpacity(1);
                    c.next().setOpacity(1);
                    pG.next().setOpacity(1);
                });
            }
        });
    }
}
