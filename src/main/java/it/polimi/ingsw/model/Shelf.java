package it.polimi.ingsw.model;

import java.util.*;

public class Shelf{

    private final static int N_ROW = 6;
    private final static int N_COL = 5;

    private Tile[][] contents;

    protected Shelf() {
        this.contents = new Tile[N_ROW][N_COL];
    };
    public Tile getTile(int row, int col){};

    public boolean isFull(){};

    public List<Integer> availableColumns(int selectionLength){};

    public int spaceInColumn(int column){};

    public void addTiles(List<Tile> tiles, int col){};

}
