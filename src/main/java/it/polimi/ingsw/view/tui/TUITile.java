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
        
        
        frameTile1 {
            public String toString() {
                return ANSI_BLUE + """
                        ┏━━━┓
                        ┃ 𝐅 ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        },
        frameTile2 {
            public String toString() {
                return ANSI_BLUE + """
                        ┏━━━┓
                        ┃ 𝔽 ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        },
        frameTile3 {
            public String toString() {
                return ANSI_BLUE + """
                        ┏━━━┓
                        ┃ 𝑭 ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        },
        trophyTile1 {
            public String toString() {
                return ANSI_CYAN + """
                        ┏━━━┓
                        ┃ 𝐓 ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        },
        trophyTile2 {
            public String toString() {
                return ANSI_CYAN + """
                        ┏━━━┓
                        ┃ 𝕋 ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        },
        trophyTile3 {
            public String toString() {
                return ANSI_CYAN + """
                        ┏━━━┓
                        ┃ 𝑻 ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        },
        plantTile1 {
            public String toString() {
                return ANSI_MAGENTA + """
                        ┏━━━┓
                        ┃ 𝐏 ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        },
        plantTile2 {
            public String toString() {
                return ANSI_MAGENTA + """
                        ┏━━━┓
                        ┃ ℙ ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        },
        plantTile3 {
            public String toString() {
                return ANSI_MAGENTA + """
                        ┏━━━┓
                        ┃ 𝑷 ┃
                        ┗━━━┛
                        """ + ANSI_RESET;
            }
        }
    }
}
