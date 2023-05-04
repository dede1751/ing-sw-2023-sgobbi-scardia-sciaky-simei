package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Shelf;
import org.junit.jupiter.api.Test;

class TUITest {
    
    @Test
    void printTest() {
        String s = String.valueOf(TUIUtils.Tiles.catTile);
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
        String s1 = String.valueOf(TUIUtils.Tiles.catTile);
        String s2 = String.valueOf(TUIUtils.Tiles.catTile);
        String s3 = String.valueOf(TUIUtils.Tiles.trophyTile);
        String s4 = String.valueOf(TUIUtils.Tiles.plantTile);
        String s5 = String.valueOf(TUIUtils.Tiles.bookTile);
        String s6 = String.valueOf(TUIUtils.Tiles.frameTile);
        String z = TUIUtils.concatString(s2, s3, 3);
        String z1 = TUIUtils.concatString(s1, s4, 3);
        String z2 = TUIUtils.concatString(s6, s5, 3);
        String f3 = TUIUtils.concatString(s6, s2, 3);
        String f2 = TUIUtils.concatString(s2, s3, 3);
        String f1 = TUIUtils.concatString(s5, s6, 3);
        String h3 = TUIUtils.concatString(z, f2, 3);
        String h2 = TUIUtils.concatString(z1, f3, 3);
        String h1 = TUIUtils.concatString(z2, f1, 3);
        // System.out.println(TUIUtils.concatString(h3, h1, 3));
        // System.out.println(TUIUtils.concatString(h2, h1, 3));
        // System.out.println(TUIUtils.concatString(h3, h2, 3));
        
        System.out.println(TUIUtils.printShelf(new Shelf()));
    }
}