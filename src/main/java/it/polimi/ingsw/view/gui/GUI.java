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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.messages.CommonGoalMessage.CommonGoalPayload;
import static javafx.application.Platform.runLater;



/**
 * The GUI class extends the View class and represents the graphical user interface of the application.
 */
public class GUI extends View {
    
    
    private final List<String> otherPlayersNicks = new ArrayList<>(3);
    
    /**
     * Constructs a new GUI instance.
     */
    public GUI() {
    
    }
    /**
     * Respond to a {@link BoardMessage}, receiving a board update from the server
     *
     * @param msg The BoardMessage received.
     */
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
    /**
     * Respond to an {@link AvailableLobbyMessage}, updating the list of available lobbies.
     *
     * @param msg The AvailableLobbyMessage received.
     */
    @Override
    public void onMessage(AvailableLobbyMessage msg) {
        this.lobbies = msg.getPayload().lobbyViewList();
        runLater(() -> GUIApp.getLoginController().updateLobbies(msg.getPayload().lobbyViewList()));
    }
    /**
     *Respond to an {@link EndGameMessage}, printing the leaderboard and terminating the game.
     *
     * @param msg The EndGameMessage received.
     */
    @Override
    public void onMessage(EndGameMessage msg) {
        try {
            FXMLLoader endgame = new FXMLLoader(ResourcesManager.GraphicalResources.getFXML("endgame.fxml"));
            EndgameController.setEndgame(msg.getPayload());
            Parent root = endgame.load();
            runLater(() -> {
                         GUIApp.getMainStage().setMaxHeight(593.0);
                         GUIApp.getMainStage().setMaxWidth(1045.0);
                         GUIApp.getMainStage().setScene(new Scene(root));
                         GUIApp.getMainStage().setTitle("The winner is : " + msg.getPayload().winner());
                     }
            );
        }
        catch( IOException e ) {
            throw new RuntimeException(e);
        }
        
    }
    /**
     * Respond to a {@link StartGameMessage}, setting up the model's initial state and starting the game. <br>
     *
     * @param msg The StartGameMessage received.
     */
    @Override
    public void onMessage(StartGameMessage msg) {
        model.setModel(msg);
        model.setStarted(true);
        List<Shelf> otherPlayerShelf = new ArrayList<>(3);
        for( var x : model.getPlayersNicknames() ) {
            if( !x.equals(this.getNickname()) ) {
                otherPlayersNicks.add(x);
                otherPlayerShelf.add(model.getShelf(x));
            }
        }
        
        runLater(() -> {
            
            GUIApp.getMainControllerInstance().getGameInterfaceController().getCurrentPlayer().setText(nickname);
            GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().updateShelf(
                    model.getShelf(nickname));
            GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().setPersonalGoal(
                    model.getPgid());
            GUIApp.getMainControllerInstance().getGameInterfaceController().initializeShelves(otherPlayersNicks,
                                                                                              otherPlayerShelf);
            
            GUIApp.getMainControllerInstance().getGameInterfaceController().getCurrentPlayer().setText(
                    "     The Current player is " + model.getCurrentPlayer());
            
            GUIApp.getMainControllerInstance().getGameInterfaceController().getChatController().setRecipientName();
            if( model.getPlayersNicknames().get(0).equals(nickname) ) {
                GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().setChairOpacity(
                        1.0);
            }
            GUIApp.getMainControllerInstance().getGameInterfaceController().getLocalPlayerController().setNickname();
            
            BoardController boardController =
                    GUIApp.getMainControllerInstance().getGameInterfaceController().getBoardController();
            boardController.updateBoard(
                    model.getBoard());
            
            boardController.setCommonGoalX(model.getCGXindex());
            boardController.setCommonGoalY(model.getCGYindex());
            boardController.setCommonGoalXStack(model.getTopCGXscore());
            boardController.setCommonGoalYStack(model.getTopCGYscore());
            
            
            GUIApp.getMainStage().setMaxHeight(Double.MAX_VALUE);
            GUIApp.getMainStage().setMaxWidth(Double.MAX_VALUE);
            GUIApp.getMainStage().setScene(new Scene(GUIApp.getMainRoot()));
        });
    }
    
    /**
     Respond to a {@link ServerResponseMessage}, adding the response to the response list. <br>
     * These messages are received as a response to various requests to the server. <br>
     * Messages involving registration always receive a response, while during the game one is received only for errors.
     *
     * @param msg The ServerResponseMessage received.
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
            Label label = new Label(msg.getPayload().msg());
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
     * Respond to a {@link ShelfMessage}, updating the current player's shelf.
     *
     * @param msg The ShelfMessage received.
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
     *  Respond to a {@link IncomingChatMessage}. <br>
     *
     * @param msg The IncomingChatMessage received.
     */
    @Override
    public void onMessage(IncomingChatMessage msg) {
        this.model.addChatMessage(msg.getSender(), msg.getPayload(), msg.getDestination());
        
        if( this.model.isStarted() ) {
            if( msg.getDestination().equals(nickname) ) {
                
                runLater(
                        () -> GUIApp.getMainControllerInstance().getGameInterfaceController().getChatController().writeChatMessage(
                                msg.getSender() + ": " + msg.getPayload(), Color.FLORALWHITE));
            }else if( msg.getSender().equals(nickname) ) {
                runLater(
                        () -> GUIApp.getMainControllerInstance().getGameInterfaceController().getChatController().writeChatMessage(
                                msg.getSender() + ": " + msg.getPayload(), Color.INDIGO));
            }else {
                runLater(
                        () -> GUIApp.getMainControllerInstance().getGameInterfaceController().getChatController().writeChatMessage(
                                msg.getSender() + ": " + msg.getPayload(), Color.BLACK));
            }
            
        }
        
    }
    
    /**
     * Respond to an {@link UpdateScoreMessage}, providing score updates at the end of the turn.
     *      *
     * @param msg The UpdateScoreMessage received.
     */
    @Override
    public void onMessage(UpdateScoreMessage msg) {
        
        this.model.setPoints(msg.getPayload().type(), msg.getPayload().player(), msg.getPayload().score());
        
        
        runLater(() -> GUIApp.getMainControllerInstance().getGameInterfaceController().updateScore(
                this.model.getPoints(msg.getPayload().player()), msg.getPayload().player()));
    }
    /**
     * Respond to a {@link CommonGoalMessage}, updating the score of the common goal stacks.
     *      *
     * @param msg The CommonGoalMessage received.
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
     * Respond to a {@link CurrentPlayerMessage}, updating the current player. <br>
     *
     * @param msg The CurrentPlayerMessage received.
     */
    @Override
    public void onMessage(CurrentPlayerMessage msg) {
        model.setCurrentPlayer(msg.getPayload());
        runLater(() -> GUIApp.getMainControllerInstance().getGameInterfaceController().getCurrentPlayer().setText(
                "     The Current player is " + msg.getPayload()));
    }
    /**
     * Runs the GUI application.
     */
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
