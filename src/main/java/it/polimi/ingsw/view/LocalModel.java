package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.messages.StartGameMessage;
import it.polimi.ingsw.model.messages.UpdateScoreMessage;

import java.util.*;

/**
 * Singleton local model representation used by the running view. <br>
 * Updated when a message is received from the server.
 */
public class LocalModel {
    
    private static LocalModel INSTANCE;
    
    private final Object startLock = new Object();
    private boolean started = false;
    
    private List<String> playersNicknames;
    
    private String currentPlayer;
    
    private final Map<String, Shelf> shelves = new HashMap<>();
    
    private final Map<String, Integer> points = new HashMap<>();
    
    private final Map<String, Integer> cgScore = new HashMap<>();
    private final Map<String, Integer> pgScore = new HashMap<>();
    private final Map<String, Integer> adjacencyScore = new HashMap<>();
    private final Map<String, Integer> bonusScore = new HashMap<>();
    
    private final ArrayList<String> chat = new ArrayList<>();
    
    private int pgid = 0;
    
    private int CGXindex = 0;
    private int CGYindex = 0;
    
    private int topCGXscore = 0;
    private int topCGYscore = 0;
    
    private Board board;
    
    /**
     * Empty constructor
     */
    private LocalModel() {
    }
    
    /**
     * Get the instance of the singleton
     *
     * @return The local model instance
     */
    public static LocalModel getInstance() {
        if( INSTANCE == null ) {
            INSTANCE = new LocalModel();
        }
        
        return INSTANCE;
    }
    
    /**
     * Initialize the model from a {@link StartGameMessage} object
     *
     * @param msg StartGameMessage objet to initialize the model
     */
    public void setModel(StartGameMessage msg) {
        
        var payload = msg.getPayload();
        var players = payload.players();
        
        this.setPlayersNicknames(
                players.stream().map(StartGameMessage.PlayerRecord::nickname).toList()
        );
        
        List<String> nicknames = new LinkedList<>();
        for( var p : players ) {
            nicknames.add(p.nickname());
            this.setShelf(p.shelf(), p.nickname());
            this.setCgScore(p.commonGoalScore(), p.nickname());
            this.setPgScore(p.personalGoalScore(), p.nickname());
            this.setAdjacencyScore(p.adjacencyScore(), p.nickname());
            this.setBonusScore(p.bonusScore(), p.nickname());
        }
        this.setPlayersNicknames(nicknames);
        
        this.setPgid(payload.personalGoalId());
        this.setCGXindex(payload.CGXIndex());
        this.setTopCGXscore(payload.topCGXScore());
        this.setCGYindex(payload.CGYIndex());
        this.setTopCGYscore(payload.topCGYScore());
        
        this.setBoard(payload.board());
        this.setCurrentPlayer(payload.currentPlayer());
    }
    
