package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.LocalModel;

import java.util.Objects;
import java.util.Random;

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
    
    
    static String generateBoard(Board board) {
        StringBuilder sb = new StringBuilder();
        
        String colmns = "   0  1  2  3  4  5  6  7  8 ";
        
        if( board == null ) {
            System.out.println("Board still not initialized!");
        }else {
            sb.append(colmns).append("\n");
            var matrix = board.getAsMatrix();
            for( int i = 0; i < 9; i++ ) {
                sb.append(i).append(" ");
                for( int j = 0; j < 9; j++ ) {
                    if( matrix[i][j] != null ) {
                        var tile = matrix[i][j].toTile();
                        sb.append(tile);
                    }else {
                        sb.append(Tile.NOTILE.toTile());
                    }
                    
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    
    static String generateShelf(Shelf shelf) {
        StringBuilder sb = new StringBuilder();
        
        
        if( shelf == null ) {
            System.out.println("Shelf still not initialized!");
        }else {
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
        }
        
        return sb.toString();
    }
    
    
    static String generateCurrentPlayer(String nickname, Integer points) {
        String top = nickname + ", it's your turn!\n";
        String mid = nickname + "\n";
        String bottom = points + "\n";
        return top + "\n" +
               mid +
               bottom;
    }
    
    
    static String generateCommonGoal(int commonGoal1) {
        StringBuilder sb = new StringBuilder();
        
        switch( commonGoal1 ) {
            
            case 0 -> sb.append("\n\n\n    " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "    \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "    \n" +
                                "    x6     \n");
            
            case 1 -> sb.append("\n\n    " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + " \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + " \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + " \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "  x4\n");
            
            case 2 -> sb.append(
                    ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "     " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET +
                    "\n\n\n\n\n" +
                    ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "     " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET);
            
            case 3 -> sb.append("\n\n\n\n   " + ANSI_WHITE_BACKGROUND + " = = " + ANSI_RESET + "\n   " +
                                ANSI_WHITE_BACKGROUND + " = = " + ANSI_RESET + "x2 \n\n");
            
            case 4 -> sb.append("█\n" +
                                "█\n" +
                                "█   max 3 " + ANSI_WHITE_BACKGROUND + "≠" + ANSI_RESET + "\n" +
                                "█   x3\n" +
                                "█\n" +
                                "█\n");
            
            case 5 -> sb.append(
                    "\n  " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + " " + ANSI_WHITE_BACKGROUND + " = " +
                    ANSI_RESET +
                    " \n\n" +
                    ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + " " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET +
                    " " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET +
                    "\n\n" +
                    ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + " " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET +
                    " " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET);
            
            case 6 -> sb.append(ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "\n" +
                                "  " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "\n" +
                                "    " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "\n" +
                                "      " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "\n" +
                                "        " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "\n ");
            
            case 7 -> sb.append("\n\n\n   █████   \n" +
                                " max 3 " + ANSI_WHITE_BACKGROUND + " ≠ " + ANSI_RESET + "\n" +
                                "   x4  ");
            
            case 8 -> sb.append("    " + ANSI_WHITE_BACKGROUND + " ≠ " + ANSI_RESET + " \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " ≠ " + ANSI_RESET + " \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " ≠ " + ANSI_RESET + " \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " ≠ " + ANSI_RESET + " x2 \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " ≠ " + ANSI_RESET + " \n" +
                                "    " + ANSI_WHITE_BACKGROUND + " ≠ " + ANSI_RESET + " ");
            case 9 -> sb.append("\n\n\n\n" + ANSI_WHITE_BACKGROUND + " ≠ ≠ ≠ ≠ ≠ " + ANSI_RESET + "\n" +
                                "     x2 ");
            
            case 10 -> sb.append(
                    "\n" + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "     " + ANSI_WHITE_BACKGROUND + " = " +
                    ANSI_RESET +
                    "\n\n" +
                    "    " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + " \n\n" +
                    ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET + "     " + ANSI_WHITE_BACKGROUND + " = " + ANSI_RESET);
            
            case 11 -> sb.append("\n █\n" +
                                 " █ █\n" +
                                 " █ █ █\n" +
                                 " █ █ █ █\n" +
                                 " █ █ █ █ █");
            
        }
        return createBox(sb.toString());
        
    }
    
    
    static String generatePersonalScore() {
        StringBuilder sb = new StringBuilder();
        String grid = """
                ───┼───┼───┼───┼───┼───""";
        
        for( int i = 1; i <= 6; i++ ) {
            sb.append(" ").append(i).append(" ");
            if( i != 6 )
                sb.append("│");
        }
        sb.append("\n").append(grid).append("\n");
        for( int i = 1; i <= 6; i++ ) {
            sb.append(" ");
            switch( i ) {
                case 1 -> sb.append(1);
                case 2 -> sb.append(2);
                case 3 -> sb.append(4);
                case 4 -> sb.append(6);
                case 5 -> sb.append(9);
                case 6 -> sb.append(12);
            }
            sb.append(" ");
            if( i != 6 )
                sb.append("│");
        }
        
        return sb.toString();
    }
    
    static String generatePersonalGoal(int personalGoal) {
        StringBuilder sb = new StringBuilder();
        switch( personalGoal ) {
            case 0 -> {
                sb.append(ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET
                          + "    \n" +
                          "        " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          "      " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                          "  " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                          "\n" +
                          "    " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "    ");
            }
            case 1 -> {
                sb.append(
                        "\n" +
                        "  " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                        ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET +
                        "    \n" +
                        "        " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "\n"
                        + "      " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                        "        " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET
                );
            }
            case 2 -> {
                sb.append("\n" +
                          ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "    " + ANSI_YELLOW_BACKGROUND + "  " +
                          ANSI_RESET +
                          "  \n" +
                          "    " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                          "  " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "    " + ANSI_CYAN_BACKGROUND + "  " +
                          ANSI_RESET + "\n" +
                          "\n" +
                          ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "        ");
            }
            case 3 -> {
                sb.append("        " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          "\n" +
                          ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET +
                          "    \n" +
                          "      " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                          "  " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET +
                          "\n     ");
            }
            case 4 -> {
                sb.append("          \n" +
                          "  " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                          "          \n" +
                          "  " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET +
                          "    \n" +
                          "        " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "    " + ANSI_GREEN_BACKGROUND + "  " +
                          ANSI_RESET +
                          "  ");
            }
            case 5 -> {
                sb.append("    " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_GREEN_BACKGROUND + "  " +
                          ANSI_RESET + "\n" +
                          "          \n" +
                          "      " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                          "          \n" +
                          "  " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_BLUE_BACKGROUND + "  " +
                          ANSI_RESET + "  \n" +
                          ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "        \n");
            }
            case 6 -> {
                sb.append(ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET
                          + "        \n" +
                          "      " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                          "  " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                          ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "        \n" +
                          "        " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          "    " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "    ");
            }
            case 7 -> {
                sb.append("        " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          "  " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                          "    " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                          ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "        \n" +
                          "      " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                          "      " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "  ");
            }
            case 8 -> {
                sb.append("    " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                          "          \n" +
                          "    " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                          "        " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          "  " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "    " + ANSI_PURPLE_BACKGROUND + "  " +
                          ANSI_RESET + "\n" +
                          ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "        ");
            }
            case 9 -> {
                sb.append("        " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          "  " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                          ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "        \n" +
                          "      " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                          "  " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                          "      " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "  \n");
            }
            case 10 -> {
                sb.append("    " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                          "  " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                          ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "        \n" +
                          "    " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                          "        " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          "      " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  ");
            }
            case 11 -> {
                sb.append("    " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                          "  " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                          "    " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                          "      " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                          "        " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "\n" +
                          ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "        ");
            }
            
        }
        return createBox(sb.toString());
    }
    
    static void printGame(LocalModel model, String nickname) {
        StringBuilder game = new StringBuilder();
        StringBuilder cg = new StringBuilder();
        
        var cgx = model.getCGXindex();
        var cgy = model.getCGYindex();
        var cgxdesc = model.getCGXdescription();
        var cgydesc = model.getCGXdescription();
        
        cg.append(generateCommonGoal(cgx)).append("   ").append(generateCommonGoal(cgy))
                .append("\n").append(" ");
        for( int i = 0; i <= cgxdesc.length() - 11; i+=11 ) {
            cg.append( cgxdesc.substring(i, i + 10) )
                    .append("\n");
        }
        cg.append("                 ");
        for( int i = 0; i <= cgydesc.length() - 11; i+=11 ) {
            cg.append( cgydesc.substring(i, i + 10) )
                    .append("\n");
        }
        
        for( int i = 0; i < model.getPlayersNicknames().size(); i++ ) {
            if( !Objects.equals(model.getPlayersNicknames().get(i), nickname) )
                TUIUtils.concatString(game.toString(), TUIUtils.generateShelf(
                        model.getShelf(model.getPlayersNicknames().get(i))), 1);
        }
        
        game.append("\n")
                .append(generateBoard(model.getBoard()))
                .append("     ").append(cg.toString())
                .append("\n");
        var players = model.getPlayersNicknames();
        for( int i = 0; i <= players.size(); i++ ) {
            game.append(generateShelf(model.getShelf(players.get(i))))
                    .append("     ") ;
        }
        game.append("\n");
        for( int i = 0; i <= players.size(); i++ ) {
            game.append(players.get(i))
                    .append("     ") ;
        }
        game.append("\n")
                .append(generatePersonalScore()).append("\n")
                        .append(generatePersonalGoal(2));
        
        System.out.println(game.toString());
    }
    
}
