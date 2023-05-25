package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import static javafx.application.Platform.*;


public class GUI extends View  {
    
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
    
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(ServerResponseMessage msg) {
        if(!msg.getPayload().isOk()) {
            runLater(() -> {
                Stage s = GUIApp.getMainStage();
                Popup p = new Popup();
                Label label = new Label(msg.getPayload().msg() + " from action : " + msg.getPayload().Action());
                label.setStyle(" -fx-background-color: white;");
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
