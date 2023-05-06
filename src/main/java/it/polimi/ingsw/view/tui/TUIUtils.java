package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.goals.common.*;
import it.polimi.ingsw.utils.exceptions.InvalidStringException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.view.LocalModel;

public class TUIUtils {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    //public static final String ANSI_WHITE_BACKGROUND = "\u001B[41m";
    
    
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
    
    
    public static String createBox(String input) {
        StringBuilder output = new StringBuilder();
        String[] lines = input.split("\n");
        String[] linesNoColor = input.split("\n");
        int maxLength = 0;
        
        // Iterate over each line to calculate the max length
        for( String line : linesNoColor ) {
            // Remove ANSI color codes before measuring length
            String plainLine = line.replaceAll("\u001B\\[[;\\d]*m", "");
            maxLength = Math.max(maxLength, plainLine.length());
        }
        
        int width = maxLength; // Add 2 for each side of the box
        String top = "┌" + "─".repeat(width + 2) + "┐\n";
        String bottom = "└" + "─".repeat(width + 2) + "┘\n";
        
        output.append(top);
        
        for( String line : lines ) {
            output.append("│ ");
            // Replace ANSI color codes with empty strings before padding
            String plainLine = line.replaceAll("\u001B\\[[;\\d]*m", "");
            output.append(line);
            output.append(" ".repeat(maxLength - plainLine.length()));
            output.append(" │\n");
        }
        
        output.append(bottom);
        return output.toString();
    }
    
    
    static String printBoard(Board board) {
        StringBuilder sb = new StringBuilder();
        
        if( board == null ) {
            System.out.println("Board Still not initialized!");
        }else {
            var matrix = board.getAsMatrix();
            for( int i = 0; i < 9; i++ ) {
                for( int j = 0; j < 9; j++ ) {
                    var tile = matrix[i][j].toTile();
                    sb.append(tile);
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    
    static String printShelf(Shelf shelf) {
        StringBuilder sb = new StringBuilder();
        //5*6
        String top = "┌───┬───┬───┬───┬───┐\n";
        String bottom = "└───┴───┴───┴───┴───┘\n";
        
        sb.append(top);
        var matrix = shelf.getAllShelf();
        for( int i = Shelf.N_ROW; i >= 0; i-- ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                var tile = matrix[i][j].toTile();
                sb.append("│").append(tile);
                sb.append("│\n");
                if( i != Shelf.N_ROW )
                    sb.append("├───┼───┼───┼───┼───┤\n");
                else
                    sb.append(bottom);
            }
        }
        return sb.toString();
    }
    
    
    static String printCurrentPlayer(String nickname, Integer points) {
        String top = nickname + ", it's your turn!\n";
        String mid = nickname + "\n";
        String bottom = points + "\n";
        return top +
               mid +
               bottom;
    }
    
    
    static String printCommonGoal(int commonGoal1, int commonGoal2) {
        StringBuilder sb = new StringBuilder();
        switch( commonGoal1 ) {
            case 0 -> sb.append(" " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                "x6\n");
            
            case 1 -> sb.append(ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + " x4\n");
            
            case 2 -> sb.append(
                    ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "    " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET +
                    "\n\n\n\n" +
                    ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "    " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET);
            
            case 3 -> sb.append(ANSI_WHITE_BACKGROUND + "==" + ANSI_RESET + "\n" +
                                ANSI_WHITE_BACKGROUND + "==" + ANSI_RESET + " x2");
            
            case 4 -> sb.append("█\n" +
                                "█\n" +
                                "█   max 3 " + ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + "\n" +
                                "█   x3\n" +
                                "█\n" +
                                "█\n");
            
            case 5 -> sb.append(
                    " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET +
                    " \n" +
                    ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET +
                    " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET +
                    "\n" +
                    ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET +
                    " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET);
            
            case 6 -> sb.append(ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                "  " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                "   " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                                "    " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n");
            
            case 7 -> sb.append("█████  \n" +
                                "  max 3 " + ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + "\n" +
                                "    X4");
            
            case 8 -> sb.append(ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + "\n" +
                                ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + "\n" +
                                ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + "\n" +
                                ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + " x2\n" +
                                ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + "\n" +
                                ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + "");
            
            case 9 -> sb.append(ANSI_WHITE_BACKGROUND + "≠≠≠≠≠" + ANSI_RESET + "\n" +
                                "  x2 ");
            
            case 10 -> sb.append(
                    ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + "\n" +
                    " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + " \n" +
                    ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET + " " + ANSI_WHITE_BACKGROUND + "=" + ANSI_RESET);
            
            case 11 -> sb.append("""
                                         █
                                         ██
                                         ███
                                         ████
                                         █████""");
        }
        
        
        return createBox(sb.toString());
 
    }
    
}
