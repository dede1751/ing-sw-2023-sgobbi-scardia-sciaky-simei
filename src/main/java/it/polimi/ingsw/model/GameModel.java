package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Game model class to be used as a representation of the game's state by the controller
 */
public class GameModel {
    
    private final int commonGoalNumX;
    private final int commonGoalNumY;
    
    private final int numPlayers;
    
    private final Stack<Integer> commonGoalStackX;
    private final Stack<Integer> commonGoalStackY;
    
    private final int currentPlayerIndex;
    private final boolean gameOver;
    private final List<Player> players;
    private final Board board;
    
    private final TileBag tileBag;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    /**
     * Initialize an empty model
     *
     * @param numPlayers  number of players for the game
     * @param commonGoalX id for the first common goal
     * @param commonGoalY id for the second common goal
     */
    public GameModel(int numPlayers, int commonGoalX, int commonGoalY) {
        this.commonGoalNumX = commonGoalX;
        this.commonGoalStackX = new Stack<>();
        
        this.commonGoalNumY = commonGoalY;
        this.commonGoalStackY = new Stack<>();
        switch( numPlayers ) {
            case 2:
                this.commonGoalStackX.push(4);
                this.commonGoalStackX.push(8);
                this.commonGoalStackY.push(4);
                this.commonGoalStackY.push(8);
            case 3:
                this.commonGoalStackX.push(4);
                this.commonGoalStackX.push(6);
                this.commonGoalStackX.push(8);
                this.commonGoalStackY.push(4);
                this.commonGoalStackY.push(6);
                this.commonGoalStackY.push(8);
            case 4:
                this.commonGoalStackX.push(2);
                this.commonGoalStackX.push(4);
                this.commonGoalStackX.push(6);
                this.commonGoalStackX.push(8);
                this.commonGoalStackY.push(4);
                this.commonGoalStackY.push(2);
                this.commonGoalStackY.push(6);
                this.commonGoalStackY.push(8);
            
        }
        
        
        this.tileBag = new TileBag();
        this.gameOver = false;
        
        this.board = new Board(numPlayers);
        this.players = new ArrayList<>();
        this.numPlayers = numPlayers;
        this.currentPlayerIndex = 0;
        System.out.println("Initialized game with " + numPlayers + " players");
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    /**
     * Get id of first common goal
     *
     * @return integer id of first common goal
     */
    public int getCommonGoalX() {
        return commonGoalNumX;
    }
    
    /**
     * Get id of second common goal
     *
     * @return integer id of second common goal
     */
    public int getCommonGoalY() {
        return commonGoalNumY;
    }
    
    public int getNumPlayers() {
        return numPlayers;
    }
    
    /**
     * Pops the score from the top of the score stack for the first common goal
     *
     * @return integer value of first common goal score
     */
    public int popStackCGX() {
        return commonGoalStackX.pop();
    }
    
    /**
     * Pops the score from the top of the score stack for the second common goal
     *
     * @return integer value of second common goal score
     */
    public int popStackCGY() {
        return commonGoalStackY.pop();
    }
    
    /**
     * Checks if the game is on its final turn
     *
     * @return true if the turn is final (although some players might still have to play)
     */
    public boolean isFinalTurn() {
        return this.gameOver;
    }
    
    /**
     * Adds player with given nickname and personal goal to the player pool
     * It is the controller's duty to avoid nickname duplicates and not add too many players
     *
     * @param nickname Unique string nickname of the player
     * @param pgID     Integer ID for the player's personal goal
     */
    public void addPlayer(String nickname, int pgID) {
        players.add(new Player(nickname, pgID));
        System.out.println("Player " + nickname + " with id: " + pgID);
        
        this.pcs.firePropertyChange("ADD PLAYER", "", nickname);
    }
    
    /**
     * Returns the entire player list
     * Internal player indices are guaranteed to be consistent with the ones from this list.
     *
     * @return Full list of players
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * Return the current player
     *
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    /**
     * Set current player based on index from getPlayers() list
     *
     * @param index Index of player to set as first in the player list
     *
     * @return Selected player
     */
    public Player setCurrentPlayer() {
        return players.get(getCurrentPlayerIndex());
    }
    
    /**
     * Get tile on the board at the given coordinate
     *
     * @param coordinate Coordinate on board to check
     *
     * @return Tile at coordinate, or null if the coordinate is not on the board
     */
    public Tile getTile(Coordinate coordinate) {
        return board.getTile(coordinate);
    }
    
    /**
     * Gets the list of all coordinates of the board, regardless of their contents
     *
     * @return List of all legal coordinates
     */
    public List<Coordinate> getAllCoordinates() {
        return board.getTiles().keySet().stream().toList();
    }
    
    /**
     * Get the list of all coordinates of the board which are not empty
     *
     * @return List of all non-empty coordinates
     */
    public List<Coordinate> getOccupied() {
        return this.board.getTiles()
                .entrySet()
                .stream()
                .filter(x -> !(Tile.NOTILE.equals(x.getValue())))
                .map(Map.Entry::getKey)
                .toList();
    }
    
    /**
     * Insert the given tile at the given coordinate on the board.
     *
     * @param coordinates Coordinate at which to insert the tile
     * @param tile        Tile type to insert
     *
     * @throws OccupiedTileException Trying to insert a tile in an already occupied position
     */
    public void insertTile(Coordinate coordinates, Tile tile) throws OccupiedTileException, OutOfBoundCoordinateException {
        this.board.insertTile(coordinates, tile);
    }
    
    /**
     * Remove tiles at the selected coordinates from the board
     *
     * @param selection List of tiles to empty on the board
     */
    public void removeSelection(List<Coordinate> selection) {
        this.board.removeSelection(selection);
    }
    
    /**
     * Shelves the given list of tiles on the current player's shelf at the given column.
     * Assumes that the column has enough space for the entire list.
     *
     * @param orderedTiles List of tiles to put on shelf, from first to last
     * @param column       Column of the shelf to insert the tiles at
     */
    public void shelveSelection(List<Tile> orderedTiles, int column) {
        this.tileBag.removeSelection(orderedTiles);
        this.getCurrentPlayer().getShelf().addTiles(orderedTiles, column);
    }
    
    /**
     * Adds given score to the current player's score
     *
     * @param score Integer score to give the current player
     *
     * @return Total score for current player
     */
    public int addCurrentPlayerScore(int score) {
        return this.getCurrentPlayer().addScore(score);
    }
    
    /**
     * Gets the amount of tiles left in play for the given type of tile
     * Will throw a class cast exception if being used with Tile.NOTILE
     *
     * @param tile Type of tile to check
     *
     * @return Amount of tile left (counts both bag and board)
     */
    public int getTileAmount(Tile tile) {
        return this.tileBag.getTileAmount(tile);
    }
    
}
