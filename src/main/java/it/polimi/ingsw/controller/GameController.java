package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.goals.common.CommonGoal;
import it.polimi.ingsw.model.goals.personal.PersonalGoal;
import it.polimi.ingsw.model.messages.Response;
import it.polimi.ingsw.model.messages.ServerResponseMessage;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.utils.mvc.IntegrityChecks;
import it.polimi.ingsw.view.messages.ChatMessage;
import it.polimi.ingsw.view.messages.DebugMessage;
import it.polimi.ingsw.view.messages.Move;
import it.polimi.ingsw.view.messages.MoveMessage;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * GameController, responsible for modifying the model according to the input from the player views
 */
public class GameController {
    
    private final GameModel model;
    
    // Needed to close the lobby when the game ends
    private final int lobbyID;
    
    private final Integer playerNumber;
    
    /**
     * Initialize the controller with the given model and start the game
     *
     * @param model   Model instance to run
     * @param lobbyID Lobby identifier, used to close lobby on exit
     */
    public GameController(GameModel model, int lobbyID) {
        this.model = model;
        this.lobbyID = lobbyID;
        this.playerNumber = model.getPlayers().size();
        
        model.startGame();
    }
    
    /**
     * Check if the board needs to be refilled according to the rules (either empty or all tiles isolated)
     *
     * @return True if a refill is needed, false otherwise
     */
    public boolean needRefill() {
        Map<Coordinate, Tile> toBeChecked = model.getBoard().getTiles();
        
        for( var entry : toBeChecked.entrySet() ) {
            if( !(entry.getValue().equals(Tile.NOTILE)) ) {
                if( !(model.getBoard().getTile(entry.getKey().getDown()) == Tile.NOTILE)
                    || !(model.getBoard().getTile(entry.getKey().getUp()) == Tile.NOTILE)
                    || !(model.getBoard().getTile(entry.getKey().getLeft()) == Tile.NOTILE)
                    || !(model.getBoard().getTile(entry.getKey().getRight()) == Tile.NOTILE) ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Compute adjacency score for final scores
     *
     * @param shelf Shelf to check for the score
     *
     * @return Integer score assigned for adjacent similar tiles in the given shelf
     */
    public int calculateAdjacency(Shelf shelf) {
        
        var mat = shelf.getAllShelf();
        var checked = new boolean[Shelf.N_ROW][Shelf.N_COL];
        int adjacentScore = 0;
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                Tile.Type type = mat[i][j].type();
                if( checked[i][j] || type == Tile.Type.NOTILE )
                    continue;
                
                var current = new Coordinate(i, j);
                
                //select chunck
                List<Coordinate> selected = new ArrayList<>();
                Queue<Coordinate> visited = new LinkedList<>();
                visited.add(current);
                checked[current.row()][current.col()] = true;
                while( !visited.isEmpty() ) {
                    current = visited.poll();
                    selected.add(current);
                    current.sumDir().stream()
                            .filter((x) -> x.row() < Shelf.N_ROW &&
                                           x.row() > -1 &&
                                           x.col() < Shelf.N_COL &&
                                           x.col() > -1 &&
                                           mat[x.row()][x.col()].type() == type &&
                                           !checked[x.row()][x.col()])
                            .forEach((x) -> {
                                visited.add(x);
                                checked[x.row()][x.col()] = true;
                            });
                }
                
                final int[] scores = {0, 0, 0, 2, 3, 5, 8};
                adjacentScore += scores[Math.min(selected.size(), 6)];
            }
        }
        return adjacentScore;
    }
    
    /**
     * Turn bookkeeping:
     * - checks common/personal/adjacency scores and updates them accordingly.
     * - refills the board if needed
     * - checks if the game is on its last turn
     */
    public void turnManager() {
        
        //reference to the current player
        Player currentPlayer = model.getCurrentPlayer();
        
        if( !currentPlayer.isCompletedGoalX() &&
            CommonGoal.getCommonGoal(model.getCommonGoalX()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerCommongGoalScore(model.popStackCGX(), GameModel.CGType.X);
        }
        if( !currentPlayer.isCompletedGoalY() &&
            CommonGoal.getCommonGoal(model.getCommonGoalY()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerCommongGoalScore(model.popStackCGY(), GameModel.CGType.Y);
        }
        
        //calculate and set in every turn the personalGoalScore
        model.setCurrentPlayerPersonalScore(
                PersonalGoal.getPersonalGoal(currentPlayer.getPg()).checkGoal(currentPlayer.getShelf()));
        
        //calculate and set in every turn the adjacentTiles and score
        model.setCurrentPlayerAdiajencyScore(calculateAdjacency(currentPlayer.getShelf()));
        
        if( needRefill() ) {
            model.refillBoard();
        }
        
        if( currentPlayer.getShelf().isFull() && !model.isLastTurn() ) {
            model.setLastTurn();
        }
    }
    
    /**
     * Update the current player, handling game termination
     */
    public void nextPlayerSetter() {
        int currentPlayerIndex = model.getCurrentPlayerIndex();
        if( model.isLastTurn() && currentPlayerIndex == 3 ) {
            endGame();
        }else {
            currentPlayerIndex = (currentPlayerIndex + 1) % playerNumber;
            model.setCurrentPlayerIndex(currentPlayerIndex);
        }
    }
    
    /**
     * Notify the views of the winner and close the lobby
     */
    public void endGame() {
        model.notifyWinner();
        LobbyController.getInstance().endGame(this.lobbyID);
    }
    
    /**
     * Save the model to a file
     */
    private void saveModel() {
        try {
            File dir = new File(ResourcesManager.recoveryDir);
            if( !dir.exists() && !dir.mkdir() ) {
                System.err.println("Unable to create recovery directory");
                return;
            }
            
            model.toJson(ResourcesManager.recoveryDir + "/" + lobbyID + ".json");
        }
        catch( IOException e ) {
            System.err.println("Error saving model");
            e.printStackTrace();
        }
    }
    
    /**
     * Respond to a chat message received from a client
     *
     * @param chat Message contents
     */
    @SuppressWarnings("unused")
    public void onMessage(ChatMessage chat) {
        model.chatBroker(chat);
    }
    
    /**
     * Respond to a move message received from a client
     *
     * @param msg Move received
     */
    @SuppressWarnings("unused")
    public void onMessage(MoveMessage msg) {
        
        String currentPlayerNick = model.getCurrentPlayer().getNickname();
        LobbyController.ClientContext cc = LobbyController.ClientContext.getCC(msg);
        if( !cc.nickname().equals(currentPlayerNick) ) {
            System.err.println("Ignoring event from player '" + msg.getPlayerNickname() + "': " +
                               msg.getMessageType().getTypeName() + ". Not the current Player.");
            var r = new ServerResponseMessage(
                    Response.NotCurrentPlayer(currentPlayerNick, msg.getClass().getSimpleName()));
            LobbyController.getInstance().updateClient(cc, r);
            return;
        }
        
        Move move = msg.getPayload();
        if( !IntegrityChecks.checkMove(move, this.model.getBoard(), this.model.getCurrentPlayer().getShelf()) ) {
            var r = new ServerResponseMessage(Response.IllegalMove(currentPlayerNick, msg.getClass().getSimpleName()));
            LobbyController.getInstance().updateClient(cc, r);
            return;
        }
        
        // if the move is valid, execute the full turn
        model.removeSelection(move.selection());
        model.shelveSelection(move.tiles(), move.column());
        turnManager();
        nextPlayerSetter();
        saveModel();
    }
    
    @SuppressWarnings("unused")
    public void onMessage(DebugMessage message) {
        LobbyController.ClientContext cc = LobbyController.ClientContext.getCC(message);
        System.out.println("Debug message just arrived hurray! It says : " + message.getPayload());
        saveModel();
        var r = new ServerResponseMessage(new Response(0, message.getPayload(), message.getClass().getSimpleName()));
        LobbyController.getInstance().updateClient(cc, r);
    }
    
}
