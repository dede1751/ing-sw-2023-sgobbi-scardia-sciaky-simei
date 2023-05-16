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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
        
        // Reset PersonalGoal/Adjacency scores (this is used because the model could be a recovery one)
        for( Player p : model.getPlayers() ) {
            p.setPersonalGoalScore(PersonalGoal.getPersonalGoal(p.getPg()).checkGoal(p.getShelf()));
            p.setAdjacencyScore(calculateAdjacency(p.getShelf()));
            p.setBonusScore(0); //FIXME
        }
        // Refill the board (recovery boards may not need refilling
        if( needRefill() ) {
            model.refillBoard();
        }
        
        model.startGame();
    }
    
    /**
     * Mock GameController class for testing purposes.
     * This hooks empty listeners to the model to avoid problems when it tries to send data to a client
     *
     * @param model Model instance to run
     */
    public GameController(GameModel model) {
        this.model = model;
        this.lobbyID = 0;
        this.playerNumber = model.getPlayers().size();
        
        for( Player p : model.getPlayers() ) {
            try {
                model.addListener(p.getNickname(), (m) -> {
                });
            }
            catch( Exception ignored ) {
            }
        }
    }
    
    /**
     * Check if the board needs to be refilled according to the rules (either empty or all tiles isolated)
     *
     * @return True if a refill is needed, false otherwise
     */
    public boolean needRefill() {
        Board board = model.getBoard();
        
        // get all coordinates with at least one empty side
        List<Coordinate> removable = board.getTiles()
                .keySet()
                .stream()
                .filter(c -> !board.getTile(c).equals(Tile.NOTILE)
                             && (board.getTile(c.getDown()) == null
                                 || board.getTile(c.getDown()).equals(Tile.NOTILE)
                                 || board.getTile(c.getUp()) == null
                                 || board.getTile(c.getUp()).equals(Tile.NOTILE)
                                 || board.getTile(c.getLeft()) == null
                                 || board.getTile(c.getLeft()).equals(Tile.NOTILE)
                                 || board.getTile(c.getRight()) == null
                                 || board.getTile(c.getRight()).equals(Tile.NOTILE)))
                .toList();
        
        // Handle empty boards
        if( removable.isEmpty() ) {
            return true;
        }
        
        // check if there is at least one where an adjacent coordinate is also removable
        return removable.stream()
                .noneMatch(c -> removable.contains(c.getDown())
                                || removable.contains(c.getUp())
                                || removable.contains(c.getLeft())
                                || removable.contains(c.getRight()));
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
        model.setCurrentPlayerAdjacencyScore(calculateAdjacency(currentPlayer.getShelf()));
        
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
        if( model.isLastTurn() && currentPlayerIndex == model.getPlayers().size() ) {
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
        ResourcesManager.saveModel(model, lobbyID);
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
     * In case of error, notify the client through the model with a ServerMessage.
     *
     * @param msg Move received
     */
    @SuppressWarnings("unused")
    public void onMessage(MoveMessage msg) {
        String currentPlayerNick = model.getCurrentPlayer().getNickname();
        
        if( !msg.getPlayerNickname().equals(currentPlayerNick) ) {
            this.model.notifyServerMessage(
                    msg.getPlayerNickname(),
                    new ServerResponseMessage(
                            Response.NotCurrentPlayer(currentPlayerNick, msg.getClass().getSimpleName()))
            );
            return;
        }
        
        Move move = msg.getPayload();
        if( !IntegrityChecks.checkMove(move, this.model.getBoard(), this.model.getCurrentPlayer().getShelf()) ) {
            this.model.notifyServerMessage(
                    msg.getPlayerNickname(),
                    new ServerResponseMessage(Response.IllegalMove(currentPlayerNick, msg.getClass().getSimpleName()))
            );
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
        System.out.println("Debug message just arrived hurray! It says : " + message.getPayload());
        saveModel();
        
        this.model.notifyServerMessage(
                message.getPlayerNickname(),
                new ServerResponseMessage(new Response(0, message.getPayload(), message.getClass().getSimpleName()))
        );
    }
    
}
