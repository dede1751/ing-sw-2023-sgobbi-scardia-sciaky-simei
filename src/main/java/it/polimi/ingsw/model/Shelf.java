package it.polimi.ingsw.model;

import java.util.*;

public class Shelf{

    private final static int N_ROW = 6;
    private final static int N_COL = 5;


    /*
    * In content every list represent a single column
    */
    private final List<Stack<Tile>> content;

    protected Shelf()
    {
        this.content = new ArrayList<Stack<Tile>>();
        for (int i = 0; i < N_COL; i++) {
            this.content.add(i, new Stack<Tile>());
        }
    };

    /*
    * if row or col is out of index the function returns null
    */
    public Tile getTile(int row, int col)
    {
        try {
            return this.content.get(col).get(row);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    };


    /*
    *   Empty position in the shelf are represented with NOTILE
    */
    public Tile[][] getAllShelf()
    {
        Tile[][] result = new Tile[N_ROW][N_COL];
        for (int i = 0; i < N_ROW; i++){
            for(int j = 0; j<N_COL; j++){
                var tile = getTile(j, i);
                if(tile == null){
                    result[i][j] = Tile.NOTILE;
                }
                else {
                    result[i][j] = tile;
                }
                //content is transposed respectively to the traditional matrix, where the [0,0]point
            }
        }
        return result;
    }

    public boolean isFull()
    {
        int result = 0;
        for(List<Tile> x: content){
            result += x.size();
        }
        return result == N_COL+N_ROW;
    };

    /*
    * return the index of all columns that are not full
    */
    public List<Integer> availableColumns(int selectionLength)
    {
        var res = new ArrayList<Integer>();
        for(int i = 0; i < N_COL; i++){
            if(content.get(i).size() < N_ROW-selectionLength) res.add(i);
        }
        return res;
    };

    public int spaceInColumn(int column)
    {
        return N_ROW - content.get(column).size();
    };


    /*
    * return all the indexes of the columns that still have space, with the remaining spaces
    */
    public Map<Integer, Integer> remaingSpace()
    {
        var res = new HashMap<Integer, Integer>();
        for(var x : availableColumns(0)){
            res.put(x, spaceInColumn(x));
        }
        return res;
    }

    /*
    * tiles must not be null, have less or equal than 3 element, be already ordered
    * col must be between 0  and 5
    */
    public void addTiles(List<Tile> tiles, int col)
    {
        for(var x : tiles){
            content.get(col).push(x);
        }
    };

}
