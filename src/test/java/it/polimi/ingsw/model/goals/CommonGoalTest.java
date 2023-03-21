package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.Shelf;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.goals.common.CommonGoal;
import it.polimi.ingsw.model.goals.common.CrossGoal;
import it.polimi.ingsw.model.goals.common.DecreasingColumnsGoal;
import it.polimi.ingsw.model.goals.common.DiagonalFiveTileGoal;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests {@link CommonGoal}
 */

public class CommonGoalTest {
    
    @Test
    public void CrossGoalTrue() {
        var shelf = new Shelf();
        shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES, Tile.CATS), 0);
        shelf.addTiles(List.of(Tile.PLANTS, Tile.CATS, Tile.PLANTS), 1);
        shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES, Tile.CATS), 2);
        var goal = new CrossGoal();
        assertTrue(goal.checkShelf(shelf));
    }
    
    @Test
    public void CrossGoalFalse() {
        var shelf = new Shelf();
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 0);
        shelf.addTiles(List.of(Tile.PLANTS, Tile.CATS, Tile.PLANTS), 1);
        shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES, Tile.CATS), 2);
        var goal = new CrossGoal();
        assertFalse(goal.checkShelf(shelf));
    }
    
    @Test
    public void DecreasingColumnTrue() {
        var shelf = new Shelf();
        shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS, Tile.CATS, Tile.CATS), 0);
        shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS, Tile.CATS), 1);
        shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS), 2);
        shelf.addTiles(List.of(Tile.CATS, Tile.CATS), 3);
        shelf.addTiles(List.of(Tile.CATS), 4);
        var goal = new DecreasingColumnsGoal();
        assertTrue(goal.checkShelf(shelf));
    }
    
    
    // Test failed
    @Test
    public void DecreasingColumnFalse() {
        var shelf = new Shelf();
        shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS, Tile.CATS), 0);
        shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS, Tile.CATS), 1);
        shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS), 2);
        shelf.addTiles(List.of(Tile.CATS, Tile.CATS), 3);
        shelf.addTiles(List.of(Tile.CATS), 4);
        var goal = new DecreasingColumnsGoal();
        assertFalse(goal.checkShelf(shelf));
    }
    
    @Test
    public void DiagonalFiveTileTrue() {
        var shelf = new Shelf();
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 0);
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 1);
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 2);
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 3);
        shelf.addTiles(List.of(Tile.CATS), 4);
        var goal = new DiagonalFiveTileGoal();
        assertTrue(goal.checkShelf(shelf));
    }
    
    @Test
    public void DiagonalFiveTileFalse() {
        var shelf = new Shelf();
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 0);
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 1);
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 2);
        shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 3);
        shelf.addTiles(List.of(Tile.CATS), 4);
        var goal = new DiagonalFiveTileGoal();
        assertFalse(goal.checkShelf(shelf));
    }
}

