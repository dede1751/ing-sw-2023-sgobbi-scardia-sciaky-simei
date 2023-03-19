package it.polimi.ingsw.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileBag {
    private final Map<Tile, Integer> bag;
    
    protected TileBag() {
        this.bag = new HashMap<>();
        this.bag.put(Tile.CATS, 22);
        this.bag.put(Tile.BOOKS, 22);
        this.bag.put(Tile.GAMES, 22);
        this.bag.put(Tile.FRAMES, 22);
        this.bag.put(Tile.TROPHIES, 22);
        this.bag.put(Tile.PLANTS, 22);
    }
    
    /**
     * @param tile the type of the tiles
     *
     * @return number of tiles of type tile currently in the bag and in the board.
     * If tile=NOTILE returns 0
     */
    int getTileAmount(Tile tile) {
        if( tile != Tile.NOTILE )
            return this.bag.get(tile);
        else
            return 0;
    }
    
    /**
     * @param selection selection of tiles to remove. the caller
     *                  must ensure that there are enough tile in the bag
     *                  and NOTILEs are ignored
     */
    void removeSelection(List<Tile> selection) {
        for( Tile tile : Tile.values() ) {
            if( tile != Tile.NOTILE ) {
                int newValue = this.bag.get(tile) - Collections.frequency(selection, tile);
                this.bag.replace(tile, newValue);
            }
        }
    }
}
