package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.utils.exceptions.CommonException;
import it.polimi.ingsw.utils.exceptions.InvalidStringException;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;

/**
 * Model's internal board representation
 */
public class Board implements Serializable {
    
    private final Map<Coordinate, Tile> tileOccupancy;
    
    public static final int maxSize = 9;
    
    /**
     * Initialize board for the given number of players. <br>
     * The player number cannot change dynamically throughout the game.
     *
     * @param numPlayers Total number of players for the game.
     */
    public Board(int numPlayers) {
        this.tileOccupancy = new HashMap<>();
        
        Coordinate[] commonCoordinates = {
                new Coordinate(1, 4), new Coordinate(1, 5), new Coordinate(2, 3),
                new Coordinate(2, 4), new Coordinate(2, 5), new Coordinate(3, 1),
                new Coordinate(3, 2), new Coordinate(3, 3), new Coordinate(3, 4),
                new Coordinate(3, 5), new Coordinate(3, 6), new Coordinate(4, 1),
                new Coordinate(4, 2), new Coordinate(4, 3), new Coordinate(4, 4),
                new Coordinate(4, 5), new Coordinate(4, 6), new Coordinate(4, 7),
                new Coordinate(5, 2), new Coordinate(5, 3), new Coordinate(5, 4),
                new Coordinate(5, 5), new Coordinate(5, 6), new Coordinate(5, 7),
                new Coordinate(6, 3), new Coordinate(6, 4), new Coordinate(6, 5),
                new Coordinate(7, 3), new Coordinate(7, 4),
        };
        for( Coordinate coordinate : commonCoordinates ) {
            this.tileOccupancy.put(coordinate, Tile.NOTILE);
        }
        
        if( numPlayers > 2 ) {
            Coordinate[] threePlayerCoordinates = {
                    new Coordinate(0, 5), new Coordinate(2, 2), new Coordinate(2, 6),
                    new Coordinate(3, 0), new Coordinate(5, 8), new Coordinate(6, 2),
                    new Coordinate(6, 6), new Coordinate(8, 3),
            };
            for( Coordinate coordinate : threePlayerCoordinates ) {
                this.tileOccupancy.put(coordinate, Tile.NOTILE);
            }
        }
        
        if( numPlayers > 3 ) {
            Coordinate[] fourPlayerCoordinates = {
                    new Coordinate(0, 4), new Coordinate(1, 3), new Coordinate(3, 7),
                    new Coordinate(4, 0), new Coordinate(4, 8), new Coordinate(5, 1),
                    new Coordinate(7, 5), new Coordinate(8, 4),
            };
            for( Coordinate coordinate : fourPlayerCoordinates ) {
                this.tileOccupancy.put(coordinate, Tile.NOTILE);
            }
        }
    }
    
    /**
     * Private Board contructor for an already initialized board.
     * @param map Map to initialize the board.
     */
    private Board(Map<Coordinate, Tile> map) {
        tileOccupancy = map;
    }
    
    /**
     * Get the tile on the board at the given coordinate
     *
     * @param coordinate Coordinate to search for a tile
     * @return Tile at coordinate, null if coordinate is not in the board
     */
    public Tile getTile(Coordinate coordinate) {
        return this.tileOccupancy.get(coordinate);
    }
    
    /**
     * Get Coordinate to File mapping for the current board. <br>
     * The map only contains "legal" coordinates, meaning coordinates usable when playing with the number of players
     * the board has been initialized with.
     *
     * @return Copy of the Coordinate->Tile map
     */
    public Map<Coordinate, Tile> getTiles() {
        return new HashMap<>(this.tileOccupancy);
    }
    
    /**
     * Remove the tiles in the list of selected coordinates. <br>
     * Coordinates must be already present in the board.
     *
     * @param selection List of coordinates to be emptied
     */
    public void removeSelection(List<Coordinate> selection) {
        for( Coordinate coordinate : selection ) {
            this.tileOccupancy.put(coordinate, Tile.NOTILE);
        }
    }
    
