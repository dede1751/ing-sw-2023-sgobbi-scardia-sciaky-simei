package it.polimi.ingsw.model;

public class Coordinate {

    private int row;

    private int col;

    protected Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() {};

    public int getCol() {};

    public Coordinate getLeft() {};

    public Coordinate getRight() {};

    public Coordinate getUp() {};

    public Coordinate getDown() {};
}
