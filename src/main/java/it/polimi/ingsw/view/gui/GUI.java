package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.controllers.BoardController;
import it.polimi.ingsw.view.messages.Move;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.messages.CommonGoalMessage.*;
import static javafx.application.Platform.*;


public class GUI extends View {
    
    
    private List<String> otherPlayersNicks = new ArrayList<>(3);
    
    @Override
    public void onMessage(BoardMessage msg) {
        runLater(() ->
                 {
                     BoardController boardController =
                             GUIApp.getMainControllerInstance().getGameInterfaceController().getBoardController();
                     boardController.resetSelected();
                     boardController.updateBoard(msg.getPayload());
                     
                 });
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
        runLater(() -> {
            List<Shelf> otherPlayerShelf = new ArrayList<>(3);
            List<Integer> scores = new ArrayList<>(3);
            for( var x : model.getPlayersNicknames() ) {
                if( !x.equals(this.getNickname()) ) {
                    otherPlayersNicks.add(x);
                    otherPlayerShelf.add(model.getShelf(x));
                    scores.add(model.getPoints(x));
                }
            }
            GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().updateShelf(
                    model.getShelf(nickname));
            GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().setPersonalGoal(
                    model.getPgid());
            GUIApp.getMainControllerInstance().getGameInterfaceController().initializeShelves(otherPlayersNicks,
                                                                                              otherPlayerShelf,
                                                                                              scores);
            
            BoardController boardController =
                    GUIApp.getMainControllerInstance().getGameInterfaceController().getBoardController();
            boardController.updateBoard(
                    model.getBoard());
            
            boardController.setCommonGoalX(model.getCGXindex());
            boardController.setCommonGoalY(model.getCGYindex());
            boardController.setCommonGoalXStack(model.getTopCGXscore());
            boardController.setCommonGoalYStack(model.getTopCGYscore());
            
            
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
                p.setAutoHide(true);
                p.setAutoFix(true);
                
                Label label = new Label(msg.getPayload().msg() + " from action : " + msg.getPayload().Action());
                label.setStyle(" -fx-background-color: #eee69b;");
                p.getContent().add(label);
                p.show(s);
                if( model.isStarted() ) {
                    GUIApp.getMainControllerInstance().getGameInterfaceController().getBoardController().resetSelected();
                }else if(msg.getPayload().Action().matches("RecoverLobbyMessage|RequestLobbyMessage|JoinLobbyMessage|CreateLobbyMessage")){
                    GUIApp.getLoginController().setWaitToJoin(false);
                }
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
        }else {
            runLater(() ->
                             GUIApp.getMainControllerInstance().getGameInterfaceController().updateShelf(
                                     msg.getPayload(), msg.getPlayer())
            );
        }
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(IncomingChatMessage msg) {
        //TODO
        
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(UpdateScoreMessage msg) {
        //TODO
        
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(CommonGoalMessage msg) {
        CommonGoalPayload p = msg.getPayload();
        runLater(() -> {
                     BoardController boardController =
                             GUIApp.getMainControllerInstance().getGameInterfaceController().getBoardController();
                     switch( p.type() ) {
                         case Y -> boardController.setCommonGoalYStack(p.availableTopScore());
                         case X -> boardController.setCommonGoalXStack(p.availableTopScore());
                     }
                 }
        );
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(CurrentPlayerMessage msg) {
        model.setCurrentPlayer(msg.getPayload());
        //TODO
        
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
