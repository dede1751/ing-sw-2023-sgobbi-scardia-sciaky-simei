package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests {@link Player}
 */
@Tag("Player")
@Tag("Model")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PlayerTest {
    
    public Player p;
    
    @BeforeEach
    public void setUp() {
        p = new Player("Lucrezia", 1);
    }
    
    @Test
    public void getNicknameAndPgTest() {
        assertEquals(p.getNickname(), "Lucrezia");
        assertEquals(p.getPg(), 1);
    }
    
    @Test
    public void getScoreTest() {
        int startingScore = p.getScore();
        assertEquals(0, startingScore);
        
        p.addCommonGoalScore(10);
        assertEquals(startingScore + 10, p.getScore());
        assertEquals(startingScore + 20, p.addCommonGoalScore(10));
    }
    
    @Test
    public void getShelfTest() {
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                assertEquals(p.getShelf().getTile(i, j), Tile.NOTILE);
            }
        }
        
        p.getShelf().addTiles(
                List.of(new Tile(Tile.Type.CATS, Tile.Sprite.ONE), new Tile(Tile.Type.TROPHIES, Tile.Sprite.ONE)),
                1);
        
        assertEquals(Tile.Type.CATS, p.getShelf().getTile(0, 1).type());
        assertEquals(Tile.Type.TROPHIES, p.getShelf().getTile(1, 1).type());
    }
    
}