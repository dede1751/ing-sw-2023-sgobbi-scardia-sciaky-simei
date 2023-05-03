package it.polimi.ingsw.view.tui;

public class TUITile {
    
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
}
