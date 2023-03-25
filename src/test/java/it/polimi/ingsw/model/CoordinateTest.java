package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests {@link Coordinate}
 */
@Tag("Coordinate")
@Tag("Model")
public class CoordinateTest {
    
    private static Coordinate c;
    
    @BeforeAll
    public static void setUp() {
        c = new Coordinate(6, 0);
    }
    
    @Test
    public void getterSetter() {
        assertEquals(c.row(), 6);
        assertEquals(c.col(), 0);
    }
    
    @Test
    public void leftRight() {
        Coordinate left = c.getLeft();
        Coordinate right = left.getRight();
        
        assertEquals(c, right);
    }
    
    @Test
    public void upDown() {
        Coordinate up = c.getUp();
        Coordinate down = up.getDown();
        
        assertEquals(c, down);
    }
    
}
