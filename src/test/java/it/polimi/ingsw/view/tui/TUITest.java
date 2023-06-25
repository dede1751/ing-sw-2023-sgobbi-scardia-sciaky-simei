package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.files.ResourcesManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class TUITest {
    
    private String getResource(String className) throws IOException {
        return Files.readString(
                Path.of(ResourcesManager.testRootDir,
                        "it/polimi/ingsw/view/tui/resources/" + className + ".json"),
                StandardCharsets.UTF_8);
    }
    
    @Test
    void printTest() {
        Tile t = new Tile(Tile.Type.CATS);
        String s = String.valueOf(t.toTile());
        System.out.println(s);
    }
    
    /*
>>>>>>> 398fa2a1afb924874a0210a0b44d8b4c039877ab
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
        String s7 = TUIUtils.printCommonGoal(7);
        String s8 = TUIUtils.printCommonGoal(1);
        String z = TUIUtils.concatString(s2, s3, 3);
        String z1 = TUIUtils.concatString(s1, s4, 3);
        String z2 = TUIUtils.concatString(s6, s5, 3);
        String f3 = TUIUtils.concatString(s6, s2, 3);
        String f2 = TUIUtils.concatString(s2, s3, 3);
        String f1 = TUIUtils.concatString(s5, s6, 3);
        String h3 = TUIUtils.concatString(z, f2, 3);
        String h2 = TUIUtils.concatString(z1, f3, 3);
        String h1 = TUIUtils.concatString(z2, f1, 3);
        String h4 = TUIUtils.concatString(s7, s8, 0);
        // System.out.println(TUIUtils.concatString(h3, h1, 3));
        // System.out.println(TUIUtils.concatString(h2, h1, 3));
        // System.out.println(TUIUtils.concatString(h3, h2, 3));
        System.out.println(h4);
        System.out.println(TUIUtils.printShelf(new Shelf()));
    }
    */
    @Test
    void commonGoals() {
        System.out.println(TUIUtils.generateCommonGoal(0));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(1));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(2));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(3));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(4));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(5));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(6));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(7));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(9));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(10));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(11));
    }
    
    @Test
    void personalGoals() {
        
        System.out.println(TUIUtils.generatePersonalGoal(0));
        System.out.println(TUIUtils.generatePersonalGoal(1));
        System.out.println(TUIUtils.generatePersonalGoal(2));
        System.out.println(TUIUtils.generatePersonalGoal(3));
        System.out.println(TUIUtils.generatePersonalGoal(4));
        System.out.println(TUIUtils.generatePersonalGoal(5));
        System.out.println(TUIUtils.generatePersonalGoal(6));
        System.out.println(TUIUtils.generatePersonalGoal(7));
        System.out.println(TUIUtils.generatePersonalGoal(8));
        System.out.println(TUIUtils.generatePersonalGoal(9));
        System.out.println(TUIUtils.generatePersonalGoal(10));
        System.out.println(TUIUtils.generatePersonalGoal(11));
        
        
    }
}

    
    



