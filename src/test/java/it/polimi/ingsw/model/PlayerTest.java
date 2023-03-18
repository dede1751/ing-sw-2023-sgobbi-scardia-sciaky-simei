package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * this class tests {@link Player}
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
        int scoreToAdd = 10;
        p.addScore(scoreToAdd);
        assertEquals(10, p.getScore());
    }
    
    @Test
    public void getShelfTest() {
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                assertNull(p.getShelf().getTile(i, j));
            }
        }
        p.getShelf().addTiles(List.of(Tile.CATS, Tile.TROPHIES), 1);
        assertEquals(Tile.CATS, p.getShelf().getTile(0, 1));
        assertEquals(Tile.TROPHIES, p.getShelf().getTile(1, 1));
    }
    
    @Test
    public void addScoreTest() {
        Player player = new Player("Lucrezia", 1);
        int startingScore = player.getScore();
        int scoreToAdd = 10;
        int expectedScore = startingScore + scoreToAdd;
        assertEquals(expectedScore, player.addScore(scoreToAdd));
    }
}