    /**
     * Wait for the game to be started by a StartGameMessage. <br>
     * Synchronizes over the startLock object.
     */
    public void waitStart() {
        synchronized(startLock) {
            while( !started ) {
                try {
                    startLock.wait();
                }
                catch( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    
    /**
     * Check if the game has started (client received a {@link StartGameMessage}).
     *
     * @return True if the game has started, false otherwise
     */
    public boolean isStarted() {
        return started;
    }
    
    /**
     * Set the game's started state.
     *
     * @param started True if the game should start, false to stop
     */
    public void setStarted(boolean started) {
        synchronized(startLock) {
            this.started = started;
            startLock.notifyAll();
        }
    }
    
    /**
     * Add an incoming chat message to the chat buffer.
     *
     * @param nickname    The nickname of the sender
     * @param message     The message
     * @param destination The recipient of the message (can be "all" or a player's nickname)
     */
    public void addChatMessage(String nickname, String message, String destination) {
        chat.add(nickname + " [" + destination + "]" + " -> " + message);
    }
    
    /**
     * Get the full game chat buffer.
     *
     * @return The chat buffer
     */
    public List<String> getChat() {
        return this.chat;
    }
    
    /**
     * Set the model's board. <br>
     * Called when receiving {@link it.polimi.ingsw.model.messages.BoardMessage}
     *
     * @param board The board received
     */
    public void setBoard(Board board) {
        this.board = board;
    }
    
    /**
     * Get the current board state.
     *
     * @return The current board
     */
    public Board getBoard() {
        return board;
    }
    
    /**
     * Set the given player's shelf. <br>
     * Called when receiving {@link it.polimi.ingsw.model.messages.ShelfMessage}
     *
     * @param shelf    The new shelf
     * @param nickname The player's nickname
     */
    public void setShelf(Shelf shelf, String nickname) {
        if( this.shelves.containsKey(nickname) ) {
            this.shelves.replace(nickname, shelf);
        }else {
            this.shelves.put(nickname, shelf);
        }
    }
    
    /**
     * Get the given player's shelf
     *
     * @param nickname The player's nickname
     *
     * @return The player's shelf
     */
    public Shelf getShelf(String nickname) {
        return this.shelves.get(nickname);
    }
    
    /**
     * Update the score of the given type for the given player <br>
     * Called when receiving {@link UpdateScoreMessage}
     *
     * @param type     The type of score to update
     * @param nickname The player's nickname
     * @param score    The new score
     */
    public void setPoints(UpdateScoreMessage.Type type, String nickname, int score) {
        switch( type ) {
            case Adjacency -> setAdjacencyScore(score, nickname);
            case CommonGoal -> setCgScore(score, nickname);
            case PersonalGoal -> setPgScore(score, nickname);
            case Bonus -> setBonusScore(score, nickname);
        }
        
        int point =
                getAdjacencyScore(nickname) + getCgScore(nickname) + getPgScore(nickname) + getBonusScore(nickname);
        if( this.points.containsKey(nickname) ) {
            this.points.replace(nickname, point);
        }else {
            this.points.put(nickname, point);
        }
    }
    
    /**
     * Get the total score for the given player. <br>
     * With total score we include: CommonGoal, PersonalGoal, Adjacency and Bonus scores.
     *
     * @param nickname The player's nickname
     *
     * @return The player's score
     */
    public int getPoints(String nickname) {
        if( this.points.get(nickname) == null ) {
            return 0;
        }else {
            return this.points.get(nickname);
        }
    }
    
    /**
     * Set the Common Goal score for the given player
     *
     * @param points   The new score
     * @param nickname The player's nickname
     */
    public void setCgScore(Integer points, String nickname) {
        if( this.cgScore.containsKey(nickname) ) {
            this.cgScore.replace(nickname, points);
        }else {
            this.cgScore.put(nickname, points);
        }
        
    }
    
    /**
     * Get the Common Goal score for the given player
     *
     * @param nickname The player's nickname
     *
     * @return The player's Common Goal score
     */
    public int getCgScore(String nickname) {
        return this.cgScore.get(nickname);
    }
    
    /**
     * Set the Personal Goal score for the given player
     *
     * @param points   The new score
     * @param nickname The player's nickname
     */
    public void setPgScore(Integer points, String nickname) {
        if( this.pgScore.containsKey(nickname) ) {
            this.pgScore.replace(nickname, points);
        }else {
            this.pgScore.put(nickname, points);
        }
    }
    
    /**
     * Get the Personal Goal score for the given player
     *
     * @param nickname The player's nickname
     *
     * @return The player's Personal Goal score
     */
    public int getPgScore(String nickname) {
        return this.pgScore.get(nickname);
    }
    
    /**
     * Set the Adjacency score for the given player
     *
     * @param points   The new score
     * @param nickname The player's nickname
     */
    public void setAdjacencyScore(Integer points, String nickname) {
        if( this.adjacencyScore.containsKey(nickname) ) {
            this.adjacencyScore.replace(nickname, points);
        }else {
            this.adjacencyScore.put(nickname, points);
        }
    }
    
    /**
     * Get the Adjacency score for the given player
     *
     * @param nickname The player's nickname
     *
     * @return The player's Adjacency score
     */
    public int getAdjacencyScore(String nickname) {
        return this.adjacencyScore.get(nickname);
    }
    
    /**
     * Set the Bonus score for the given player
     *
     * @param points   The new score
     * @param nickname The player's nickname
     */
    public void setBonusScore(Integer points, String nickname) {
        if( this.bonusScore.containsKey(nickname) ) {
            this.bonusScore.replace(nickname, points);
        }else {
            this.bonusScore.put(nickname, points);
        }
    }
    
    /**
     * Get the Bonus score for the given player
     *
     * @param nickname The player's nickname
     *
     * @return The player's Bonus score
     */
    public int getBonusScore(String nickname) {
        return this.bonusScore.get(nickname);
    }
    
    /**
     * Method to access and add values to a Map that associates
     * a player with his respective personal score.
     *
     * @param pgid The personal goal id of the person playing.
     */
    public void setPgid(int pgid) {
        this.pgid = pgid;
    }
    
    /**
     * Get the personal goal id for this client.
     *
     * @return The personal goal id of the person playing.
     */
    public int getPgid() {
        return this.pgid;
    }
    
    /**
     * Set the Common Goal X index.
     *
     * @param CGXindex The integer id of Common Goal X.
     */
    public void setCGXindex(int CGXindex) {
        this.CGXindex = CGXindex;
    }
    
    /**
     * Get the Common Goal X index.
     *
     * @return The integer id of Common Goal X.
     */
    public int getCGXindex() {
        return CGXindex;
    }
    
    /**
     * Set the Common Goal Y index.
     *
     * @param CGYindex The integer id of Common Goal Y.
     */
    public void setCGYindex(int CGYindex) {
        this.CGYindex = CGYindex;
    }
    
    /**
     * Get the Common Goal Y index.
     *
     * @return The integer id of Common Goal Y.
     */
    public int getCGYindex() {
        return CGYindex;
    }
    
    /**
     * Get the score at the top of the Common Goal X stack.
     *
     * @return The score at the top of the Common Goal stack.
     */
    public int getTopCGXscore() {
        return topCGXscore;
    }
    
    /**
     * Set the score at the top of the Common Goal X stack. <br>
     * Called when receiving {@link it.polimi.ingsw.model.messages.CommonGoalMessage}
     *
     * @param topCGXscore The new score.
     */
    public void setTopCGXscore(int topCGXscore) {
        this.topCGXscore = topCGXscore;
    }
    
    /**
     * Get the score at the top of the Common Goal Y stack.
     *
     * @return The score at the top of the Common Goal stack.
     */
    public int getTopCGYscore() {
        return topCGYscore;
    }
    
    /**
     * Set the score at the top of the Common Goal Y stack. <br>
     * Called when receiving {@link it.polimi.ingsw.model.messages.CommonGoalMessage}
     *
     * @param topCGYscore The new score.
     */
    public void setTopCGYscore(int topCGYscore) {
        this.topCGYscore = topCGYscore;
    }
    
    /**
     * Get the list of players' nicknames.
     *
     * @return The list of players' nicknames.
     */
    public List<String> getPlayersNicknames() {
        return playersNicknames;
    }
    
    /**
     * Set the list of players' nicknames. <br>
     * Called when receiving {@link StartGameMessage}
     *
     * @param playersNicknames The list of players' nicknames.
     */
    public void setPlayersNicknames(List<String> playersNicknames) {
        this.playersNicknames = playersNicknames;
    }
    
    /**
     * Get the nickname of the current player.
     *
     * @return The nickname of the current player.
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Set the nickname of the current player. <br>
     * Called when receiving {@link it.polimi.ingsw.model.messages.CurrentPlayerMessage}
     *
     * @param currentPlayer The nickname of the current player.
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
}
