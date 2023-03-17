package it.polimi.ingsw.model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class ShelfTest {

    Shelf shelf;
    @BeforeAll
    public void init_param(){
        shelf = new Shelf();
    }

    @Test
    public void test_insert_and_getShelf(){

        var columns = new ArrayList<List<Tile>>();

        columns.add(0,new ArrayList<Tile>());
        columns.get(0).add(0, Tile.CATS);
        columns.get(0).add(1, Tile.BOOKS);
        columns.get(0).add(2, Tile.PLANTS);
        columns.add(1, new ArrayList<Tile>());
        columns.get(1).add(0, Tile.PLANTS);
        columns.get(1).add(1, Tile.TROPHIES);
        columns.add(2, new ArrayList<Tile>());
        columns.get(2).add(0, Tile.FRAMES);
        columns.get(2).add(1, Tile.TROPHIES);
        columns.get(2).add(2, Tile.TROPHIES);
        columns.get(2).add(3, Tile.TROPHIES);
        columns.get(2).add(4, Tile.TROPHIES);
        columns.get(2).add(5, Tile.TROPHIES);
        columns.add(3, new ArrayList<Tile>());
        columns.get(3).add(0, Tile.PLANTS);

        for (int i = 0; i < columns.size(); i++) {
            shelf.addTiles(columns.get(i), i);
        }

        var s = shelf.getAllShelf();
        for (int i = 0; i < columns.size() ; i++) {
            for (int j = 0; j < columns.get(i).size(); j++) {

            }
        }

        for





    }



}
