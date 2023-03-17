package it.polimi.ingsw.model;

public class Coordinate {

    private final int row;

    private final int col;

    protected Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    };

    public int getCol() {
        return this.col;
    };

    public Coordinate getLeft() {
        return new Coordinate(row, col - 1);
    };

    public Coordinate getRight() {
        return new Coordinate(row, col + 1);
    };

    public Coordinate getUp() {
        return new Coordinate(row - 1, col);
    };

    public Coordinate getDown() {
        return new Coordinate(row + 1, col);
    };

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
