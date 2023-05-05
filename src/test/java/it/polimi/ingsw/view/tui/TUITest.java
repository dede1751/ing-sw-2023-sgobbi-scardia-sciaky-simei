package it.polimi.ingsw.view.tui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.utils.exceptions.InvalidStringException;
import it.polimi.ingsw.utils.files.ResourcesManager;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.view.tui.TUIUtils;
import it.polimi.ingsw.model.Tile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

class TUITest {
    
    private String getResource(String className) throws IOException {
        return Files.readString(
                Path.of(ResourcesManager.testRootDir,
                        "it/polimi/ingsw/view/tui/resources/" + className + ".json"),
                StandardCharsets.UTF_8);
    }
    
    @Test
    void printTest() {
        String s = String.valueOf(TUIUtils.Tiles.catTile);
        System.out.println(s);
    }
    
    @Test
    void printBoardTest() {
        var name = ResourcesManager.getCurrentMethod();
        String json;
        try {
            json = getResource(name);
            Gson gson =
                    new GsonBuilder().registerTypeAdapter(Board.class,
                                                          new Board.BoardDeserializer()).create();
            var board = gson.fromJson(json, String[][].class);
            TUIUtils.printBoard(board);
        }
        catch( IOException e ) {
            e.printStackTrace();
            fail();
        }
        catch( InvalidStringException e ) {
            throw new RuntimeException(e);
        }
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