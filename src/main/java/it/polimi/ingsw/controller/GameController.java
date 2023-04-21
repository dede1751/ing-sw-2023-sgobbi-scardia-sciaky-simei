package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.goals.common.CommonGoal;
import it.polimi.ingsw.model.goals.personal.PersonalGoal;
import it.polimi.ingsw.utils.mvc.IntegrityChecks;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;
import it.polimi.ingsw.view.messages.*;

import java.awt.desktop.SystemEventListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


//TODO change all the print

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
    public Boolean needRefill() {
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
            model.addCurrentPlayerCommongGoalScore(model.popStackCGX());
            currentPlayer.setCompletedGoalX(true);
        }
        if( !currentPlayer.isCompletedGoalY() &&
            CommonGoal.getCommonGoal(model.getCommonGoalY()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerCommongGoalScore(model.popStackCGY());
            currentPlayer.setCompletedGoalY(true);
        }
        
        //calculate and set in every turn the personalGoalScore
        currentPlayer.setPersonalGoalScore(
                PersonalGoal.getPersonalGoal(currentPlayer.getPg()).checkGoal(currentPlayer.getShelf()));
        
        //calculate and set in every turn the adjacentTiles and score
        currentPlayer.setAdjacentScore(calculateAdjacency(currentPlayer.getShelf()));
        
        if( needRefill() ) {
            model.getBoard().refill(model.getTileBag());
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
        Player winner = model.getPlayers()
                .stream()
                .max(Comparator.comparingInt(Player::getScore))
                .orElseThrow();
        
        model.setWinner(winner.getNickname());
        LobbyController.getInstance().endGame(this.lobbyID);
    }
    
    /**
     * Callback from view
     *
     * @param o   ViewMessage containing all relevant view information
     * @param evt Type of user action that caused the view state change
     */
    //TODO change name to ViewMessage
    public void update(ViewMessage o, View.Action evt) {
        String currentPlayerNick = model.getCurrentPlayer().getNickname();
        if( !o.getNickname().equals(currentPlayerNick) ) {
            System.err.println(
                    "Ignoring event from player '" + o.getNickname() + "': " + evt + ". Not the current Player.");
            return;
        }
        
        switch( evt ) {
            case MOVE -> {
                if( IntegrityChecks.checkSelectionForm(o.getSelection()) ) {
                    model.removeSelection(o.getSelection());
                    model.shelveSelection(o.getTiles(), o.getColumn());
                    turnManager();
                    nextPlayerSetter();
                }
            }
            case CHAT -> {
                //TODO chat functions;
            }
            case PASS_TURN -> {
                //FIXME only for debug porpouses, to be removed in final version
                System.out.println("Player '" + currentPlayerNick + "' passed his turn.");
                nextPlayerSetter();
            }
        }
    }
    
    public <T extends ViewMsg<?>> Response update(T message ){
        try {
            Method m = this.getClass().getMethod("onMessage", message.getMessageType());
            return (Response) m.invoke(this, message);
        }catch( NoSuchMethodException e){
            return new Response(-128, "Illegal message, no operation defined. Refere to the network manual");
        }
        catch( InvocationTargetException | IllegalAccessException e ) {
            return new Response(128, "Server is acting up, please be patient...");
        }
    }
    
    
    
    public Response onMessage(MoveMessage msg){
        
        String currentPlayerNick = model.getCurrentPlayer().getNickname();
        if( !msg.getPlayerNickname().equals(currentPlayerNick) ) {
            System.err.println(
                "Ignoring event from player '" + msg.getPlayerNickname() + "': " + msg.getMessageType().getTypeName() + ". Not the current Player.");
            return Response.NotCurrentPlayer(currentPlayerNick);
        }else {
            Move move = msg.getPayload();
            if( IntegrityChecks.checkMove(move, this.model.getBoard(), this.model.getCurrentPlayer().getShelf()) ) {
                model.removeSelection(move.selection());
                model.shelveSelection(move.tiles(), move.column());
                turnManager();
                nextPlayerSetter();
                return Response.Ok();
            }
            return Response.IllegalMove(currentPlayerNick);
        }
    }
    
    public Response onMessage(ChatMessage chat){
        return new Response(1, "Not implemented yet");
    }
    
    public Response onMessage(DebugMessage message){
        System.out.println("Debug message just arrived urray! It says : " + message.getPayload());
        return new Response(0, message.getPayload());
    }
    
    public record Response(int status, String msg) {
        public static Response Ok() {
            return new Response(0, "OK");
        }
        
        public static Response IllegalMove(String playerNick) {
            System.err.println("Illegal move by player : " + playerNick + " will be ignored");
            return new Response(-1, "Illegal Move : ignoring player action");
        }
        public static Response NotCurrentPlayer(String playerNick){
            System.err.println(playerNick + " is not the current player, this event will be ignored");
            return new Response(-1, "Not the current player : event will be ignored");
        }
        
        
        
    }


    
    
}
