package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.Shelf;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.goals.common.CommonGoal;
import it.polimi.ingsw.model.goals.common.CrossGoal;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests {@link CommonGoal}
 */

public class CommonGoalTest {
    
    @Test
    public void CrossGoalTest() {
        var shelf = new Shelf();
        shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES, Tile.CATS), 0);
        shelf.addTiles(List.of(Tile.PLANTS, Tile.CATS, Tile.PLANTS), 0);
        shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES, Tile.CATS), 0);
        var goal = new CrossGoal();
        assertTrue(goal.checkShelf(shelf));
    }
}

