package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.List;

/**
 * Coordinate class to index locations on the board. <br>
 * Bottom row is 0 and top is 8. <br>
 * Leftmost col is 0 and rightmost is 8.
 *
 * @param row coordinate's row
 * @param col coordinate's column
 */
public record Coordinate(int row, int col) implements Serializable {
    
    /**
     * Get coordinate row id
     *
     * @return Row index
     */
    @Override
    public int row() {
        return this.row;
    }
    
    /**
     * Get coordinate column id
     *
     * @return Column index
     */
    @Override
    public int col() {
        return this.col;
    }
    
    /**
     * Get the coordinate to the left
     *
     * @return New coordinate to the left of current one
     */
    public Coordinate getLeft() {
        return new Coordinate(row, col - 1);
    }
    
    /**
     * Get the coordinate to the right
     *
     * @return New coordinate to the right of current one
     */
    public Coordinate getRight() {
        return new Coordinate(row, col + 1);
    }
    
    /**
     * Get the coordinate above
     *
     * @return New coordinate atop the current one
     */
    public Coordinate getUp() {
        return new Coordinate(row + 1, col);
    }
    
    /**
     * Get the coordinate below
     *
     * @return New coordinate under the current one
     */
    public Coordinate getDown() {
        return new Coordinate(row - 1, col);
    }
    
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
    
    /**
     * Get the sum of the current coordinate with the given offset
     *
     * @param offset Offset to be summed
     * @return The sum of this and offset
     */
    public Coordinate sum(Coordinate offset) {
        return new Coordinate(row + offset.row, col + offset.col);
    }
    
    /**
     * Subtract the offset to the current object and return the result of the operation as a new Coordinate
     *
     * @param offset The offset to be subtracted
     * @return This - offset
     */
    public Coordinate sub(Coordinate offset) {
        return new Coordinate(row - offset.row, col - offset.col);
    }
    
    /**
     * Sum the current coordinate to a list of offsets. <br>
     * This is used to producee more complex structures represented as lists of coordinates.
     *
     * @param offset The list of offset to be summed
     * @return the list of coordinates obtained by applying all the offsets to the current coordinate
     */
    public List<Coordinate> sumList(List<Coordinate> offset) {
        return offset.stream().map((x) -> x.sum(this)).toList();
    }
    
    /**
     * Return the list of all the adjacent coordinates
     *
     * @return The List of Coordinate of all the adiajent coordinate
     */
    public List<Coordinate> adjacent() {
        return this.sumList(List.of(new Coordinate(-1, 0),
                                    new Coordinate(1, 0),
                                    new Coordinate(0, -1),
                                    new Coordinate(0, 1)));
    }
}