    /**
     * Insert tile at given coordinate. <br>
     * Coordinate must be already present in the board.
     *
     * @param coordinate Coordinate to set the tile at
     * @param tile       Tile to set at given coordinate
     *
     * @throws OutOfBoundCoordinateException    If the coordinate doesn't belong to the board
     * @throws OccupiedTileException            If the coordinate is already occupied
     */
    public void insertTile(Coordinate coordinate, Tile tile) throws OutOfBoundCoordinateException, OccupiedTileException {
        if( tileOccupancy.containsKey(coordinate) )
            if( tileOccupancy.get(coordinate) == Tile.NOTILE )
                this.tileOccupancy.replace(coordinate, tile);
            else
                throw new OccupiedTileException();
        else
            throw new OutOfBoundCoordinateException(coordinate);
    }
    
    /**
     * Refill the board using the given tilebag. <br>
     * The bag will not be modified, as the contents of the tilebag include the contents of the board. <br>
     * If not enough tiles are present to refill the board, it will only be partially refilled. <br>
     * Refilling happens in a breadth-first approach starting at a random coordinate.
     *
     * @param tileBag Bag to draw the tiles from.
     */
    public void refill(TileBag tileBag) {
        var engine = new Random();
        List<Coordinate> coordList = tileOccupancy.keySet().stream().toList();
        this.removeSelection(coordList);
        
        boolean[][] visited = new boolean[9][9];
        
        Predicate<Coordinate> needVisiting = x ->
                this.tileOccupancy.containsKey(x)
                && !visited[x.row()][x.col()];
        
        ArrayList<Coordinate> selected = new ArrayList<>();
        Queue<Coordinate> coordGraph = new LinkedList<>();
        Coordinate initial = coordList.get(engine.nextInt(0, coordList.size()));
        coordGraph.add(initial);
        
        int i = 1;
        int nTiles = Math.min(tileBag.currentTileNumber(), tileOccupancy.size());
        
        while( !coordGraph.isEmpty() ) {
            Coordinate current = coordGraph.peek();
            
            selected.add(coordGraph.remove());
            visited[current.row()][current.col()] = true;
            
            if( needVisiting.test(current.getDown()) && i < nTiles ) {
                coordGraph.add(current.getDown());
                visited[current.getDown().row()][current.getDown().col()] = true;
                i++;
            }
            if( needVisiting.test(current.getLeft()) && i < nTiles ) {
                coordGraph.add(current.getLeft());
                visited[current.getLeft().row()][current.getLeft().col()] = true;
                i++;
            }
            if( needVisiting.test(current.getRight()) && i < nTiles ) {
                coordGraph.add(current.getRight());
                visited[current.getRight().row()][current.getRight().col()] = true;
                i++;
            }
            if( needVisiting.test(current.getUp()) && i < nTiles ) {
                coordGraph.add(current.getUp());
                visited[current.getUp().row()][current.getUp().col()] = true;
                i++;
            }
        }
        
        var bag = tileBag.getAllBag();
        
        try {
            for( Coordinate coord : selected ) {
                Tile randomTile;
                do {
                    randomTile = Tile.ALL_TILES[engine.nextInt(0, Tile.NUM_TILES)]; //git blame someone-else
                }
                while( bag.get(randomTile) == 0 );
                
                insertTile(coord, randomTile);
                bag.put(randomTile, bag.get(randomTile) - 1);
            }
        }
        catch( CommonException ex ) {
            System.err.println("there shouldn't be an error in there...");
        }
    }
    
    
    /**
     * Get the matrix representation of the board as a 9*9 tiles Matrix. <br>
     * Invalid positions in the board are represented with null values within the matrix, while unoccupied positions
     * contain {@link Tile#NOTILE}
     *
     * @return The tile-matrix rapresentation of the board.
     */
    
    public Tile[][] getAsMatrix() {
        var result = new Tile[9][9];
        for( int i = 0; i < 9; i++ ) {
            for( int j = 0; j < 9; j++ ) {
                var coord = new Coordinate(i, j);
                result[i][j] = this.tileOccupancy.getOrDefault(coord, null);
            }
        }
        return result;
    }
    
    /**
     * Board's custom gson serializer<br>
     */
    @Override
    public String toString() {
        Gson g = new GsonBuilder().registerTypeAdapter(getClass(), new BoardSerializer()).create();
        return g.toJson(this, this.getClass());
    }
    
