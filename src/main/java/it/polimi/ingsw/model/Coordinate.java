package it.polimi.ingsw.model;

/**
 * Coordinate class to index locations on the board.
 * This class is immutable.
 */
public class Coordinate {

    private final int row;

    private final int col;

    /**
     * Initialize coordinate with given x,y values
     * @param row Row index, top row is 0 and bottom is 8
     * @param col Column index, leftmost column is 0 and rightmost is 8
     */
    protected Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Get coordinate row id
     * @return Row index
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Get coordinate column id
     * @return Column index
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Get the coordinate to the left
     * @return New coordinate to the left of current one
     */
    public Coordinate getLeft() {
        return new Coordinate(row, col - 1);
    }

    /**
     * Get the coordinate to the right
     * @return New coordinate to the right of current one
     */
    public Coordinate getRight() {
        return new Coordinate(row, col + 1);
    }

    /**
     * Get the coordinate above
     * @return New coordinate atop the current one
     */
    public Coordinate getUp() {
        return new Coordinate(row - 1, col);
    }

    /**
     * Get the coordinate below
     * @return New coordinate under the current one
     */
    public Coordinate getDown() {
        return new Coordinate(row + 1, col);
    }

    @Override
    public int hashCode() {
        return this.row * 9 + this.col;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate c)) {
            return false;
        }

        return this.row == c.row && this.col == c.col;
    }
}
