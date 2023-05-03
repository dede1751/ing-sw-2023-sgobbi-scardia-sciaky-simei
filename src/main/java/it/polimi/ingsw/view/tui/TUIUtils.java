package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Shelf;

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
    
    public String concatString(String s1, String s2, int space) {
        String[] s1Lines = s1.split("\n");
        String[] s2Lines = s2.split("\n");
        StringBuilder sb = new StringBuilder();
        String[] shortS = s1Lines.length > s2Lines.length ? s2Lines : s1Lines;
        String[] longS = s1Lines.length < s2Lines.length ? s2Lines : s1Lines;
        
        
        for( int i = 0; i < shortS.length; i++ ) {
            sb.append(s1Lines[i]);
            for( int j = 0; j < space; j++ ) {
                sb.append(" ");
            }
            sb.append(s2Lines[i]).append("\n");
        }
        
        for( int i = shortS.length; i < longS.length; i++ ) {
            if( s1Lines == longS ) {
                sb.append(s1Lines[i]).append("\n");
            }else {
                for( int j = 0; j < s1Lines[0].length() + space; j++ ) {
                    sb.append(" ");
                }
                sb.append(s2Lines[i]).append("\n");
            }
        }
        
        
        return sb.toString();
    }
    
    
    private void printBoard(Board board) {
        var def = "C-,-)";
        if( board == null ) {
            System.out.println("Board Still not initialized!");
            for( int i = 8; i >= 0; i-- ) {
                for( int j = 0; j < 9; j++ ) {
                    System.out.print(def + ",");
                }
                System.out.print("\n");
            }
        }else {
            var matrix = board.getAsMatrix();
            for( int i = 8; i >= 0; i-- ) {
                for( int j = 0; j < 9; j++ ) {
                    if( matrix[i][j] == null ) {
                        System.out.print(def + ",");
                    }else {
                        System.out.print(matrix[i][j].toString() + ",");
                    }
                }
                System.out.print("\n");
            }
        }
    }
    
    //TODO
    private void printShelf(Shelf shelf) {
    
    }
}
