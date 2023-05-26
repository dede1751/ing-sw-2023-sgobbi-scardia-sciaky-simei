package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javafx.application.Platform.*;


public class GUI extends View {
    
    
    @Override
    public void onMessage(BoardMessage msg) {
    }
    
    @Override
    public void onMessage(AvailableLobbyMessage msg) {
        this.lobbies = msg.getPayload().lobbyViewList();
        runLater(() -> GUIApp.getLoginController().updateLobbies(msg.getPayload().lobbyViewList()));
    }
    
    @Override
    public void onMessage(EndGameMessage msg) {
    
    }
    
    @Override
    public void onMessage(StartGameMessage msg) {
        model.setModel(msg);
        model.setStarted(true);
        List<String> otherPlayersNicks = new ArrayList<>(3);
        List<Shelf> otherPlayerShelf = new ArrayList<>(3);
        List<Integer> scores = new ArrayList<>(3);
        for( var x : model.getPlayersNicknames() ) {
            if( !x.equals(this.getNickname()) ) {
                otherPlayersNicks.add(x);
                otherPlayerShelf.add(model.getShelf(x));
                scores.add(model.getPoints(x));
            }
        }
        runLater(() -> {
            GUIApp.getMainControllerInstance().getGameInterfaceController().initializeShelves(otherPlayersNicks,
                                                                                              otherPlayerShelf,
                                                                                              scores);
            GUIApp.getMainStage().setScene(new Scene(GUIApp.getMainRoot()));
        });
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(ServerResponseMessage msg) {
        if( !msg.getPayload().isOk() ) {
            runLater(() -> {
                Stage s = GUIApp.getMainStage();
                Popup p = new Popup();
                Label label = new Label(msg.getPayload().msg() + " from action : " + msg.getPayload().Action());
                label.setStyle(" -fx-background-color: #eee69b;");
                p.getContent().add(label);
                p.show(s);
            });
        }
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(ShelfMessage msg) {
        if( msg.getPlayer().equals(this.getNickname()) ) {
            runLater(() ->
                             GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().updateShelf(
                                     msg.getPayload())
            );
        }else{
            //FIXME update other shelves
        }
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(IncomingChatMessage msg) {
    
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(UpdateScoreMessage msg) {
    
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(CommonGoalMessage msg) {
    
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(CurrentPlayerMessage msg) {
    
    }
    
    @Override
    public void run() {
        try {
            Application.launch(GUIApp.class);
        }
        catch( Exception e ) {
            throw new RuntimeException(e);
        }
        
    }
    
}
