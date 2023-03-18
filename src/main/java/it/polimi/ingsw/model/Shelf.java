package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

/**
 * Shelf representation within the model
 */
public class Shelf{

    protected final static int N_ROW = 6;
    protected final static int N_COL = 5;

    /**
     * Shelf represented as a list of stacks, where each stack represents a column.
     * Row: 0 is the bottom row, 5 is the top row
     * Col: 0 is the leftmost column, 4 is the rightmost
     */
    private final List<Stack<Tile>> content;

    protected Shelf() {
        this.content = new ArrayList<>();

        for (int i = 0; i < N_COL; i++) {
            this.content.add(i, new Stack<>());
        }
    }

    /**
     * Get tile at given x, y coordinates
     * @param row Row of the desired tile
     * @param col Colum of the desired tile
     * @return    Tile in the position [row, col], null if the coordinates are out of bounds
     *
     */
    public Tile getTile(int row, int col) {
        try {
            return this.content.get(col).get(row);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Get a matrix representation of the shelf
     * @return The shelf as a matrix of Tile, empty tiles are represented as Tile.NOTILE
     */
    public Tile[][] getAllShelf() {
        Tile[][] result = new Tile[N_ROW][N_COL];

        for (int i = 0; i < N_ROW; i++){
            for(int j = 0; j<N_COL; j++){
                var tile = getTile(i, j);
                result[i][j] = Objects.requireNonNullElse(tile, Tile.NOTILE);
            }
        }
        return result;
    }

    /**
     * Checks whether the shelf is full
     * @return True if every box of the shelf is occupied (not Tile.NOTILE)
     */
    public boolean isFull() {
        return content.stream().allMatch(x -> x.size()== N_ROW);
    }

    /**
     * Get the list of indices for the columns which can still be filled.
     * @param selectionLength Length of the selection, it's supposed to be correct and between 1 and 3
     * @return                Index of all columns that have enough space for the selection
     */
    public List<Integer> availableColumns(int selectionLength) {
        var res = new ArrayList<Integer>();

        for(int i = 0; i < N_COL; i++){
            if(content.get(i).size() <= (N_ROW-selectionLength))
                res.add(i);
        }
        return res;
    }

    /**
     * Get the amount of space left in the given column
     * @param column Integer index of column to check
     * @return       Number of non-occupied boxes in the column
     */
    public int spaceInColumn(int column)
    {
        return N_ROW - content.get(column).size();
    }

    /**
     * Get mapping of column indices to amount of remaining spaces.
     * @return All the indices of the columns that still have space, with the remaining space
     */
    public Map<Integer, Integer> remainingSpace() {
        var res = new HashMap<Integer, Integer>();
        for(var x : availableColumns(0)){
            res.put(x, spaceInColumn(x));
        }
        return res;
    }

    /**
     * Add ordered list of tiles to given column
     * @param tiles Not null, have less or equal than 3 element, be already ordered, NOTILEs will be ignored
     * @param col   Column index, between 0 and 5
     */
    public void addTiles(List<Tile> tiles, int col) {
        for(var x : tiles){
            if (x != Tile.NOTILE) content.get(col).push(x);
        }
    }

}
