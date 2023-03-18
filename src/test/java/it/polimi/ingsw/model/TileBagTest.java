package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Tag("TileBag")
@Tag("Model")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TileBagTest {

    private TileBag bag;

    @BeforeAll
    public void init_param(){
        bag = new TileBag();
    }

    @Test
    public void test_getTileAmount(){
        for(var tile : Tile.values()){
            assertEquals(22, bag.getTileAmount(tile));
        }
    }

    @Test
    public void test_removeSelection(){
        var sel = List.of(Tile.PLANTS, Tile.CATS, Tile.GAMES);
        bag.removeSelection(sel);
        for (var tile: sel) {
            assertEquals(21, bag.getTileAmount(tile));
        }
    }


}
