package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.utils.exceptions.InvalidStringException;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Shelf representation within the model
 */
public class Shelf {
    
    public final static int N_ROW = 6;
    public final static int N_COL = 5;
    
    /**
     * Shelf represented as a list of stacks, where each stack represents a column.
     * Row: 0 is the bottom row, 5 is the top row
     * Col: 0 is the leftmost column, 4 is the rightmost
     */
    private final List<Stack<Tile>> content;
    
    public Shelf() {
        this.content = new ArrayList<>();
        
        for( int i = 0; i < N_COL; i++ ) {
            this.content.add(i, new Stack<>());
        }
    }
    
    /**
     * Get tile at given x, y coordinates
     *
     * @param row Row of the desired tile
     * @param col Column of the desired tile
     *
     * @return Tile in the position [row, col], NOTILE if out of bounds or empty
     */
    public Tile getTile(int row, int col) {
        try {
            return this.content.get(col).get(row);
        }
        catch( IndexOutOfBoundsException e ) {
            return Tile.NOTILE;
        }
    }
    
    /**
     * Get a matrix representation of the shelf
     *
     * @return The shelf as a matrix of Tile, empty tiles are represented as Tile.NOTILE
     */
    public Tile[][] getAllShelf() {
        Tile[][] result = new Tile[N_ROW][N_COL];
        
        for( int i = 0; i < N_ROW; i++ ) {
            for( int j = 0; j < N_COL; j++ ) {
                result[i][j] = Objects.requireNonNullElse(getTile(i, j), Tile.NOTILE);
            }
        }
        return result;
    }
    
    /**
     * Checks whether the shelf is full
     *
     * @return True if every box of the shelf is occupied (not Tile.NOTILE)
     */
    public boolean isFull() {
        return content.stream().allMatch(x -> x.size() == N_ROW);
    }
    
    /**
     * Get the list of indices for the columns which can still be filled.
     *
     * @param selectionLength Length of the selection, it's supposed to be correct and between 1 and 3
     *
     * @return Index of all columns that have enough space for the selection
     */
    public List<Integer> availableColumns(int selectionLength) {
        var res = new ArrayList<Integer>();
        
        for( int i = 0; i < N_COL; i++ ) {
            if( content.get(i).size() <= (N_ROW - selectionLength) ) {
                res.add(i);
            }
        }
        return res;
    }
    
    /**
     * Get the amount of space left in the given column
     *
     * @param column Integer index of column to check
     *
     * @return Number of non-occupied boxes in the column
     */
    public int spaceInColumn(int column) {
        return N_ROW - content.get(column).size();
    }
    
    /**
     * Get the amount of space left in each column.
     *
     * @return Map linking each available column to the amount of space it has.
     */
    public Map<Integer, Integer> remainingSpace() {
        var res = new HashMap<Integer, Integer>();
        
        for( var x : availableColumns(0) ) {
            res.put(x, spaceInColumn(x));
        }
        return res;
    }
    
    /**
     * Add ordered list of tiles to given column
     *
     * @param tiles Not null, have less or equal than 3 element, be already ordered, NOTILEs will be ignored
     * @param col   Column index, between 0 and 5
     */
    public void addTiles(List<Tile> tiles, int col) {
        for( var x : tiles ) {
            if( x != Tile.NOTILE ) {
                content.get(col).push(x);
            }
        }
    }
    
    /**
     * Protected class that contains the gson custom serializer logic for Shelf.
     * Each tile is represented as its string representation (see Tile.toString()) {@linkplain Tile}
     * Each column is represented as a json Array in objects each named column_i, where i is the index of the column.
     * The json is expected to have the structure {shelf : {
     * column_0 : [...],
     * column_1 : [...],
     * ...
     * }
     * }
     * The names of the attributes are irrelevant
     */
    protected static class ShelfSerializer implements JsonSerializer<Shelf> {
        @Override
        public JsonElement serialize(Shelf shelf, Type typeOfSrc, JsonSerializationContext context) {
            var matrix = shelf.getAllShelf();
            var result = new JsonObject();
            for( int i = 0; i < N_COL; i++ ) {
                var currentArray = new JsonArray();
                for( int j = 0; j < N_ROW; j++ ) {
                    currentArray.add(matrix[j][i].toString());
                }
                result.add("column_" + i, currentArray);
            }
            var s = new JsonObject();
            s.add("shelf", result);
            return s;
        }
    }
    
    /**
     * Protected class that contains the gson custom deserializer logic for Shelf.
     * The valid Json element tree representing the object has the following structure:
     * Each tile is represented as its string representation (see Tile.toString()) {@linkplain Tile}
     * Each column is represented as a json Array in objects each named column_i, where i is the index of the column.
     * The json is expected to have the structure {shelf : {
     * column_0 : [...],
     * column_1 : [...],
     * ...
     * }
     * }
     * The names of the attributes are irrelevant
     */
    protected static class ShelfDeserializer implements JsonDeserializer<Shelf> {
        @Override
        public Shelf deserialize(JsonElement element, Type typeOfDst, JsonDeserializationContext context) throws JsonParseException {
            var result = new Shelf();
            if( element.isJsonObject() ) {
                var columns = element.getAsJsonObject().asMap().values().iterator().next().getAsJsonObject().entrySet();
                int i = 0;
                for( var x : columns ) {
                    if( x.getValue().isJsonArray() ) {
                        var l = x.getValue().getAsJsonArray().asList();
                        var tileList = l.stream().map((k) -> {
                            try {
                                return Tile.fromString(k.getAsJsonPrimitive().getAsString());
                            }
                            catch( InvalidStringException e ) {
                                throw new JsonParseException("Invalid string");
                            }
                        }).filter((k) -> k.type() != Tile.Type.NOTILE).toList();
                        result.addTiles(tileList, i);
                        i++;
                    }else {
                        throw new JsonParseException("Invalid JsonElement : invalid structure");
                    }
                }
            }else {
                throw new JsonParseException("Invalid JsonElement : not an  object");
            }
            return result;
        }
    }
    
    /**
     * Create a Shelf object from a Json string
     *
     * @param json a valid json string
     *
     * @return Shelf
     */
    public static Shelf fromJson(String json) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Shelf.class, new ShelfDeserializer());
        return gson.create().fromJson(json, Shelf.class);
    }
    
    /**
     * Serialize the object to a Json string
     *
     * @return String
     */
    public String toJson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Shelf.class, new ShelfSerializer());
        return gson.create().toJson(this, Shelf.class);
    }
    
    /**
     * Serialize the object to a Json string
     *
     * @return String
     */
    public static String toJson(Shelf shelf) {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Shelf.class, new ShelfSerializer());
        return gson.create().toJson(shelf, Shelf.class);
    }
    
    @Override
    public boolean equals(Object o) {
        if( o instanceof Shelf ) {
            for( int i = 0; i < N_ROW; i++ ) {
                for( int j = 0; j < N_COL; j++ ) {
                    if( !this.getTile(i, j).equals(((Shelf) o).getTile(i, j)) ) {
                        return false;
                    }
                }
            }
            return true;
        }else {
            return false;
        }
    }
}
