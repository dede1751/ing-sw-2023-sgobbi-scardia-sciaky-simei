package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.Tile.Sprite;
import it.polimi.ingsw.model.Tile.Type;
import it.polimi.ingsw.model.goals.common.*;
import it.polimi.ingsw.utils.files.ResourcesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests {@link CommonGoal}
 */
@Tag("Model")
@Tag("CommonGoal")
public class CommonGoalTest {
    
    Shelf shelf;
    
    @BeforeEach
    public void initShelf() {
        this.shelf = new Shelf();
    }
    
    private String getResource(String className) {
        String result = null;
        try {
            result = Files.readString(
                    Path.of(ResourcesManager.testRootDir,
                            "it/polimi/ingsw/model/goals/resources/" + className + ".json"),
                    StandardCharsets.UTF_8);
        }
        catch( IOException e ) {
            e.printStackTrace(System.err);
        }
        return result;
    }
    
    @Tag("CrossGoal")
    @Nested
    class CrossGoalTest {
        @Test
        public void CrossGoalTrue() {
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 2);
            var goal = new CrossGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void CrossGoalFalse() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 2);
            var goal = new CrossGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Tag("DecreasingColumnGoalTest")
    @Nested
    class DecreasingColumnGoalTest {
        
        @Test
        public void DecreasingColumnTrue() {
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new DecreasingColumnsGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void DecreasingColumnFalse() {
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new DecreasingColumnsGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Tag("DiagonalFiveTileGoal")
    @Nested
    class DiagonalFiveTileGoalTest {
        @Test
        public void DiagonalFiveTileTrue() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new DiagonalFiveTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void DiagonalFiveTileFalse() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new DiagonalFiveTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Tag("EightUniqueGoal")
    @Nested
    class EightUniqueGoalTest {
        @Test
        public void EightUniqueGoalTrue() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 4);
            var goal = new EightUniqueGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void EightUniqueGoalFalse() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE)), 4);
            var goal = new EightUniqueGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Tag("FourCornersGoal")
    @Nested
    class FourCornersGoalTest {
        @Test
        public void FourCornersTrue() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)),
                           4);
            var goal = new FourCornersGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void FourCornersFalse() {
            var shelf = new Shelf();
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE)), 4);
            var goal = new FourCornersGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Tag("SixGroupTwoTileGoal")
    @Nested
    class SixGroupTwoTileGoalTest {
        
        @Test
        void SixGroupTwoTileGoalTrue1() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new SixGroupTwoTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        void SixGroupTwoTileGoalFalse1() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new SixGroupTwoTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
        @Test
        void SixGroupTwoTileGoalFalse2() {
            
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE)), 4);
            var goal = new SixGroupTwoTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
        @Test
        void SixGroupTwoTileGoalFalse3() {
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE),
                                   new Tile(Type.FRAMES, Sprite.TWO), new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new SixGroupTwoTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
        
    }
    
    @Tag("FourGroupFourTileGoal")
    @Nested
    class FourGroupFourTileGoalTest {
        @Test
        void FourGroupFourTileGoalFalse1() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new FourGroupFourTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
        @Test
        void FourGroupFourTileGoalTrue1() {
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new FourGroupFourTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        void FourGroupFourTileGoalFalse2() {
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.GAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE)), 4);
            var goal = new FourGroupFourTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
        
    }
    
    @Tag("FourRowFiveTileGoal")
    @Nested
    class FourRowFiveTileGoalTest {
        @Test
        public void FourRowFiveTileTrue() {
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 4);
            var goal = new FourRowFiveTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void FourRowFiveTileFalse() {
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 4);
            var goal = new FourRowFiveTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
    }
    
    @Tag("ThreeColumnSixTileGoal")
    @Nested
    class ThreeColumnSixTileGoalTest {
        @Test
        public void ThreeColumnSixTileTrue() {
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 4);
            var goal = new ThreeColumnSixTileGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void ThreeColumnSixTileFalse() {
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.PLANTS, Sprite.ONE)), 4);
            var goal = new ThreeColumnSixTileGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Tag("TwoColumnDistinctGoal")
    @Nested
    class TwoColumnDistinctGoalTest {
        @Test
        public void TwoColumnDistinctTrue() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE)), 3);
            var goal = new TwoColumnDistinctGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void TwoColumnDistinctFalse() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE)), 1);
            var goal = new TwoColumnDistinctGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Tag("TwoRowDistinctGoal")
    @Nested
    class TwoRowDistinctGoalTest {
        @Test
        public void TwoRowDistinctTrue() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.CATS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.CATS, Sprite.ONE)), 3);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new TwoRowDistinctGoal();
            assertTrue(goal.checkShelf(shelf));
        }
        
        @Test
        public void TwoRowDistinctFalse() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.TROPHIES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 4);
            var goal = new TwoRowDistinctGoal();
            assertFalse(goal.checkShelf(shelf));
        }
    }
    
    @Tag("TwoGroupSquareGoal")
    @Nested
    class TwoGroupSquareGoalTest {
        @Test
        public void TwoGroupSquareFalse() {
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 0);
            shelf.addTiles(List.of(new Tile(Type.FRAMES, Sprite.ONE), new Tile(Type.FRAMES, Sprite.ONE),
                                   new Tile(Type.TROPHIES, Sprite.ONE)), 1);
            shelf.addTiles(List.of(new Tile(Type.PLANTS, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.GAMES, Sprite.ONE)), 2);
            shelf.addTiles(List.of(new Tile(Type.GAMES, Sprite.ONE), new Tile(Type.BOOKS, Sprite.ONE),
                                   new Tile(Type.BOOKS, Sprite.ONE), new Tile(Type.CATS, Sprite.ONE)), 3);
            
            var goal = new TwoGroupSquareGoal();
            assertFalse(goal.checkShelf(shelf));
        }
        
        @Test
        public void TwoGroupSquareTrue1() {
            var attribute = ResourcesManager.getCurrentMethod();
            assertDoesNotThrow(() -> {
                var file = getResource(this.getClass().getSimpleName());
                var json = ResourcesManager.JsonManager.getObjectByAttribute(file, attribute);
                
                var goal = new TwoGroupSquareGoal();
                assertTrue(goal.checkShelf(Shelf.fromJson(json)));
            });
        }
    }
}

