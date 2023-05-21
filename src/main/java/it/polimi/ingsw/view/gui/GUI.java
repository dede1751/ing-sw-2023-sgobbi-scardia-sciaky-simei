package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.view.View;
import javafx.stage.Stage;


public class GUI extends View  {
    
    
    
    private GUIApp app = new GUIApp(this);
    
    @Override
    public void onMessage(BoardMessage msg) {
    
    }
    
    @Override
    public void onMessage(AvailableLobbyMessage msg) {
    
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
            app.start(new Stage());
        }
        catch( Exception e ) {
            throw new RuntimeException(e);
        }
        
    }
    
}
