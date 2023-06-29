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

    @Test
    void commonGoals() {
        System.out.println(TUIUtils.generateCommonGoal(0,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(1,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(2,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(3,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(4,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(5,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(6,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(7,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(8,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(9,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(10,8));
        System.out.println("\n\n\n\n");
        System.out.println(TUIUtils.generateCommonGoal(11,8));
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

    
    



