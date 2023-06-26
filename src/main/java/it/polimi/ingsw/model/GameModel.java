package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.utils.mvc.ModelListener;
import it.polimi.ingsw.view.messages.ChatMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Game model class to be used as a representation of the game's state by the controller
 */
public class GameModel {
    
    private final int numPlayers;
    
    private final int commonGoalNumX;
    private final int commonGoalNumY;
    
    private final Stack<Integer> commonGoalStackX;
    private final Stack<Integer> commonGoalStackY;
    
    private boolean lastTurn;
    
    private int currentPlayerIndex;
    private final List<Player> players;
    
    private final Map<String, ModelListener> listeners;
    
    private final Board board;
    
    private final TileBag tileBag;
    
    private boolean gameEnded = false;
    
    public void setGameEnded(boolean b) {
        gameEnded = b;
    }
    
    public boolean getGameEnded() {
        return gameEnded;
    }
    
    /**
     * Disambiguation enum for the common goals
     */
    public enum CGType {
        X, Y
    }
    
    /**
     * Initialize an empty model for the given number of players.
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
        
        this.listeners = new HashMap<>(numPlayers);
    }
    
    /**
     * Notify all listeners of the start of the game
     */
    public void startGame() {
        this.notifyStartGame();
    }
    
    /**
     * Initilize a model from the entire state of the game.
     *
     * @param numPlayers number of players for the game
     * @param commonGoalNumX id for the first common goal
     * @param commonGoalNumY id for the second common goal
     * @param CGXS stack of scores for the first common goal
     * @param CGYS stack of scores for the second common goal
     * @param board board of the game
     * @param tileBag tile bag of the game
     */
    public GameModel(int numPlayers, int commonGoalNumX, int commonGoalNumY, Stack<Integer> CGXS, Stack<Integer> CGYS, Board board, TileBag tileBag) {
        this.numPlayers = numPlayers;
        this.commonGoalNumX = commonGoalNumX;
        this.commonGoalNumY = commonGoalNumY;
        this.commonGoalStackX = CGXS;
        this.commonGoalStackY = CGYS;
        this.players = new ArrayList<>(numPlayers);
        this.board = board;
        this.tileBag = tileBag;
        this.listeners = new HashMap<>(numPlayers);
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
    
    /**
     * Set the game state to LastTurn. Assigns the current player the bonus point for finishing first.
     */
    public void setLastTurn() {
        lastTurn = true;
        players.get(getCurrentPlayerIndex()).setBonusScore(1);//add the bonus point to the first who finishes the shelf
    }
    
    /**
     * Check if it's the last turn
     *
     * @return true if it's the last turn, false otherwise
     */
    public boolean isLastTurn() {
        return this.lastTurn;
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
    }
    
    /**
     * Returns the entire player list <br>
     * Internal player indices are guaranteed to be consistent with the ones from this list.
     *
     * @return Full list of players
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * Returns the list of player nicknames
     *
     * @return Full list of player nicknames
     */
    public List<String> getNicknames() {
        return players.stream().map(Player::getNickname).toList();
    }
    
    /**
     * Return the index of the current player in the list returned by {@link #getPlayers() getPlayers}
     *
     * @return The index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
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
     * Set the current player index
     *
     * @param currentPlayerIndex new index of the current player
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
        notifyCurrentPlayerChange();
    }
    
    /**
     * Adds given score to the current player's common goal score. <br>
     * Notifies all listeners of the change.
     *
     * @param score Integer score to give the current player
     * @param t     CGType of the common goal to add the score to
     */
    public void addCurrentPlayerCommongGoalScore(int score, CGType t) {
        Player player = this.getCurrentPlayer();
        int i = player.addCommonGoalScore(score);
        int newScore;
        switch( t ) {
            case X -> {
                player.setCompletedGoalX(true);
                try {
                    newScore = this.commonGoalStackX.peek();
                }
                catch( EmptyStackException e ) {
                    newScore = 0;
                }
                notifyAllListeners(new CommonGoalMessage(CGType.X, newScore));
            }
            case Y -> {
                player.setCompletedGoalY(true);
                
                try {
                    newScore = this.commonGoalStackY.peek();
                }
                catch( EmptyStackException e ) {
                    newScore = 0;
                }
                notifyAllListeners(new CommonGoalMessage(CGType.Y, newScore));
            }
        }
        notifyAllListeners(new UpdateScoreMessage(i, UpdateScoreMessage.Type.CommonGoal, player.getNickname()));
    }
    
    /**
     * Set the new score for the current player's personal goal. <br>
     * Notifies all listeners of the change.
     *
     * @param score Integer score to give the current player
     */
    public void setCurrentPlayerPersonalScore(int score) {
        Player player = this.getCurrentPlayer();
        player.setPersonalGoalScore(score);
        notifyAllListeners(new UpdateScoreMessage(score, UpdateScoreMessage.Type.PersonalGoal, player.getNickname()));
    }
    
    /**
     * Set the new score for the current player's adjacency score. <br>
     * Notifies all listeners of the change.
     *
     * @param score Integer score to give the current player
     */
    public void setCurrentPlayerAdjacencyScore(int score) {
        Player player = this.getCurrentPlayer();
        player.setAdjacencyScore(score);
        notifyAllListeners(new UpdateScoreMessage(score, UpdateScoreMessage.Type.Adjacency, player.getNickname()));
    }
    
    /**
     * Performa a board refill and notify all listeners of the new board.
     */
    public void refillBoard() {
        this.board.refill(this.tileBag);
        notifyAllListeners(new BoardMessage(this));
    }
    
    /**
     * Gets the amount of tiles left in play for the given type of tile.
     *
     * @param tile Type of tile to check
     * @return Amount of tile left (counts both bag and board)
     *
     * @throws ClassCastException if used with {@link Tile#NOTILE}
     */
    public int getTileAmount(Tile tile) {
        return this.tileBag.getTileAmount(tile);
    }
    
    /**
     * Get tile on the board at the given coordinate
     *
     * @param coordinate Coordinate on board to check
     * @return Tile at coordinate, or null if the coordinate is not on the board
     */
    public Tile getTile(Coordinate coordinate) {
        return board.getTile(coordinate);
    }
    
    /**
     * Gets the list of all legal coordinates of the board, regardless of their contents.
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
     * Remove tiles at the selected coordinates from the board.
     * Notifies all listeners of the board change.
     *
     * @param selection List of coordinates to remove tiles from
     */
    public void removeSelection(List<Coordinate> selection) {
        this.board.removeSelection(selection);
        notifyBoardChange();
    }
    
    /**
     * Shelves the given list of tiles on the current player's shelf at the given column. <br>
     * Assumes that the column has enough space for the entire list. <br>
     * Notifies all listeners of the shelf change.
     *
     * @param orderedTiles List of tiles to put on shelf, from first to last
     * @param column       Column of the shelf to insert the tiles at
     */
    public void shelveSelection(List<Tile> orderedTiles, int column) {
        this.tileBag.removeSelection(orderedTiles);
        this.getCurrentPlayer().getShelf().addTiles(orderedTiles, column);
        notifyShelfChange();
    }
    
    /**
     * Add a listener to the model (or reset the listener with the given name). <br>
     * At least one listener for each player should be supplied.
     *
     * @param name     Listener name, should be the nickname for listeners relaying information to clients
     * @param listener Reference to the listener
     */
    public void addListener(String name, ModelListener listener) {
        this.listeners.put(name, listener);
    }
    
    /**
     * Notify all listeners with a message.<br>
     * Only works if the game isn't terminated yet.
     *
     * @param msg Message to send to all listeners
     * @param <T> Type of message to send
     */
    private <T extends ModelMessage<?>> void notifyAllListeners(T msg) {
        if( gameEnded )
            return;
        for( ModelListener listener : this.listeners.values() ) {
            listener.update(msg);
        }
    }
    
    /**
     * Broker a chat message to the correct client.
     *
     * @param chat Message to be sent.
     */
    public void chatBroker(ChatMessage chat) {
        IncomingChatMessage message = new IncomingChatMessage(
                chat.getPayload(), chat.getPlayerNickname(), chat.getDestination());
        
        if( chat.getDestination().equals("ALL") ) {
            notifyAllListeners(message);
        }else if( this.listeners.containsKey(chat.getDestination()) ) {
            ModelListener targetListener = this.listeners.get(chat.getDestination());
            targetListener.update(message);
            this.listeners.get(chat.getPlayerNickname()).update(message);
        }
    }
    
    /**
     * Notify all clients of a change in the board. <br>
     * A new Board object is sent to all clients.
     */
    private void notifyBoardChange() {
        notifyAllListeners(new BoardMessage(this));
    }
    
    /**
     * Notify all clients when the current player is changed. <br>
     * The nickname of the new current player is notified to all clients.
     */
    private void notifyCurrentPlayerChange() {
        notifyAllListeners(new CurrentPlayerMessage(this.getCurrentPlayer().getNickname()));
    }
    
    /**
     * Notify all client of a change in the shelf of the current player. <br>
     * A shelf object is sent to all clients.
     */
    private void notifyShelfChange() {
        notifyAllListeners(new ShelfMessage(this.getCurrentPlayer().getShelf(), this.getCurrentPlayer().getNickname()));
    }
    
    /**
     * Notify all clients of the start of the game. <br>
     * Sends the entire game state to each client (this is done separately, to keep personal goals secret).
     */
    private void notifyStartGame() {
        // we have to create separate messages for each client
        for( Player x : players ) {
            ModelListener playerListener = this.listeners.get(x.getNickname());
            playerListener.update(
                    new StartGameMessage(
                            this.board,
                            this.players,
                            this.getCurrentPlayer().getNickname(),
                            x.getPg(),
                            this.commonGoalNumX, this.peekStackCGX(),
                            this.commonGoalNumY, this.peekStackCGY())
            );
        }
    }
    
    /**
     * Notify the clients of the end of the game. <br>
     * Sends the nickname of the winner and the leaderboard to all clients.
     */
    public void notifyWinner() {
        List<Player> winningPlayers = this.players.stream()
                //there cannot be any equal element
                .sorted((x, y) -> {
                    if( x.getScore() > y.getScore() ) {
                        return -1;
                    }else if( x.getScore() == y.getScore() ) {
                        return this.players.indexOf(x) > this.players.indexOf(y) ? -1 : 1;
                    }else {
                        return 1;
                    }
                }).toList();
        
        Map<String, Integer> leaderboard = new LinkedHashMap<>();
        for( Player x : winningPlayers ) {
            leaderboard.put(x.getNickname(), x.getScore());
        }
        
        String winner = winningPlayers.get(0).getNickname();
        notifyAllListeners(new EndGameMessage(winner, leaderboard));
    }
    
    /**
     * Notify the client with the given nickname with a server message.
     *
     * @param listenerName Name of the listener to notify
     * @param msg          Server Response Message to send
     */
    public void notifyServerMessage(String listenerName, ServerResponseMessage msg) {
        ModelListener listener = this.listeners.get(listenerName);
        listener.update(msg);
    }
    
    /**
     * Peek the top of the CGX score stack.
     *
     * @return The top of the stack, or 0 if the stack is empty.
     */
    public int peekStackCGX() {
        try {
            return commonGoalStackX.peek();
        }
        catch( EmptyStackException e ) {
            return 0;
        }
    }
    
    /**
     * Peek the top of the CGY score stack.
     *
     * @return The top of the stack, or 0 if the stack is empty.
     */
    public int peekStackCGY() {
        try {
            return commonGoalStackY.peek();
        }
        catch( EmptyStackException e ) {
            return 0;
        }
    }
    
    /**
     * Get the entire Board object.
     * @return The Board object.
     */
    public Board getBoard() {
        return this.board;
    }
    
    /**
     * Get the TileBag object.
     * @return The TileBag object.
     */
    public TileBag getTileBag() {
        return this.tileBag;
    }
    
    /**
     * Add an already created player to the game. (used to restore a game from a save file)
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    /**
     * GameModel's custom gson serializer
     */
    protected static class ModelSerializer implements JsonSerializer<GameModel> {
        
        @Override
        public JsonElement serialize(GameModel model, Type typeOfSrc, JsonSerializationContext context) {
            var result = new JsonObject();
            Gson gson = new GsonBuilder().registerTypeAdapter(Player.class,
                                                              new Player.PlayerSerializer()).registerTypeAdapter(
                    Board.class, new Board.BoardSerializer()).create();
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
    
    /**
     * Return the json serialization of the object.
     *
     * @return The state of the GameModel object in a json-formatted string
     */
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameModel.class, new ModelSerializer()).create();
        return gson.toJson(this, GameModel.class);
    }
    
    /**
     * Write the serialized json string of the object directly to the specified file.
     *
     * @param path The path to file to be written
     * @throws IOException If an error occur while writing the file
     */
    public void toJson(String path) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameModel.class, new ModelSerializer()).create();
        FileWriter writer = new FileWriter(path);
        
