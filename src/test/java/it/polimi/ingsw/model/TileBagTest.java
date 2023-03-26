package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests {@link TileBag}
 */
@Tag("TileBag")
@Tag("Model")
@TestInstance(Lifecycle.PER_METHOD)
public class TileBagTest {
    
    public TileBag bag;
    
    @BeforeEach
    public void init_param() {
        bag = new TileBag();
    }
    
    @Test
    public void getTileAmountTest() {
        for ( Tile.Type type : Tile.Type.values() ) {
            if ( type != Tile.Type.NOTILE ) {
                int count = bag.getTileAmount(new Tile(type, Tile.Sprite.ONE));
                count += bag.getTileAmount(new Tile(type, Tile.Sprite.TWO));
                count += bag.getTileAmount(new Tile(type, Tile.Sprite.THREE));
                
                assertEquals(22, count, () -> "tile :" + type.name());
            }
        }
    }
    
    @Test
    public void removeSelectionTest() {
        List<Tile> selection = List.of(
                new Tile(Tile.Type.PLANTS, Tile.Sprite.ONE),
                new Tile(Tile.Type.CATS, Tile.Sprite.ONE),
                new Tile(Tile.Type.GAMES, Tile.Sprite.ONE));
        bag.removeSelection(selection);
        
        for ( Tile tile: selection ) {
            assertEquals(7, bag.getTileAmount(tile), () -> "tile : " + tile.type().name());
        }
    }
    
}
