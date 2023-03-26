package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.Tile.*;
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
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 2);
            var goal = new CrossGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void CrossGoalFalse() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 2);
            var goal = new CrossGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class DecreasingColumnGoalTest {
        
        @Test
        public void DecreasingColumnTrue() {
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new DecreasingColumnsGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void DecreasingColumnFalse() {
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new DecreasingColumnsGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class DiagonalFiveTileGoalTest {
        @Test
        public void DiagonalFiveTileTrue() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new DiagonalFiveTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void DiagonalFiveTileFalse() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new DiagonalFiveTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class EightUniqueGoalTest {
        @Test
        public void EightUniqueGoalTrue() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 4);
            var goal = new EightUniqueGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void EightUniqueGoalFalse() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 4);
            var goal = new EightUniqueGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class FourCornersGoalTest {
        @Test
        public void FourCornersTrue() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)),
                           4);
            var goal = new FourCornersGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void FourCornersFalse() {
            var shelf = new Shelf();
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 4);
            var goal = new FourCornersGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class SixGroupTwoTileGoalTest {
        
        @Test
        void SixGroupTwoTileGoalTrue1() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new SixGroupTwoTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        void SixGroupTwoTileGoalFalse1() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new SixGroupTwoTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
        @Test
        void SixGroupTwoTileGoalFalse2() {
            
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 4);
            var goal = new SixGroupTwoTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
    }
    
    @Nested
    class FourGroupFourTileGoalTest {
        @Test
        void FourGroupFourTileGoalTrue1() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new FourGroupFourTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        void FourGroupFourTileGoalTrue2() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new FourGroupFourTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        void FourGroupFourTileGoalFalse() {
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new FourGroupFourTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
        
    }
    
    @Nested
    class FourRowFiveTileGoalTest {
        @Test
        public void FourRowFiveTileTrue() {
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 4);
            var goal = new FourRowFiveTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void FourRowFiveTileFalse() {
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 4);
            var goal = new FourRowFiveTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
    }
    
    @Nested
    class ThreeColumnSixTileGoalTest {
        @Test
        public void ThreeColumnSixTileTrue() {
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 4);
            var goal = new ThreeColumnSixTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void ThreeColumnSixTileFalse() {
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 4);
            var goal = new ThreeColumnSixTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class TwoColumnDistinctGoalTest {
        @Test
        public void TwoColumnDistinctTrue() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 3);
            var goal = new TwoColumnDistinctGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void TwoColumnDistinctFalse() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 1);
            var goal = new TwoColumnDistinctGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class TwoRowDistinctGoalTest {
        @Test
        public void TwoRowDistinctTrue() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new TwoRowDistinctGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void TwoRowDistinctFalse() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new TwoRowDistinctGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Nested
    class TwoGroupSquareGoalTest {
        @Test
        public void TwoGroupSquareTrue() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            
            var goal = new TwoGroupSquareGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
    }
}

