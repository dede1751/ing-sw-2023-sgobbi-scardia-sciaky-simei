package it.polimi.ingsw.view.tui;

public class TUITile {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_MAGENTA = "\033[0;35m";
    
    
    public enum Tiles {
        
        
        catTile1 {
            public String toString() {
                return ANSI_GREEN + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_GREEN + "┃ \uD835\uDC02 ┃\n" + ANSI_RESET +
                       ANSI_GREEN + "┗━━━┛" + ANSI_RESET;
            }
        },
        catTile2 {
            public String toString() {
                return ANSI_GREEN + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_GREEN + "┃ ℂ ┃\n" + ANSI_RESET +
                       ANSI_GREEN + "┗━━━┛" + ANSI_RESET;
            }
        },
        catTile3 {
            public String toString() {
                return ANSI_GREEN + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_GREEN + "┃ \uD835\uDC6A ┃\n" + ANSI_RESET +
                       ANSI_GREEN + "┗━━━┛" + ANSI_RESET;
            }
        },
        bookTile1 {
            public String toString() {
                return ANSI_WHITE + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_WHITE + "┃ 𝐁 ┃\n" + ANSI_RESET +
                       ANSI_WHITE + "┗━━━┛" + ANSI_RESET;
            }
        },
        bookTile2 {
            public String toString() {
                return ANSI_WHITE + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_WHITE + "┃ 𝔹 ┃\n" + ANSI_RESET +
                       ANSI_WHITE + "┗━━━┛" + ANSI_RESET;
            }
        },
        bookTile3 {
            public String toString() {
                return ANSI_WHITE + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_WHITE + "┃ 𝑩 ┃\n" + ANSI_RESET +
                       ANSI_WHITE + "┗━━━┛" + ANSI_RESET;
            }
        },
        gameTile1 {
            public String toString() {
                return ANSI_YELLOW + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_YELLOW + "┃ 𝐆 ┃\n" + ANSI_RESET +
                       ANSI_YELLOW + "┗━━━┛" + ANSI_RESET;
            }
        },
        gameTile2 {
            public String toString() {
                return ANSI_YELLOW + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_YELLOW + "┃ 𝔾 ┃\n" + ANSI_RESET +
                       ANSI_YELLOW + "┗━━━┛" + ANSI_RESET;
            }
        },
        gameTile3 {
            public String toString() {
                return ANSI_YELLOW + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_YELLOW + "┃ 𝑮 ┃\n" + ANSI_RESET +
                       ANSI_YELLOW + "┗━━━┛" + ANSI_RESET;
            }
        },
        
        
        frameTile1 {
            public String toString() {
                return ANSI_BLUE + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_BLUE + "┃ 𝐅 ┃\n" + ANSI_RESET +
                       ANSI_BLUE + "┗━━━┛" + ANSI_RESET;
            }
        },
        frameTile2 {
            public String toString() {
                return ANSI_BLUE + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_BLUE + "┃ 𝔽 ┃\n" + ANSI_RESET +
                       ANSI_BLUE + "┗━━━┛" + ANSI_RESET;
            }
        },
        frameTile3 {
            public String toString() {
                return ANSI_BLUE + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_BLUE + "┃ 𝑭 ┃\n" + ANSI_RESET +
                       ANSI_BLUE + "┗━━━┛" + ANSI_RESET;
            }
        },
        trophyTile1 {
            public String toString() {
                return ANSI_CYAN + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_CYAN + "┃ 𝐓 ┃\n" + ANSI_RESET +
                       ANSI_CYAN + "┗━━━┛" + ANSI_RESET;
            }
        },
        trophyTile2 {
            public String toString() {
                return ANSI_CYAN + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_CYAN + "┃ 𝕋 ┃\n" + ANSI_RESET +
                       ANSI_CYAN + "┗━━━┛" + ANSI_RESET;
            }
        },
        trophyTile3 {
            public String toString() {
                return ANSI_CYAN + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_CYAN + "┃ 𝑻 ┃\n" + ANSI_RESET +
                       ANSI_CYAN + "┗━━━┛" + ANSI_RESET;
            }
        },
        plantTile1 {
            public String toString() {
                return ANSI_MAGENTA + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_MAGENTA + "┃ 𝐏 ┃\n" + ANSI_RESET +
                       ANSI_MAGENTA + "┗━━━┛" + ANSI_RESET;
            }
        },
        plantTile2 {
            public String toString() {
                return ANSI_MAGENTA + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_MAGENTA + "┃ ℙ ┃\n" + ANSI_RESET +
                       ANSI_MAGENTA + "┗━━━┛" + ANSI_RESET;
            }
        },
        plantTile3 {
            public String toString() {
                return ANSI_MAGENTA + "┏━━━┓\n" + ANSI_RESET +
                       ANSI_MAGENTA + "┃ 𝑷 ┃\n" + ANSI_RESET +
                       ANSI_MAGENTA + "┗━━━┛" + ANSI_RESET;
            }
        }
    }
}
