package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.goals.common.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests {@link CommonGoal}
 */

public class CommonGoalTest {
    
    Shelf shelf;
    
    @BeforeEach
    public void initShelf() {
        this.shelf = new Shelf();
    }
    
    @Nested
    class CrossGoalTest {
        @Test
        public void CrossGoalTrue() {
            shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.CATS, Tile.PLANTS), 1);
            shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES, Tile.CATS), 2);
            var goal = new CrossGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void CrossGoalFalse() {
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.CATS, Tile.PLANTS), 1);
            shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES, Tile.CATS), 2);
            var goal = new CrossGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class DecreasingColumnGoalTest {
        
        @Test
        public void DecreasingColumnTrue() {
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
            shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS, Tile.CATS), 1);
            shelf.addTiles(List.of(Tile.CATS, Tile.CATS, Tile.CATS), 2);
            shelf.addTiles(List.of(Tile.CATS, Tile.CATS), 3);
            shelf.addTiles(List.of(Tile.CATS), 4);
            var goal = new DecreasingColumnsGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class DiagonalFiveTileGoalTest {
        @Test
        public void DiagonalFiveTileTrue() {
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
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 1);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS), 2);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 3);
            shelf.addTiles(List.of(Tile.CATS), 4);
            var goal = new DiagonalFiveTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class EightUniqueGoalTest {
        @Test
        public void EightUniqueGoalTrue() {
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.CATS), 2);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 3);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS, Tile.TROPHIES), 4);
            var goal = new EightUniqueGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void EightUniqueGoalFalse() {
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.CATS), 2);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 3);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS, Tile.TROPHIES), 4);
            var goal = new EightUniqueGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class FourCornersGoalTest {
        @Test
        public void FourCornersTrue() {
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS, Tile.CATS, Tile.CATS, Tile.CATS, Tile.TROPHIES), 0);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.CATS), 2);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 3);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS, Tile.PLANTS, Tile.PLANTS, Tile.TROPHIES),
                           4);
            var goal = new FourCornersGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void FourCornersFalse() {
            var shelf = new Shelf();
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS, Tile.CATS, Tile.CATS, Tile.CATS, Tile.TROPHIES), 0);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.CATS), 2);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.CATS), 3);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.PLANTS, Tile.PLANTS, Tile.PLANTS, Tile.PLANTS), 4);
            var goal = new FourCornersGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class SixGroupTwoTileGoalTest {
        @Test
        void SixGroupTwoTileGoalTrue() {
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS, Tile.BOOKS), 0);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS, Tile.TROPHIES), 2);
            shelf.addTiles(List.of(Tile.BOOKS, Tile.BOOKS), 3);
            shelf.addTiles(List.of(Tile.GAMES), 4);
            var goal = new SixGroupTwoTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        void SixGroupTwoTileGoalFalse() {
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS, Tile.BOOKS), 0);
            shelf.addTiles(List.of(Tile.CATS, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS, Tile.TROPHIES), 2);
            shelf.addTiles(List.of(Tile.BOOKS, Tile.BOOKS), 3);
            shelf.addTiles(List.of(Tile.GAMES), 4);
            var goal = new SixGroupTwoTileGoal();
            assertFalse(goal.checkShelf(shelf));
            
        }
        
    }
    
    @Nested
    class FourGroupFourTileGoalTest {
        @Test
        void FourGroupFourTileGoalTrue() {
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.BOOKS, Tile.GAMES), 0);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.TROPHIES, Tile.GAMES), 1);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.TROPHIES, Tile.CATS, Tile.TROPHIES, Tile.GAMES), 2);
            shelf.addTiles(List.of(Tile.BOOKS, Tile.BOOKS, Tile.BOOKS, Tile.BOOKS, Tile.GAMES), 3);
            shelf.addTiles(List.of(Tile.GAMES), 4);
            var goal = new SixGroupTwoTileGoal();
            assertTrue(goal.checkShelf(shelf));
            
        }
    }
    
    @Nested
    class FourRowFiveTileGoalTest {
        @Test
        public void FourRowFiveTileTrue() {
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.PLANTS, Tile.TROPHIES, Tile.FRAMES, Tile.CATS), 1);
            shelf.addTiles(List.of(Tile.CATS, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES), 2);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES), 3);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES), 4);
            var goal = new FourRowFiveTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void FourRowFiveTileFalse() {
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.FRAMES, Tile.PLANTS, Tile.TROPHIES, Tile.FRAMES, Tile.CATS), 1);
            shelf.addTiles(List.of(Tile.CATS, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES), 2);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES), 3);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES), 4);
            var goal = new FourRowFiveTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
    }
    
    //Test failed
    @Nested
    class ThreeColumnSixTileGoalTest {
        @Test
        public void ThreeColumnSixTileTrue() {
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.FRAMES, Tile.PLANTS, Tile.TROPHIES, Tile.FRAMES, Tile.FRAMES, Tile.PLANTS), 1);
            shelf.addTiles(List.of(Tile.CATS, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES), 2);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES, Tile.BOOKS, Tile.BOOKS), 3);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.TROPHIES, Tile.BOOKS, Tile.PLANTS, Tile.PLANTS), 4);
            var goal = new ThreeColumnSixTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void ThreeColumnSixTileFalse() {
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.TROPHIES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.FRAMES, Tile.PLANTS, Tile.TROPHIES, Tile.BOOKS, Tile.FRAMES, Tile.PLANTS), 1);
            shelf.addTiles(List.of(Tile.TROPHIES, Tile.BOOKS, Tile.TROPHIES, Tile.FRAMES, Tile.BOOKS, Tile.BOOKS), 3);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.TROPHIES, Tile.BOOKS, Tile.PLANTS, Tile.PLANTS), 4);
            var goal = new ThreeColumnSixTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class TwoColumnDistinctGoalTest {
        @Test
        public void TwoColumnDistinctTrue() {
            shelf.addTiles(List.of(Tile.FRAMES, Tile.BOOKS, Tile.TROPHIES, Tile.CATS, Tile.PLANTS, Tile.GAMES), 0);
            shelf.addTiles(List.of(Tile.FRAMES, Tile.BOOKS), 1);
            shelf.addTiles(List.of(Tile.CATS, Tile.BOOKS, Tile.TROPHIES, Tile.GAMES, Tile.PLANTS, Tile.FRAMES), 3);
            var goal = new TwoColumnDistinctGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void TwoColumnDistinctFalse() {
            shelf.addTiles(List.of(Tile.FRAMES, Tile.BOOKS, Tile.TROPHIES, Tile.CATS, Tile.PLANTS, Tile.GAMES), 0);
            shelf.addTiles(List.of(Tile.FRAMES, Tile.BOOKS), 1);
            var goal = new TwoColumnDistinctGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class TwoRowDistinctGoalTest {
        @Test
        public void TwoRowDistinctTrue() {
            shelf.addTiles(List.of(Tile.FRAMES, Tile.BOOKS, Tile.FRAMES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.BOOKS, Tile.TROPHIES, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.BOOKS, Tile.GAMES), 2);
            shelf.addTiles(List.of(Tile.CATS, Tile.BOOKS, Tile.CATS), 3);
            shelf.addTiles(List.of(Tile.GAMES, Tile.BOOKS, Tile.PLANTS, Tile.CATS), 4);
            var goal = new TwoRowDistinctGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void TwoRowDistinctFalse() {
            shelf.addTiles(List.of(Tile.FRAMES, Tile.BOOKS, Tile.FRAMES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.BOOKS, Tile.TROPHIES, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.BOOKS, Tile.GAMES), 2);
            shelf.addTiles(List.of(Tile.GAMES, Tile.BOOKS, Tile.PLANTS, Tile.CATS), 4);
            var goal = new TwoRowDistinctGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class TwoGroupSquareGoalTest {
        @Test
        public void TwoGroupSquareTrue() {
            shelf.addTiles(List.of(Tile.FRAMES, Tile.FRAMES, Tile.FRAMES, Tile.CATS), 0);
            shelf.addTiles(List.of(Tile.FRAMES, Tile.FRAMES, Tile.TROPHIES), 1);
            shelf.addTiles(List.of(Tile.PLANTS, Tile.BOOKS, Tile.BOOKS, Tile.GAMES), 2);
            shelf.addTiles(List.of(Tile.GAMES, Tile.BOOKS, Tile.BOOKS, Tile.CATS), 3);
            
            var goal = new TwoGroupSquareGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
    }
}

