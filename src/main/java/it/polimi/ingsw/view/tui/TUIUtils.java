package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.messages.EndGameMessage;
import it.polimi.ingsw.view.LocalModel;

import java.util.Iterator;
import java.util.List;

/**
 * Utility class for TUI, responsible for pretty-printing the game state. <br>
 * Each time we print the board state, we expect to receive a prompt to display for the user input and a possibly null
 * error message. <br>
 * The TUI is made up of various elements, each a String separated by newlines. They can be joined vertically and
 * horizontally to compose the entire menu.
 */
public class TUIUtils {
    
    /**
     * Private unused constructor to appease Javadoc.
     */
    private TUIUtils() {
    }
    
    private static final LocalModel model = LocalModel.getInstance();
    
    private static final String ANSI_RESET = "\u001B[0m";
    
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    
    
    private static final String ANSI_GOLD_BACKGROUND = "\033[38;5;16;48;5;228m";
    private static final String ANSI_SILVER_BACKGROUND = "\033[38;5;16;48;5;253m";
    private static final String ANSI_BRONZE_BACKGROUND = "\033[38;5;16;48;5;166m";
    private static final String ANSI_DARK_BRONZE_BACKGROUND = "\033[38;5;16;48;5;94m";
    
    private static final String ANSI_YELLOW_BOLD = "\033[1;33m";
    private static final String ANSI_BROWN_BOLD = "\033[1;38;5;130m";
    private static final String ANSI_DARK_BROWN_BOLD = "\033[1;38;5;94m";
    private static final String ANSI_RED_BOLD = "\033[1;31m";
    
    
    private static final String TITLE = "\n" + createBox(
            ANSI_YELLOW_BOLD + "$$\\      $$\\            $$$$$$\\  $$\\                 $$\\  $$$$$$\\  $$\\" +
            ANSI_RESET + "\n" +
            ANSI_YELLOW_BOLD + "$$$\\    $$$ |          $$  __$$\\ $$ |                $$ |$$  __$$\\ \\__|" +
            ANSI_RESET + "\n" +
            ANSI_YELLOW_BOLD +
            "$$$$\\  $$$$ |$$\\   $$\\ $$ /  \\__|$$$$$$$\\   $$$$$$\\  $$ |$$ /  \\__|$$\\  $$$$$$\\" + ANSI_RESET +
            "\n" +
            ANSI_YELLOW_BOLD +
            "$$\\$$\\$$ $$ |$$ |  $$ |\\$$$$$$\\  $$  __$$\\ $$  __$$\\ $$ |$$$$\\     $$ |$$  __$$\\" + ANSI_RESET +
            "\n" +
            ANSI_YELLOW_BOLD + "$$ \\$$$  $$ |$$ |  $$ | \\____$$\\ $$ |  $$ |$$$$$$$$ |$$ |$$  _|    $$ |$$$$$$$$ |" +
            ANSI_RESET + "\n" +
            ANSI_YELLOW_BOLD + "$$ |\\$  /$$ |$$ |  $$ |$$\\   $$ |$$ |  $$ |$$   ____|$$ |$$ |      $$ |$$   ____|" +
            ANSI_RESET + "\n" +
            ANSI_YELLOW_BOLD +
            "$$ | \\_/ $$ |\\$$$$$$$ |\\$$$$$$  |$$ |  $$ |\\$$$$$$$\\ $$ |$$ |      $$ |\\$$$$$$$\\" + ANSI_RESET +
            "\n" +
            ANSI_YELLOW_BOLD +
            "\\__|     \\__| \\____$$ | \\______/ \\__|  \\__| \\_______|\\__|\\__|      \\__| \\_______|" +
            ANSI_RESET + "\n" +
            ANSI_YELLOW_BOLD + "             $$\\   $$ |" + ANSI_RESET + "\n" +
            ANSI_YELLOW_BOLD + "             \\$$$$$$  |" + ANSI_RESET + "\n" +
            ANSI_YELLOW_BOLD + "              \\______/" + ANSI_RESET,
            ANSI_DARK_BROWN_BOLD);
    
    
    /**
     * Clear the console, by either calling the cls executable on Windows or by using ANSI escape codes on Unix. <br>
     * Note that this is not necessarily supported on all systems, and may not work depending on your terminal emulator.
     */
    public static void clearConsole() {
        try {
            if( System.getProperty("os.name").contains("Windows") ) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }else {
                System.out.print("\033\143");
            }
        }
        catch( Exception ignored ) {
        }
    }
    
    /**
     * Print the login screen.
     *
     * @param prompt The prompt to display
     * @param error  The error message to display, or null if there is no error
     */
    public static void printLoginScreen(String prompt, String error) {
        clearConsole();
        System.out.println(TITLE + "\n");
        if( error != null ) {
            System.out.println(ANSI_RED_BOLD + error + ANSI_RESET);
        }
        System.out.println(prompt);
        System.out.print(">> ");
    }
    
    /**
     * Print the main game state.
     *
     * @param nickname The nickname of the player playing the game.
     * @param prompt   The prompt to display
     * @param error    The error message to display, or null if there is no error
     */
    public static void printGame(String nickname, String prompt, String error) {
        StringBuilder sb = new StringBuilder(TITLE);
        StringBuilder playersb = new StringBuilder();
        
        String boardCg = concatString(
                generateBoard(),
                "\n\n" + generateCommonGoal(model.getCGXindex(), model.getTopCGXscore()),
                3
        );
        boardCg = concatString(
                boardCg,
                "\n\n" + generateCommonGoal(model.getCGYindex(), model.getTopCGYscore()),
                1
        );
        
        String player = concatString(
                generateShelf(nickname),
                playersb.append("\n\n")
                        .append(generatePersonalGoal(model.getPgid()))
                        .append(generatePersonalScore(nickname)).toString(),
                2);
        
        sb.append(generateOtherShelves(nickname))
                .append("\n")
                .append(boardCg)
                .append("\n")
                .append(player)
                .append("\n");
        
        String tui = concatString(createBox(sb.toString(), ANSI_YELLOW_BOLD), generateChat(), 4);
        
        sb = new StringBuilder(tui);
        if( error != null ) {
            sb.append("\n")
                    .append(ANSI_RED_BOLD)
                    .append(error)
                    .append(ANSI_RESET);
        }
        sb.append("\n")
                .append(prompt)
                .append("\n>> ");
        
        clearConsole();
        System.out.print(sb);
    }
    
    /**
     * Print the end game leaderboard.
     *
     * @param endgame     The contents of the {@link EndGameMessage} received from the server.
     * @param firstPlayer The nickname of the first player in the game. (The one who started the game)
     */
    public static void printEndGameScreen(EndGameMessage.EndGamePayload endgame, String firstPlayer) {
        
        List<String> color = List.of(ANSI_GOLD_BACKGROUND, ANSI_SILVER_BACKGROUND, ANSI_BRONZE_BACKGROUND,
                                     ANSI_DARK_BRONZE_BACKGROUND);
        
        StringBuilder sb = new StringBuilder(TITLE + "\n");
        sb.append("CONGRATULATION, THE GAME ENDED!\n")
                .append("THE WINNER IS : ")
                .append(ANSI_YELLOW_BOLD)
                .append(endgame.winner())
                .append(ANSI_RESET)
                .append("!\n")
                .append("LEADERBOARD : \n");
        
        Iterator<String> iter = color.iterator();
        for( var x : endgame.points().entrySet() ) {
            sb.append(iter.next());
            String name = String.format(" * %-14.14s", x.getKey());
            sb.append("\t").append(name).append(" : ").append(x.getValue());
            sb.append(ANSI_RESET);
            if( x.getKey().equals(firstPlayer) )
                sb.append(" ⑁");
            sb.append("\n");
        }
        
        clearConsole();
        System.out.print(sb);
    }
    
    /**
     * Join two TUI elements horizontally
     *
     * @param s1    The first element
     * @param s2    The second element
     * @param space The number of spaces between the two elements
     *
     * @return The two elements joined horizontally
     */
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
    
    /**
     * Create a colored box around the input string.
     *
     * @param input The string to put in the box
     * @param color The color of the box
     *
     * @return The input string surrounded by a box
     */
    public static String createBox(String input, String color) {
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
        String top = color + "┌" + "─".repeat(width + 2) + "┐" + ANSI_RESET + "\n";
        String bottom = color + "└" + "─".repeat(width + 2) + "┘" + ANSI_RESET + "\n";
        
        output.append(top);
        
        for( String line : lines ) {
            output.append(color).append("│ ").append(ANSI_RESET);
            // Replace ANSI color codes with empty strings before padding
            String plainLine = line.replaceAll("\u001B\\[[;\\d]*m", "");
            output.append(line);
            output.append(" ".repeat(maxLength - plainLine.length()));
            output.append(color).append(" │").append(ANSI_RESET).append("\n");
        }
        
        output.append(bottom);
        return output.toString();
    }
    
    /**
     * Create a colored box around the input string.
     *
     * @param input The string to put in the box
     * @param color The color of the box
     * @param space Number of spaces between the strinf input
     *
     * @return The input string surrounded by a box
     */
    public static String createBox(String input, String color, int space) {
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
        String top = color + "┌" + "─".repeat(width + 2) + "┐" + ANSI_RESET + " ".repeat(space) + "\n";
        String bottom = color + "└" + "─".repeat(width + 2) + "┘" + ANSI_RESET + " ".repeat(space)+"\n" ;
        
        output.append(top);
        
        for( String line : lines ) {
            output.append(color).append("│ ").append(ANSI_RESET);
            // Replace ANSI color codes with empty strings before padding
            String plainLine = line.replaceAll("\u001B\\[[;\\d]*m", "");
            output.append(line);
            output.append(" ".repeat(maxLength - plainLine.length()));
            output.append(color).append(" │").append(ANSI_RESET).append( " ".repeat(space)).append("\n");
        }
        
        output.append(bottom);
        return output.toString();
    }
    
    
    
    /**
     * Generate the TUI representation of a list of tiles. <br>
     * Used to display tiles once they have been selected.
     *
     * @param tiles The list of tiles to represent
     *
     * @return The TUI representation of the tiles
     */
    public static String generateTiles(List<Tile> tiles) {
        StringBuilder sb = new StringBuilder();
        
        for( int i = 0; i < tiles.size(); i++ ) {
            sb.append(i + 1)
                    .append(": ")
                    .append(tiles.get(i).toTile())
                    .append(" ");
        }
        return sb.toString();
    }
    
    /**
     * Weapper for generateTiles(), mapping coordinates to tiles using the board.
     *
     * @param selection The list of coordinates to represent
     *
     * @return The TUI representation of the tiles
     */
    public static String generateSelection(List<Coordinate> selection) {
        List<Tile> tiles = selection.stream()
                .map(c -> model.getBoard().getTile(c))
                .toList();
        
        return generateTiles(tiles);
    }
    
    /**
     * Generate the TUI representation of the chat. <br>
     * Chat is displayed on the right side of the screen in a separate box.
     *
     * @return The TUI representation of the chat
     */
    public static String generateChat() {
        StringBuilder sb = new StringBuilder();
        
        List<String> chat = model.getChat();
        List<String> latestChat = chat.subList(chat.size() - Math.min(chat.size(), 55), chat.size());
        if (latestChat.size() == 55) {
            latestChat.set(0, " ... ");
        }

        for( String s : latestChat ) {
            sb.append(s).append("\n");
        }
        
        if( latestChat.size() > 0 ) {
            return createBox(sb.toString(), ANSI_YELLOW_BOLD);
        }else {
            return " ";
        }
    }
    
    /**
     * Generate the TUI representation of the board.
     *
     * @return The TUI representation of the board
     */
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
    
    /**
     * Generate the TUI representation of a player's shelf. <br>
     * Also includes the player's name and score. Current player is printed in bright yello.
     *
     * @param nickname The nickname of the player whose shelf to represent
     *
     * @return The TUI representation of the shelf
     */
    private static String generateShelf(String nickname) {
        Shelf shelf = model.getShelf(nickname);
        StringBuilder sb = new StringBuilder();
        String name = String.format(" * %-14.14s", nickname);
        int score = model.getPoints(nickname);
        
        if( nickname.equals(model.getCurrentPlayer()) ) {
            name = ANSI_YELLOW_BOLD + name + ANSI_RESET;
        }
        name = name + "(" + score + ")";
        name += "\n";
        
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
                }else {
                    sb.append(bot);
                }
            }
        }else {
            return "no shelf received";
        }
        
        
        return sb.toString();
    }
    
    /**
     * Generate the shelves for all players except the given nickname. <br>
     * Used to display shelves other than the one of the person playing.
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
    
    /**
     * Generate the TUI representation of the common goals.
     *
     * @param commonGoal The index of the common goal to print
     *
     * @param score  The current score of the top stack of the common goal
     *
     * @return The TUI representation of the common goal
     */
    public static String generateCommonGoal(int commonGoal, int score) {
        StringBuilder sb = new StringBuilder();
    
        switch( commonGoal ) {
        
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
        
            case 11 -> sb.append("""
                                          
                                          █
                                          █ █
                                          █ █ █
                                          █ █ █ █
                                          █ █ █ █ █\
                                         """);
        
        }
        return createBox(" "+ANSI_RED_BOLD+String.valueOf(score)+ ANSI_RESET+" ", ANSI_RED_BOLD,9)+ createBox(sb.toString(), ANSI_DARK_BROWN_BOLD);
    
    }
    
    /**
     * Generate the TUI representation of the personal goals. <br>
     * This includes the personal goal "shelf" and a scoreboard, with the current score highlighted.
     *
     * @param nickname The nickname of the player
     *
     * @return The TUI representation of the personal goals
     */
    private static String generatePersonalScore(String nickname) {
        StringBuilder sb = new StringBuilder();
        String grid = "\n" + """
                ───┼───┼───┼───┼───┼───
                """;
        
        int pgScore = model.getPgScore(nickname);
        
        sb.append(" 1 │ 2 │ 3 │ 4 │ 5 │ 6 ")
                .append(grid);
        
        switch( pgScore ) {
            case 0 -> sb.append(" 1 │ 2 │ 4 │ 6 │ 9 │ 12 ");
            case 1 -> sb.append(ANSI_BROWN_BOLD + " 1 " + ANSI_RESET + "│")
                    .append(" 2 │ 4 │ 6 │ 9 │ 12 ");
            case 2 -> sb.append(ANSI_BROWN_BOLD + " 1 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 2 " + ANSI_RESET + "│")
                    .append(" 4 │ 6 │ 9 │ 12 ");
            case 4 -> sb.append(ANSI_BROWN_BOLD + " 1 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 2 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 4 " + ANSI_RESET + "│")
                    .append(" 6 │ 9 │ 12 ");
            case 6 -> sb.append(ANSI_BROWN_BOLD + " 1 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 2 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 4 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 6 " + ANSI_RESET + "│")
                    .append(" 9 │ 12 ");
            case 9 -> sb.append(ANSI_BROWN_BOLD + " 1 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 2 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 4 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 6 " + ANSI_RESET + "│" +
                                ANSI_BROWN_BOLD + " 9 " + ANSI_RESET + "│")
                    .append(" 12 ");
            case 12 -> sb.append(ANSI_BROWN_BOLD + " 1 " + ANSI_RESET + "│" +
                                 ANSI_BROWN_BOLD + " 2 " + ANSI_RESET + "│" +
                                 ANSI_BROWN_BOLD + " 4 " + ANSI_RESET + "│" +
                                 ANSI_BROWN_BOLD + " 6 " + ANSI_RESET + "│" +
                                 ANSI_BROWN_BOLD + " 9 " + ANSI_RESET + "│" +
                                 ANSI_BROWN_BOLD + " 12 " + ANSI_RESET + "│");
        }
        
        return sb.toString();
    }
    
    /**
     * Generate the TUI representation of the personal goals. <br>
     * Personal goals are a smaller version of the normal shelves.
     *
     * @param personalGoal The id of the personal goal
     *
     * @return The TUI representation of the personal goal
     */
    public static String generatePersonalGoal(int personalGoal) {
        StringBuilder sb = new StringBuilder();
        switch( personalGoal ) {
            case 0 -> sb.append(
                    ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET
                    + "    \n" +
                    "        " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "\n" +
                    "      " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                    "  " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                    "\n" +
                    "    " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "    ");
            case 1 -> sb.append(
                    "\n" +
                    "  " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                    ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET +
                    "    \n" +
                    "        " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "\n"
                    + "      " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                    "        " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET
            );
            case 2 -> sb.append("\n" +
                                ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "    " + ANSI_YELLOW_BACKGROUND + "  " +
                                ANSI_RESET +
                                "  \n" +
                                "    " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                                "  " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "    " + ANSI_CYAN_BACKGROUND +
                                "  " +
                                ANSI_RESET + "\n" +
                                "\n" +
                                ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "        ");
            case 3 -> sb.append("        " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "\n" +
                                "\n" +
                                ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_BLUE_BACKGROUND + "  " +
                                ANSI_RESET +
                                "    \n" +
                                "      " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                                "  " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + ANSI_GREEN_BACKGROUND + "  " +
                                ANSI_RESET +
                                "\n     ");
            case 4 -> sb.append("          \n" +
                                "  " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                                "          \n" +
                                "  " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + ANSI_WHITE_BACKGROUND + "  " +
                                ANSI_RESET +
                                "    \n" +
                                "        " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "\n" +
                                ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "    " + ANSI_GREEN_BACKGROUND + "  " +
                                ANSI_RESET +
                                "  ");
            case 5 ->
                    sb.append("    " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_GREEN_BACKGROUND + "  " +
                              ANSI_RESET + "\n" +
                              "          \n" +
                              "      " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                              "          \n" +
                              "  " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "  " + ANSI_BLUE_BACKGROUND + "  " +
                              ANSI_RESET + "  \n" +
                              ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "        \n");
            case 6 -> sb.append(ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET
                                + "        \n" +
                                "      " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                                "  " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                                ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "        \n" +
                                "        " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "\n" +
                                "    " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "    ");
            case 7 -> sb.append("        " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "\n" +
                                "  " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                                "    " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                                ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "        \n" +
                                "      " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                                "      " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "  ");
            case 8 -> sb.append("    " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                                "          \n" +
                                "    " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                                "        " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "\n" +
                                "  " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "    " + ANSI_PURPLE_BACKGROUND +
                                "  " +
                                ANSI_RESET + "\n" +
                                ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "        ");
            case 9 -> sb.append("        " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "\n" +
                                "  " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                                ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "        \n" +
                                "      " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                                "  " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                                "      " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "  \n");
            case 10 -> sb.append("    " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                                 "  " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                                 ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "        \n" +
                                 "    " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                                 "        " + ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "\n" +
                                 "      " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  ");
            case 11 -> sb.append("    " + ANSI_WHITE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                                 "  " + ANSI_PURPLE_BACKGROUND + "  " + ANSI_RESET + "      \n" +
                                 "    " + ANSI_BLUE_BACKGROUND + "  " + ANSI_RESET + "    \n" +
                                 "      " + ANSI_CYAN_BACKGROUND + "  " + ANSI_RESET + "  \n" +
                                 "        " + ANSI_YELLOW_BACKGROUND + "  " + ANSI_RESET + "\n" +
                                 ANSI_GREEN_BACKGROUND + "  " + ANSI_RESET + "        ");
            
        }
        return createBox(sb.toString(), ANSI_DARK_BROWN_BOLD);
    }
    
}
