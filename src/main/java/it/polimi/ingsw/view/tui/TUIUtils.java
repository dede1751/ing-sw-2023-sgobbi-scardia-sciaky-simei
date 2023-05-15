package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.view.LocalModel;

import java.util.ArrayList;
import java.util.List;

public class TUIUtils {
    
    private static final LocalModel model = LocalModel.getInstance();
    
    public static final String ANSI_RESET = "\u001B[0m";
    
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_YELLOW_BOLD = "\033[1;33m";
    public static final String ANSI_BROWN_BOLD = "\033[1;38;5;130m";
    
    // TODO titolo figo
    public static void printStartGame() {
        System.out.println("THE GAME HAS STARTED");
    }
    
    public static void printGame(String nickname) {
        StringBuilder sb = new StringBuilder();
        StringBuilder playersb = new StringBuilder();
        
        
        String boardCg = concatString(
                generateBoard(),
                "\n\n" + generateCommonGoal(model.getCGXindex()),
                3
        );
        
        boardCg = concatString(
                boardCg,
                "\n\n" + generateCommonGoal(model.getCGYindex()),
                1
        );
        
        String player = concatString(
                generateShelf(nickname),
                playersb.append("\n\n")
                        .append(generatePersonalGoal(model.getPgid()))
                        .append(generatePersonalScore()).toString(),
                2);
        
        sb.append(generateOtherShelves(nickname))
                .append("\n\n")
                .append(boardCg)
                .append("\n\n\n")
                .append(player)
                .append("\n\n");
        
        System.out.println(sb);
    }
    
    public static void printSelection(List<Coordinate> selection) {
        StringBuilder sb = new StringBuilder();
        
        List<Tile> tiles = selection.stream()
                .map(c -> model.getBoard().getTile(c))
                .toList();
        
        for ( int i = 0; i < tiles.size(); i++ ) {
            sb.append(i + 1)
                    .append(": ")
                    .append(tiles.get(i).toTile())
                    .append(" ");
        }
        
        System.out.print(sb);
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
    
    
    private static String generateBoard() {
        Board board = model.getBoard();
        StringBuilder sb = new StringBuilder();
        
        String columns = "   0  1  2  3  4  5  6  7  8 \n";
        
        sb.append(columns);
        Tile[][] matrix = board != null ? board.getAsMatrix() : new Tile[9][9];
        
        for( int i = 8; i >= 0; i-- ) {
            sb.append(i).append(" ");
            
            for( int j = 0; j < 9; j++ ) {
                if( matrix[i][j] != null ) {
                    String tile = matrix[i][j].toTile();
                    sb.append(tile);
                }else {
                    sb.append(Tile.NOTILE.toTile());
                }
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    // TODO use method to color string? better coloring
    private static String generateShelf(String nickname) {
        Shelf shelf = model.getShelf(nickname);
        StringBuilder sb = new StringBuilder();
        String name = String.format(" * %-18.18s \n", nickname);
        if ( nickname.equals(model.getCurrentPlayer()) ) {
            name = ANSI_YELLOW_BOLD + name + ANSI_RESET;
        }
        String top = ANSI_BROWN_BOLD + "┌───┬───┬───┬───┬───┐" + ANSI_RESET;
        String bot = ANSI_BROWN_BOLD + "└───┴───┴───┴───┴───┘" + ANSI_RESET;
        
        if( shelf != null ) {
            var matrix = shelf.getAllShelf();
            
            sb.append(name).append(top).append("\n");
            
            for( int i = Shelf.N_ROW - 1; i >= 0; i-- ) {
                for( int j = 0; j < Shelf.N_COL; j++ ) {
                    var tile = matrix[i][j].toTile();
                    sb.append(ANSI_BROWN_BOLD + "│" + ANSI_RESET).append(tile);
                }
                sb.append(ANSI_BROWN_BOLD + "│" + ANSI_RESET).append("\n");
                if( i != 0 ) {
                    sb.append(ANSI_BROWN_BOLD + "├───┼───┼───┼───┼───┤" + ANSI_RESET);
                    sb.append("\n");
                }
                else {
                    sb.append(bot);
                }
            }
        }else {
            return "no shelf received";
        }
        
        
        return sb.toString();
    }
    
    /**
     * Generate the shelves for all players except the given nickname
     *
     * @param nickname nickname to exclude
     *
     * @return string containing all shelves
     */
    private static String generateOtherShelves(String nickname) {
        String s = "";
        
        for( String n : model.getPlayersNicknames() ) {
            if( !n.equals(nickname) ) {
                s = concatString(s, generateShelf(n), 1);
            }
        }
        
        return s;
    }
    
    private static String generateCommonGoal(int commonGoal1) {
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
    
    
    private static String generatePersonalScore() {
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
    
    private static String generatePersonalGoal(int personalGoal) {
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
    
}
