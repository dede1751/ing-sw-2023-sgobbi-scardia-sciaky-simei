package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void addCommonGoalScoreTest() {
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
    
    @Test
    public void getCommonGoalScoreTest() {
        int startingScore = p.getScore();
        assertEquals(0, startingScore);
        
        assertEquals(0, p.getCommonGoalScore());
    }
    
    @Test
    public void personalGoalScoreTest() {
        int pgScore = 10;
        int startingScore = p.getScore();
        assertEquals(0, startingScore);
        
        int expectedScore = startingScore + pgScore;
        p.setPersonalGoalScore(10);
        assertEquals(expectedScore, p.getScore());
        assertEquals(pgScore, p.getPersonalGoalScore());
    }
    
    @Test
    public void adjacentScoreTest() {
        int adjacentScore = 10;
        int startingScore = p.getScore();
        assertEquals(0, startingScore);
        
        int expectedScore = startingScore + adjacentScore;
        p.setAdjacencyScore(10);
        assertEquals(expectedScore, p.getScore());
        assertEquals(adjacentScore, p.getAdjacencyScore());
    }
    
    @Test
    public void bonusScoreTest() {
        int bonusScore = 10;
        int startingScore = p.getScore();
        assertEquals(0, startingScore);
        
        int expectedScore = startingScore + bonusScore;
        p.setBonusScore(10);
        assertEquals(expectedScore, p.getScore());
        assertEquals(bonusScore, p.getBonusScore());
    }
    
    @Test
    public void completedGoalTest() {
        assertFalse(p.isCompletedGoalX());
        assertFalse(p.isCompletedGoalY());
        
        p.setCompletedGoalX(true);
        p.setCompletedGoalY(true);
        assertTrue(p.isCompletedGoalX());
        assertTrue(p.isCompletedGoalY());
        
        p.setCompletedGoalX(false);
        p.setCompletedGoalY(false);
        assertFalse(p.isCompletedGoalX());
        assertFalse(p.isCompletedGoalY());
    }
    
}