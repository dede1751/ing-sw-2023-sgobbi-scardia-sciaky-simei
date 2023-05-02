package it.polimi.ingsw.view.tui;

import org.junit.jupiter.api.Test;
import it.polimi.ingsw.view.tui.TUITile;

class TUITest {
    
    @Test
    void printTest() {
        String s = String.valueOf(TUITile.Tiles.catTile1);
        System.out.println(s);
    }
    
    @Test
    void run() {
    }
    
    @Test
    void userLogin() {
    }
    
    @Test
    void update() {
    }
    
    
    @Test
    void concat() {
        String s1 = "]]]\n" +
                    "[[[\n" +
                    "[[[\n";
        String s2 = "iii\n" +
                    "iii\n" +
                    "iii\n";
        TUI tui = new TUI();
        String s = tui.concatString(s2, s1, 3);
        
        System.out.println(tui.concatString(s2, s1, 1));
        
    }
}