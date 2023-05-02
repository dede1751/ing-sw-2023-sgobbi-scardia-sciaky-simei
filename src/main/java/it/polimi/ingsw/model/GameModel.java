package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.utils.exceptions.DuplicateNickname;
import it.polimi.ingsw.utils.exceptions.NoPlayerWithNickname;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.files.ResourcesManager;
import it.polimi.ingsw.view.messages.ChatMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
    
    private final Map<String, Client> clientMap;
    
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
        
        this.clientMap = new HashMap<>(numPlayers);
    }
    
    public void startGame() {
        this.notifyStartGame();
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
        this.clientMap = new HashMap<>(numPlayers);
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
     * Checks if the game is on its final turn and set gameOver to true if the turn is final
     * (although some players might still have to play)
     */
    public void setLastTurn() {
        lastTurn = true;
        players.get(getCurrentPlayerIndex()).setBonusScore(1);//add the bonus point to the first who finishes the shelf
        
    }
    
    /**
     * Check if it's the last turn
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
     * Returns the entire player list
     * Internal player indices are guaranteed to be consistent with the ones from this list.
     *
     * @return Full list of players
     */
    public List<Player> getPlayers() {
        return players;
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
     * @param currentPlayerIndex new index of the current player
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
        notifyCurrentPlayerChange();
    }
    
    /**
     * Adds given score to the current player's score
     *
     * @param score Integer score to give the current player
     * @return Total score for current player
     */
    public int addCurrentPlayerCommongGoalScore(int score) { return this.getCurrentPlayer().addCommonGoalScore(score); }
    
    
    /**
     * Gets the amount of tiles left in play for the given type of tile
     * Will throw a class cast exception if being used with Tile.NOTILE
     *
     * @param tile Type of tile to check
     * @return Amount of tile left (counts both bag and board)
     */
    public int getTileAmount(Tile tile) {
        return this.tileBag.getTileAmount(tile);
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
        notifyBoardChange();
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
        notifyShelfChange();
    }
    
    /**
     * Add client reference to model.
     * This method should be called only after {@link it.polimi.ingsw.model.GameModel#addPlayer(String, int)}.
     * It is necessary for the correct functioning of the networking communication.
     *
     * @param nickname nickname of the client
     * @param client   reference to the client object
     *
     * @throws DuplicateNickname    if it exists a client linked to the same nickname
     * @throws NoPlayerWithNickname if a player with the same nickname doesn't exist. This method should be
     *                              called only after {@link it.polimi.ingsw.model.GameModel#addPlayer(String, int) addPlayer}
     */
    public void addClient(String nickname, Client client) throws DuplicateNickname, NoPlayerWithNickname {
        if( this.clientMap.containsKey(nickname) ) {
            throw new DuplicateNickname(nickname);
        }else if( this.players.stream().filter((x) -> x.getNickname().equals(nickname)).count() != 1 ) {
            throw new NoPlayerWithNickname(nickname);
        }else {
            this.clientMap.put(nickname, client);
        }
    }
    
    private <T extends ModelMessage<?>> void notifyClient(T msg, Client player) {
        try {
            player.update(msg);
        }
        catch( RemoteException e ) {
            AtomicReference<String> nick = new AtomicReference<>();
            clientMap.entrySet()
                    .stream()
                    .filter((x) -> x.getValue() == player).findFirst()
                    .ifPresent((x) -> nick.set(x.getKey()));
            System.err.println("Unable to update player " + nick);
            System.err.println(e.getMessage());
        }
    }
    
    private <T extends ModelMessage<?>> void notifyAllClient(T msg) {
        for( var n : this.clientMap.entrySet() ) {
            notifyClient(msg, n.getValue());
        }
    }
    
    /**
     * Broker a chat message to the correct client
     * @param chat Message to be sent
     */
    public void chatBroker(ChatMessage chat){
        
        IncomingChatMessage message = new IncomingChatMessage(chat.getPayload(), chat.getPlayerNickname());
        if(chat.getDestination().equals("BROADCAST")){
            notifyAllClient(message);
        }else{
            Client player = this.clientMap.get(chat.getPlayerNickname());
            notifyClient(message, player);
        }
    }
    
    private void notifyBoardChange() {
        var boardMessage = new BoardMessage(this);
        notifyAllClient(boardMessage);
    }
    
    private void notifyCurrentPlayerChange() {
        var pMessage = new CurrentPlayerMessage(this.getCurrentPlayer().getNickname());
        notifyAllClient(pMessage);
    }
    
    private void notifyShelfChange() {
        var shelfMessage = new ShelfMessage(this.getCurrentPlayer().getShelf(), this.getCurrentPlayer().getNickname());
        notifyAllClient(shelfMessage);
    }
    
    private void notifyStartGame() {
        List<String> nicks = players.stream().map(Player::getNickname).toList();
        for(var x : players){
            Client c = this.clientMap.get(x.getNickname());
            var msg = new StartGameMessage(nicks, x.getPg(), this.commonGoalNumX, this.commonGoalNumY);
            notifyClient(msg, c);
        }
    }
    
    /**
     * Notify the clients that the game is ended by sending a leaderboard.
     */
    public void notifyWinner() {
        String winner = this.players.stream()
                .max(Comparator.comparingInt(Player::getScore))
                .orElseThrow()
                .getNickname();
        Map<String, Integer> leaderboard = new HashMap<>();
        for (Player x : this.getPlayers()){
            leaderboard.put(x.getNickname(), x.getScore());
        }
        notifyAllClient(new EndGameMessage(new EndGamePayload(winner, leaderboard)));
    }
    
    // Testing methods
    public int peekStackCGX() {
        return commonGoalStackX.peek();
    }
    
    public int peekStackCGY() {
        return commonGoalStackY.peek();
    }
    
    public Board getBoard() {
        return this.board;
    }
    
    public TileBag getTileBag() {
        return this.tileBag;
    }
    
    private void addPlayer(Player player) {
        players.add(player);
    }
    
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
    
    public String toJson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameModel.class, new ModelSerializer()).create();
        return gson.toJson(this, GameModel.class);
    }
    
    public void toJson(String path) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(GameModel.class, new ModelSerializer()).create();
        FileWriter writer = new FileWriter(path);
        
        gson.toJson(this, GameModel.class, writer);
        writer.flush();
        writer.close();
    }
    
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
