package it.polimi.ingsw.view.tui;

public class TUITile {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    
    public enum Tiles {
        
        
        catTile1 {
            public String toString() {
                return ANSI_GREEN + """
                       ┏━━━┓
                       ┃ 𝐂 ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
        catTile2 {
            public String toString() {
                return ANSI_GREEN + """
                       ┏━━━┓
                       ┃ ℂ ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
        catTile3 {
            public String toString() {
                return ANSI_GREEN + """
                       ┏━━━┓
                       ┃ 𝑪 ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
        bookTile1 {
            public String toString() {
                return ANSI_WHITE + """
                       ┏━━━┓
                       ┃ 𝐁 ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
        bookTile2 {
            public String toString() {
                return ANSI_WHITE + """
                       ┏━━━┓
                       ┃ 𝔹 ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
        bookTile3 {
            public String toString() {
                return ANSI_WHITE + """
                       ┏━━━┓
                       ┃ 𝑩 ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
        gameTile1 {
            public String toString() {
                return ANSI_YELLOW + """
                       ┏━━━┓
                       ┃ 𝐆 ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
        gameTile2 {
            public String toString() {
                return ANSI_YELLOW + """
                       ┏━━━┓
                       ┃ 𝔾 ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
        gameTile3 {
            public String toString() {
                return ANSI_YELLOW + """
                       ┏━━━┓
                       ┃ 𝑮 ┃
                       ┗━━━┛
                       """ + ANSI_RESET;
            }
        },
    }
}
