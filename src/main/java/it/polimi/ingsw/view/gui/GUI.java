package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.controllers.BoardController;
import it.polimi.ingsw.view.gui.controllers.EndgameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.messages.CommonGoalMessage.CommonGoalPayload;
import static javafx.application.Platform.runLater;


public class GUI extends View {
    
    
    private final List<String> otherPlayersNicks = new ArrayList<>(3);
    
    @Override
    public void onMessage(BoardMessage msg) {
        model.setBoard(msg.getPayload());
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
        try {
            FXMLLoader endgame = new FXMLLoader(ResourcesManager.GraphicalResources.getFXML("endgame.fxml"));
            EndgameController.setEndgame(msg.getPayload());
            Parent root = endgame.load();
            runLater(() -> {
                         GUIApp.getMainStage().setScene(new Scene(root));
                         GUIApp.getMainStage().setTitle("The winner is : " + msg.getPayload().winner());
                     }
            );
        }
        catch( IOException e ) {
            throw new RuntimeException(e);
        }
        
    }
    
    @Override
    public void onMessage(StartGameMessage msg) {
        model.setModel(msg);
        model.setStarted(true);
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
            GUIApp.getMainControllerInstance().getGameTab().setText(nickname);
            GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().updateShelf(
                    model.getShelf(nickname));
            GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().setPersonalGoal(
                    model.getPgid());
            GUIApp.getMainControllerInstance().getGameInterfaceController().initializeShelves(otherPlayersNicks,
                                                                                              otherPlayerShelf,
                                                                                              scores);
            GUIApp.getMainControllerInstance().getChatController().setRecipientName();
            
            
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
            if( model.isStarted() ) {
                runLater(() ->
                                 GUIApp.getMainControllerInstance().getGameInterfaceController().getBoardController().resetSelected());
            }else if( msg.getPayload().Action().matches(
                    "RecoverLobbyMessage|JoinLobbyMessage|CreateLobbyMessage") ) {
                GUIApp.getLoginController().setWaitToJoin(false);
            }
            Stage s = GUIApp.getMainStage();
            Popup p = new Popup();
            p.setAutoHide(true);
            p.setAutoFix(true);
            Label label = new Label(msg.getPayload().msg() + " from action : " + msg.getPayload().Action());
            label.setStyle(" -fx-background-color: #b06b51;");
            label.setFont(new Font("Noto Sans", 16));
            p.getContent().add(label);
            runLater(() -> p.show(s));
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    if( p.isShowing() ) {
                        runLater(p::hide);
                    }
                }
                catch( InterruptedException e ) {
                    if( p.isShowing() ) {
                        runLater(p::hide);
                    }
                }
            }).start();
        }else if( msg.getPayload().Action().matches(
                "RecoverLobbyMessage|JoinLobbyMessage|CreateLobbyMessage") ) {
            new Thread(() ->
                               GUIApp.getLoginController().waitingGameAnimation()).start();
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
        this.model.addChatMessage(msg.getSender(), msg.getPayload(), msg.getDestination());
        if( this.model.isStarted() ) {
            runLater(() -> {
                
                GUIApp.getMainControllerInstance().getChatController().writeChatMessage(
                        msg.getSender() + ": " + msg.getPayload());
                
                
            });
        }
        
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(UpdateScoreMessage msg) {
        
        this.model.setPoints(msg.getPayload().type(), msg.getPayload().player(), msg.getPayload().score());
        
        
        runLater(() -> {
            
            GUIApp.getMainControllerInstance().getGameInterfaceController().updateScore(
                    this.model.getPoints(msg.getPayload().player()), msg.getPayload().player());
            
            
        });
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
                         case Y -> {
                             boardController.setCommonGoalYStack(p.availableTopScore());
                             model.setCGYindex(p.availableTopScore());
                         }
                         case X -> {
                             model.setTopCGXscore(p.availableTopScore());
                             boardController.setCommonGoalXStack(p.availableTopScore());
                         }
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
        runLater(() -> GUIApp.getMainControllerInstance().getGameTab().setText(
                nickname + "| current --> " + msg.getPayload()));
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