        gson.toJson(this, GameModel.class, writer);
        writer.flush();
        writer.close();
    }
    
    /**
     * GameModel's' custom gson deserializer
     */
    static public class ModelDeserializer implements JsonDeserializer<GameModel> {
        
        @Override
        public GameModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().registerTypeAdapter(Shelf.class,
                                                              new Shelf.ShelfDeserializer()).registerTypeAdapter(
                    Player.class, new Player.PlayerDeserializer()).registerTypeAdapter(Board.class,
                                                                                       new Board.BoardDeserializer()).registerTypeAdapter(
                    TileBag.class, new TileBag.TileBagDeserializer()).create();
            var stackToken = new TypeToken<Stack<Integer>>() {
            }.getType();
            var numPlayer =
                    gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "PlayersNumber"), int.class);
            var CGX = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CommonGoalX"), int.class);
            var CGY = gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CommonGoalY"), int.class);
            Stack<Integer> xStack =
                    gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CGX"), stackToken);
            Stack<Integer> yStack =
                    gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CGY"), stackToken);
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
            var currentPlayer =
                    gson.fromJson(ResourcesManager.JsonManager.getElementByAttribute(json, "CurrentPlayer"), int.class);
            result.setCurrentPlayerIndex(currentPlayer);
            return result;
        }
    }
    
}
