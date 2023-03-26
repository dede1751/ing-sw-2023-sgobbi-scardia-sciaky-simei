package it.polimi.ingsw.model;

/**
 * Coordinate class to index locations on the board.
 * Bottom row is 0 and top is 8.
 * Leftmost col is 0 and rightmost is 8.
 * This class is immutable.
 */
public record Coordinate(int row, int col) {
    
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
    
}
