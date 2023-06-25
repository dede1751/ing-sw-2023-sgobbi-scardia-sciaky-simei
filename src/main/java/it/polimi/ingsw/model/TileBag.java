package it.polimi.ingsw.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.utils.exceptions.InvalidStringException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Model's internal representation of the tile's bag
 */
public class TileBag {
    
    private final Map<Tile, Integer> bag;
    
    public TileBag() {
        this.bag = new HashMap<>();
        
        for( Tile tile : Tile.ALL_TILES ) {
            if( tile.sprite() == Tile.Sprite.ONE ) {
                this.bag.put(tile, 8);
            }else {
                this.bag.put(tile, 7);
            }
        }
    }
    
    private TileBag(Map<Tile, Integer> tileBag) {
        bag = tileBag;
    }
    
    /**
     * Get the number of tiles of the given type in play.
     *
     * @param tile The type of tile (both kind and sprite value) to count occurrences of.
     *
     * @return Number of tiles of type tile currently in the bag and in the board, if NOTILE 0
     */
    public int getTileAmount(Tile tile) {
        if( tile != Tile.NOTILE ) {
            return this.bag.get(tile);
        }else {
            return 0;
        }
    }
    
    /**
     * Remove selected list of tiles from play.
     * Selection should not contain NOTILEs, and selection should be limited to bag contents
     *
     * @param selection List of tiles to remove from the bag.
     */
    public void removeSelection(List<Tile> selection) {
        for( Tile tile : selection ) {
            this.bag.replace(tile, this.bag.get(tile) - 1);
        }
    }
    
    /**
     * Get a copy of the bag
     *
     * @return Map linking each tile to its amount
     */
    public Map<Tile, Integer> getAllBag() {
        return new HashMap<>(this.bag);
    }
    
    /**
     * Get the number of tiles in the bag and on the board.
     *
     * @return Total current number of tiles in play
     */
    public int currentTileNumber() {
        return this.bag.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
    
    
    /**
     * TileBag's custom Gson Deserializer class. <br>
     * Serialization is left to the standard gson's Map serialization. <br>
     * An example of a valid serialized json class is thus provided : <br>
     * <code>
     * "TileBag": { <br>
     * "(C,3)": 7, "(C,1)": 8, "(B,3)": 7, <br>
     * "(F,3)": 7, "(B,1)": 8, "(G,3)": 7, <br>
     * "(F,1)": 8, "(G,2)": 7, "(P,3)": 7, <br>
     * "(P,1)": 8, "(F,2)": 7, "(P,2)": 7, <br>
     * "(T,2)": 7, "(C,2)": 7, "(T,1)": 8, <br>
     * "(T,3)": 7, "(B,2)": 7, "(G,1)": 8  <br>
     * } <br>
     * </code>
     */
    public static class TileBagDeserializer implements JsonDeserializer<TileBag> {
        
        @Override
        public TileBag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            var result = new HashMap<Tile, Integer>();
            if( json.isJsonObject() ) {
                for( var x : json.getAsJsonObject().entrySet() ) {
                    try {
                        result.put(Tile.fromString(x.getKey()), x.getValue().getAsInt());
                    }
                    catch( InvalidStringException e ) {
                        throw new JsonParseException("Invalid Json : Invalid TileBag Section");
                    }
                }
            }
            return new TileBag(result);
        }
    }
}
