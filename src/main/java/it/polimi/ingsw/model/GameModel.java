package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.observer.Observable;
import it.polimi.ingsw.utils.files.ResourcesManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Game model class to be used as a representation of the game's state by the controller
 */
public class GameModel extends Observable<GameModel.Event> {
    
    public enum Event {
        GAME_START,
        LAST_TURN,
        NEW_CURRENT_PLAYER,
        FINISHED_GAME,
    }
    
    private final int commonGoalNumX;
    private final int commonGoalNumY;
    
    private final int numPlayers;
    
    private final Stack<Integer> commonGoalStackX;
    private final Stack<Integer> commonGoalStackY;
    private String winner;
    
    private int currentPlayerIndex;
    private boolean lastTurn;
    private final List<Player> players;
    private final Board board;
    
    private final TileBag tileBag;
    
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
        
        if( numPlayers > 3 ) {
            this.commonGoalStackX.push(2);
            this.commonGoalStackY.push(2);
        }
        
        this.commonGoalStackX.push(4);
        this.commonGoalStackY.push(4);
        
        if( numPlayers > 2 ) {
            this.commonGoalStackX.push(6);
            this.commonGoalStackY.push(6);
        }
        this.commonGoalStackX.push(8);
        this.commonGoalStackY.push(8);
        
        this.tileBag = new TileBag();
        this.lastTurn = false;
        
        this.board = new Board(numPlayers);
        this.players = new ArrayList<>();
        this.numPlayers = numPlayers;
        this.currentPlayerIndex = 0;
    }
    
    public void startGame() {
        this.setChangedAndNotifyObservers(Event.GAME_START);
    }
    
    private GameModel(int numPlayers, int commonGoalNumX, int commonGoalNumY, Stack<Integer> CGXS, Stack<Integer> CGYS, Board board, TileBag tileBag) {
        this.numPlayers = numPlayers;
        this.commonGoalNumX = commonGoalNumX;
        this.commonGoalNumY = commonGoalNumY;
        this.commonGoalStackX = CGXS;
        this.commonGoalStackY = CGYS;
        this.players = new ArrayList<>(numPlayers);
        this.board = board;
        this.tileBag = tileBag;
        
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
    
    /**
     * Get number of participating players
     *
     * @return number of players
     */
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
    
    
    public Board getBoard() {
        return this.board;
    }
    
    public TileBag getTileBag() {
        return this.tileBag;
    }
    
    /**
     * Checks if the game is on its final turn and set gameOver to true if the turn is final (although some players might still have to play)
     */
    public void setLastTurn() {
        lastTurn = true;
        players.get(getCurrentPlayerIndex()).setBonusScore(1);//add the bonus point to the first who finishes the shelf
        
    }
    
    public boolean isFinalTurn() {
        return this.lastTurn;
    }
    
    public void setWinner(String winner) {
        this.winner = winner;
        setChangedAndNotifyObservers(Event.FINISHED_GAME);
    }
    
    public String getWinner() {
        return this.winner;
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
    }
    
    public void addPlayer(Player player) {
        players.add(player);
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
    
    
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    
    public void setCurrentPlayerIndex(int i) {
        currentPlayerIndex = i;
        setChangedAndNotifyObservers(Event.NEW_CURRENT_PLAYER);
    }
    
    
    /**
     * Return the current player
     *
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
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
        return this.board.getTiles().entrySet().stream().filter(x -> !(Tile.NOTILE.equals(x.getValue()))).map(
                Map.Entry::getKey).toList();
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
    
    
    //TODO  fix the parameters
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
    public int addCurrentPlayerCommongGoalScore(int score) {
        return this.getCurrentPlayer().addCommonGoalScore(score);
    }
    
    public int setCurrentPlayerPersonalGoalScore(int score){
        return this.getCurrentPlayer().setPersonalGoalScore(score);
    }
    
    public int setCurrentPlayerAdjency(int score){
        return this.getCurrentPlayer().setAdjacentScore(score);
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
    
    private void setChangedAndNotifyObservers(Event evt) {
        setChanged();
        notifyObservers(evt);
    }
    
    protected static class ModelSerializer implements JsonSerializer<GameModel> {
        @Override
        public JsonElement serialize(GameModel model, Type typeOfSrc, JsonSerializationContext context) {
            var result = new JsonObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Player.class, new Player.PlayerSerializer()).
                    registerTypeAdapter(Board.class, new Board.BoardSerializer()).create();
            //Common goal properties
            result.addProperty("CommonGoalX", model.commonGoalNumX);
            result.addProperty("CommonGoalY", model.commonGoalNumY);
            result.add("CGX",
                       JsonParser.parseString(gson.toJson(model.commonGoalStackX, new TypeToken<Stack<Integer>>() {
                       }.getType())));
            result.add("CGY",
                       JsonParser.parseString(gson.toJson(model.commonGoalStackY, new TypeToken<Stack<Integer>>() {
                       }.getType())));
            //Board state
            result.add("Board", JsonParser.parseString(gson.toJson(model.board)));
            //Bag
            result.add("TileBag", JsonParser.parseString(gson.toJson(model.tileBag.getAllBag())));
            //Global Players properties
            result.addProperty("PlayersNumber", model.numPlayers);
            result.addProperty("CurrentPlayer", model.currentPlayerIndex);
            var playerNicks = new JsonArray();
            for( var x : model.players ) {
                playerNicks.add(x.getNickname());
            }
            result.add("PlayersNickname", playerNicks);
            //Single players properties
            for( var x : model.players ) {
                result.add(x.getNickname(), JsonParser.parseString(gson.toJson(x)));
            }
            
            return result;
        }
    }
    
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameModel.class, new ModelSerializer()).create();
        return gson.toJson(this, GameModel.class);
    }
    
    static public class ModelDeserializer implements JsonDeserializer<GameModel> {
        @Override
        public GameModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().registerTypeAdapter(Shelf.class, new Shelf.ShelfDeserializer())
                    .registerTypeAdapter(Player.class, new Player.PlayerDeserializer())
                    .registerTypeAdapter(Board.class, new Board.BoardDeserializer())
                    .registerTypeAdapter(TileBag.class, new TileBag.TileBagDeserializer())
                    .create();
            var stackToken = new TypeToken<Stack<Integer>>() {}.getType();
            var numPlayer = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "PlayersNumber"), int.class);
            var CGX =
                    gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CommonGoalX"), int.class);
            var CGY =
                    gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CommonGoalY"), int.class);
            Stack<Integer> xStack = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CGX"), stackToken);
            Stack<Integer> yStack = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CGY"), stackToken);
            var board = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "Board"), Board.class);
            var tileBag =
                    gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "TileBag"), TileBag.class);
            var players = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "PlayersNickname"),
                                        String[].class);
            var result = new GameModel(numPlayer, CGX, CGY, xStack, yStack, board, tileBag);
            for( var p : players ) {
                var player = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, p), Player.class);
                result.addPlayer(player);

            }
            var currentPlayer = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CurrentPlayer"), int.class);
            result.setCurrentPlayerIndex(currentPlayer);
            return result;
        }
    }
}
