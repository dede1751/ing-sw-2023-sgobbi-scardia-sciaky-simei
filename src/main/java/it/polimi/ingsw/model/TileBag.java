package it.polimi.ingsw.model;

import java.util.*;

public class TileBag {
    private Map<Tile,Integer> bag;

    protected TileBag(){
        this.bag = new HashMap<Tile, Integer>();
        this.bag.put(Tile.CATS, 22);
        this.bag.put(Tile.BOOKS, 22);
        this.bag.put(Tile.GAMES, 22);
        this.bag.put(Tile.FRAMES, 22);
        this.bag.put(Tile.TROPHIES, 22);
        this.bag.put(Tile.PLANTS, 22);
    }

    /**
     *
     * @param tile the type of the tiles
     * @return number of tiles of type tile currently in the bag and in the board
     */
    int getTileAmount(Tile tile){
        return this.bag.get(tile);
    }
    /**
     * @param selection selection of tiles to remove. Every tile mustn't be NOTILE and the caller
     *                  must ensure that there are enough tile in the bag
     */
    void removeSelection(List<Tile> selection){
        for(Tile tile: Tile.values()) {
            int newValue = this.bag.get(tile) - Collections.frequency(selection, tile);
            this.bag.replace(tile, newValue);
        }
    }
}