    /**
     * Board's custom gson serializer
     */
    public static class BoardSerializer implements JsonSerializer<Board> {
        
        /**
         * Serialization function
         * @param src the object that needs to be converted to Json.
         * @param typeOfSrc the actual type (fully genericized version) of the source object.
         * @param context Serialization context utility
         * @return the serialized object as a JsonElement
         * <p>
         * A valid json representation is provided: <br>
         * <pre><code>
         * "Board": [ ["(-,-)", "(-,-)", "(-,-)", "(-,-)", "(N,0)", "(N,0)", "(-,-)", "(-,-)", "(-,-)"], <br>
         *            ["(-,-)", "(-,-)", "(-,-)", "(N,0)", "(N,0)", "(N,0)", "(-,-)", "(-,-)", "(-,-)"], <br>
         *            ["(-,-)", "(-,-)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(-,-)", "(-,-)"], <br>
         *            ["(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(-,-)"], <br>
         *            ["(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)"], <br>
         *            ["(-,-)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)"], <br>
         *            ["(-,-)", "(-,-)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(-,-)", "(-,-)"], <br>
         *            ["(-,-)", "(-,-)", "(-,-)", "(N,0)", "(N,0)", "(N,0)", "(-,-)", "(-,-)", "(-,-)"], <br>
         *            ["(-,-)", "(-,-)", "(-,-)", "(N,0)", "(N,0)", "(-,-)", "(-,-)", "(-,-)", "(-,-)"] ],
         * </code></pre>
         */
        
        @Override
        public JsonElement serialize(Board src, Type typeOfSrc, JsonSerializationContext context) {
            String def = "(-,-)";
            var mat = new String[9][9];
            for( int i = 0; i < 9; i++ ) {
                for( int j = 0; j < 9; j++ ) {
                    var coord = new Coordinate(i, j);
                    if( src.tileOccupancy.containsKey(coord) ) {
                        mat[i][j] = src.getTile(coord).toString();
                    }else {
                        mat[i][j] = def;
                    }
                }
            }
            Gson gson = new GsonBuilder().create();
            return gson.toJsonTree(mat);
        }
    }
    
    /**
     * Board's custom gson deserializer <br>
     */
    public static class BoardDeserializer implements JsonDeserializer<Board> {
        
        /**
         * Deserialization function
         * @param json The Json data being deserialized <br>
         * A valid json representation is provided: <br>
         *
         * "Board": [ ["(-,-)", "(-,-)", "(-,-)", "(-,-)", "(N,0)", "(N,0)", "(-,-)", "(-,-)", "(-,-)"], <br>
         *            ["(-,-)", "(-,-)", "(-,-)", "(N,0)", "(N,0)", "(N,0)", "(-,-)", "(-,-)", "(-,-)"], <br>
         *            ["(-,-)", "(-,-)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(-,-)", "(-,-)"], <br>
         *            ["(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(-,-)"], <br>
         *            ["(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)"], <br>
         *            ["(-,-)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)"], <br>
         *            ["(-,-)", "(-,-)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(N,0)", "(-,-)", "(-,-)"], <br>
         *            ["(-,-)", "(-,-)", "(-,-)", "(N,0)", "(N,0)", "(N,0)", "(-,-)", "(-,-)", "(-,-)"], <br>
         *            ["(-,-)", "(-,-)", "(-,-)", "(N,0)", "(N,0)", "(-,-)", "(-,-)", "(-,-)", "(-,-)"] ],
         * @param typeOfT The type of the Object to deserialize to
         * @param context Deserialization context utility
         * @return The deserializer Board object
         * @throws JsonParseException If an invalid json object is provided
         */
        @Override
        public Board deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().create();
            var hashMap = new HashMap<Coordinate, Tile>();
            var board = gson.fromJson(json, String[][].class);
            for( int i = 0; i < 9; i++ ) {
                for( int j = 0; j < 9; j++ ) {
                    var coord = new Coordinate(i, j);
                    try {
                        var tile = Tile.fromString(board[i][j]);
                        hashMap.put(coord, tile);
                    }
                    catch( InvalidStringException ignored ) {
                    }
                }
            }
            return new Board(hashMap);
        }
        
    }
}
