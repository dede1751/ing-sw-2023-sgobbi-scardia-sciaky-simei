package it.polimi.ingsw.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Tag("TileBag")
@Tag("Model")
@TestInstance(Lifecycle.PER_METHOD)
public class TileBagTest {

    public TileBag bag;

    @BeforeEach
    public void init_param(){
        bag = new TileBag();
    }

    @Test
    public void test_getTileAmount(){
        for(var tile : Tile.values()){
            if(tile != Tile.NOTILE) assertEquals(22, bag.getTileAmount(tile), ()-> "tile :" + tile.name());
        }
    }
    @Test
    public void test_removeSelection(){
        var sel = List.of(Tile.PLANTS, Tile.CATS, Tile.GAMES);
        bag.removeSelection(sel);
        for (var tile: sel) {
            assertEquals(21, bag.getTileAmount(tile), () -> "tile : " + tile.name());
        }
    }


}
