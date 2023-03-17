package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShelfTest {

    Shelf shelf;
    @BeforeAll
    public void init_param(){
        shelf = new Shelf();
    }

    @Test
    public void test_insert_and_getShelf(){

        var columns = new ArrayList<List<Tile>>();

        columns.add(0,new ArrayList<>());
        columns.get(0).add(0, Tile.CATS);
        columns.get(0).add(1, Tile.BOOKS);
        columns.get(0).add(2, Tile.PLANTS);
        columns.add(1, new ArrayList<>());
        columns.get(1).add(0, Tile.PLANTS);
        columns.get(1).add(1, Tile.TROPHIES);
        columns.add(2, new ArrayList<>());
        columns.get(2).add(0, Tile.FRAMES);
        columns.get(2).add(1, Tile.TROPHIES);
        columns.get(2).add(2, Tile.TROPHIES);
        columns.get(2).add(3, Tile.TROPHIES);
        columns.get(2).add(4, Tile.TROPHIES);
        columns.get(2).add(5, Tile.TROPHIES);
        columns.add(3, new ArrayList<>());
        columns.get(3).add(0, Tile.PLANTS);

        for (int i = 0; i < columns.size(); i++) {
            shelf.addTiles(columns.get(i), i);
        }
        var s = shelf.getAllShelf();
        for (int i = 0; i < columns.size() ; i++) {
            for (int j = 0; j < columns.get(i).size(); j++) {
                assertEquals(columns.get(i).get(j), s[j][i]);
                assertEquals(columns.get(i).get(j), shelf.getTile(j, i));
                assertEquals(s[j][i], shelf.getTile(j, i));
            }
            for(int j = columns.get(i).size(); j < Shelf.N_ROW; j++) {
                assertEquals(s[j][i], Tile.NOTILE);
            }
        }
        for (int i = columns.size(); i < Shelf.N_COL; i++){
            for (int j = 0; j < Shelf.N_ROW; j++){
                assertEquals(s[j][i], Tile.NOTILE);
                assertNull(shelf.getTile(j, i));
            }
        }

    }

}
