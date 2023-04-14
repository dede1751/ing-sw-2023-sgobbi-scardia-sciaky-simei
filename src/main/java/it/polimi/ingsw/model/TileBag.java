package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TileBag {
    
    private final Map<Tile, Integer> bag;
    
    protected TileBag() {
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
    
    public static class TileBagDeserializer implements JsonDeserializer<TileBag> {
        
        @Override
        public TileBag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().create();
            var token = new TypeToken<Map<Tile, Integer>>() {
            }.getType();
            var hashMap = (Map<Tile, Integer>) gson.fromJson(json, token);
            return new TileBag(hashMap);
        }
    }
}
