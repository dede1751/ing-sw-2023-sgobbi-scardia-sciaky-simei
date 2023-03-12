package it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.List;

public class TileBag {
    private HashMap<Tile,Integer> bag;

    protected TileBag(HashMap<Tile,Integer> bag){
        this.bag=bag;
    }

    int getTileAmount(Tile tile){
        return bag.get(tile);
    }

    void removeSelection(List<Tile> selection){



    }
}
