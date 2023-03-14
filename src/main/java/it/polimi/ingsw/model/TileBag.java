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

    int getTileAmount(Tile tile){
        return this.bag.get(tile);
    }

    void removeSelection(List<Tile> selection){
        for(Tile tile: Tile.values()) {
            int newValue = this.bag.get(tile) - Collections.frequency(selection, tile);
            this.bag.put(tile, newValue);
        }
    }
}
