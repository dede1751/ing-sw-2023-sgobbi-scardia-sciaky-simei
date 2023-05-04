package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;

public class TUIUtils {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    
    
    public enum Tiles {
        catTile {
            public String toString() {
                return ANSI_GREEN_BACKGROUND + ANSI_BLACK + " C " + ANSI_RESET;
            }
        },
        bookTile {
            public String toString() {
                return ANSI_WHITE_BACKGROUND + ANSI_BLACK + " B " + ANSI_RESET;
            }
        },
        gameTile {
            public String toString() {
                return ANSI_YELLOW_BACKGROUND + ANSI_BLACK + " G " + ANSI_RESET;
            }
        },
        frameTile {
            public String toString() {
                return ANSI_BLUE_BACKGROUND + ANSI_BLACK + " F " + ANSI_RESET;
            }
        },
        trophyTile {
            public String toString() {
                return ANSI_CYAN_BACKGROUND + ANSI_BLACK + " T " + ANSI_RESET;
            }
        },
        plantTile {
            public String toString() {
                return ANSI_PURPLE_BACKGROUND + ANSI_BLACK + " P " + ANSI_RESET;
            }
        }
    }
    
    
    public static String concatString(String s1, String s2, int space) {
        String[] s1Lines = s1.split("\n");
        String[] s2Lines = s2.split("\n");
        StringBuilder sb = new StringBuilder();
        String[] shortS = s1Lines.length > s2Lines.length ? s2Lines : s1Lines;
        String[] longS = s1Lines.length < s2Lines.length ? s2Lines : s1Lines;
        
        for( int i = 0; i < shortS.length; i++ ) {
            sb.append(s1Lines[i]);
            sb.append(" ".repeat(Math.max(0, space)));
            sb.append(s2Lines[i]).append("\n");
        }
        
        for( int i = shortS.length; i < longS.length; i++ ) {
            if( s1Lines == longS ) {
                sb.append(s1Lines[i]).append("\n");
            }else {
                sb.append(" ".repeat(Math.max(0, s1Lines[0].length() + space)));
                sb.append(s2Lines[i]).append("\n");
            }
        }
        
        return sb.toString();
    }
    
    
    static void printBoard(Board board) {
        if( board == null ) {
            System.out.println("Board Still not initialized!");
        } else {
            var matrix = board.getAsMatrix();
            for( int i = 0; i < 9; i++ ) {
                for( int j = 0; j < 9; j++ ) {
                    if( matrix[i][j] != null ) {
                        var tile = matrix[i][j].type();
                        switch (tile) {
                            case CATS:
                                System.out.print(Tiles.catTile);
                            case BOOKS:
                                System.out.print(Tiles.bookTile);
                            case GAMES:
                                System.out.print(Tiles.gameTile);
                            case FRAMES:
                                System.out.print(Tiles.frameTile);
                            case TROPHIES:
                                System.out.print(Tiles.trophyTile);
                            case PLANTS:
                                System.out.print(Tiles.plantTile);
                        }
                    }
                }
                System.out.print("\n");
            }
        }
    }
    
    
    static String printShelf(Shelf shelf) {
        StringBuilder sb = new StringBuilder();
        //5*6
        String top = "┌───┬───┬───┬───┬───┐\n";
        String bottom = "└───┴───┴───┴───┴───┘\n";
        
        sb.append(top);
        
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
    
            sb.append(("│" + Tiles.catTile).repeat(Shelf.N_COL));
            sb.append("│\n");
            sb.append("├───┼───┼───┼───┼───┤\n");
        }
        sb.append("│" + Tiles.catTile + "│" + Tiles.catTile + "│" + Tiles.catTile + "│" + Tiles.catTile + "│" +
                  Tiles.catTile + "│  \n");
        sb.append(bottom);
        return sb.toString();
    }
}
