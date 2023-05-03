package it.polimi.ingsw.view.tui;

import org.junit.jupiter.api.Test;
import it.polimi.ingsw.view.tui.TUITile;

class TUITest {
    
    @Test
    void printTest() {
        String s = String.valueOf(TUITile.Tiles.catTile);
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
        
        TUI tui = new TUI();
        String s1 = String.valueOf(TUITile.Tiles.catTile);
        String s2 = String.valueOf(TUITile.Tiles.catTile);
        String s3 = String.valueOf(TUITile.Tiles.trophyTile);
        String s4 = String.valueOf(TUITile.Tiles.plantTile);
        String s5 = String.valueOf(TUITile.Tiles.bookTile);
        String s6 = String.valueOf(TUITile.Tiles.frameTile);
        String z = tui.concatString(s2, s3, 3);
        String z1 = tui.concatString(s1, s4, 3);
        String z2 = tui.concatString(s6, s5, 3);
        String f3 = tui.concatString(s6, s2, 3);
        String f2 = tui.concatString(s2, s3, 3);
        String f1 = tui.concatString(s5, s6, 3);
        
        String h3 = tui.concatString(z, f2, 3);
        String h2 = tui.concatString(z1, f3, 3);
        String h1 = tui.concatString(z2, f1, 3);
        System.out.println(tui.concatString(h3, h1, 3));
        System.out.println(tui.concatString(h2, h1, 3));
        System.out.println(tui.concatString(h3, h2, 3));
        
    }
}