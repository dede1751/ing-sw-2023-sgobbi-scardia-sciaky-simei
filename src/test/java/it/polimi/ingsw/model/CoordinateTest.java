package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.rmi.ConnectIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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
    
    @Test
    public void toStringTest() {
        Coordinate coord = new Coordinate(1, 4);
        String s = "(1,4)";
        assertEquals(s, coord.toString());
    }
    
    @Test
    public void sumTest() {
        Coordinate c1 = new Coordinate(2, 3);
        Coordinate c2 = new Coordinate(3, 4);
        Coordinate expected = new Coordinate(5, 7);
        assertEquals(expected, c1.sum(c2));
    }
    
    @Test
    public void subTest() {
        Coordinate c1 = new Coordinate(6, 5);
        Coordinate c2 = new Coordinate(3, 3);
        Coordinate expected = new Coordinate(3, 2);
        assertEquals(expected, c1.sub(c2));
    }
    
    @Test
    public void sumListTest() {
        
        Coordinate toSum = new Coordinate(1, 1);
        
        Coordinate coord1 = new Coordinate(1, 2);
        Coordinate coord2 = new Coordinate(2, 1);
        Coordinate coord3 = new Coordinate(3, 2);
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coord1);
        coordinates.add(coord2);
        coordinates.add(coord3);
    
        Coordinate newCoord1 = new Coordinate(2, 3);
        Coordinate newCoord2 = new Coordinate(3, 2);
        Coordinate newCoord3 = new Coordinate(4, 3);
        List<Coordinate> expected = new ArrayList<>();
        expected.add(newCoord1);
        expected.add(newCoord2);
        expected.add(newCoord3);
        
        assertEquals(expected, toSum.sumList(coordinates));
    }
    
    
    @Test
    public void sumDirTest() {
        
        Coordinate coord = new Coordinate(4, 4);
        
        Coordinate up = new Coordinate(5, 4);
        Coordinate down = new Coordinate(3, 4);
        Coordinate left = new Coordinate(4, 3);
        Coordinate right = new Coordinate(4, 5);
        List<Coordinate> expected = new ArrayList<>();
        expected.add(down);
        expected.add(up);
        expected.add(left);
        expected.add(right);
        
        assertEquals(expected, coord.sumDir());
    }
}
