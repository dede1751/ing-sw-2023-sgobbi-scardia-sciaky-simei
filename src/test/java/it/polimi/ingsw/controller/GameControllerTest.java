package it.polimi.ingsw.controller;

import com.google.gson.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.files.ResourcesManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Controller")
class GameControllerTest {
    
    private String getResource(String className) throws IOException {
        return Files.readString(
                Path.of(ResourcesManager.testRootDir,
                        "it/polimi/ingsw/controller/resources/" + className + "Test.json"),
                StandardCharsets.UTF_8);
    }
    
    @Test
    public void needRefillTrue() {
        var name = ResourcesManager.getCurrentMethod();
        String json;
        try {
            json = getResource(name);
            Gson gson =
                    new GsonBuilder().registerTypeAdapter(GameModel.class, new GameModel.ModelDeserializer()).create();
            var model = gson.fromJson(json, GameModel.class);
            var controller = new GameController(model, new ArrayList<>());
            assertTrue(controller.needRefill());
        }
        catch( IOException e ) {
            e.printStackTrace();
            fail();
        }
        
    }
    
    @Tag("turnManager")
    @Nested
    class turnManagerTest {
        
        @Test
        public void turnManagerRefillTrue1() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class, new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
                assertTrue(controller.needRefill());
                var tiles = model.getBoard().getTiles();
                var numofNoTiles = Collections.frequency(tiles.values()
                                                                 .stream()
                                                                 .map(Tile::type)
                                                                 .toList(), Tile.Type.NOTILE);
                controller.turnManager();
                tiles = model.getBoard().getTiles();
                var numofTiles = tiles.values().stream().filter((tile) -> tile != Tile.NOTILE).count();
                assertEquals(numofNoTiles, numofTiles);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @Test
        public void turnManagerRefillTrue2() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class, new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
                assertTrue(controller.needRefill());
                controller.turnManager();
                var tiles = model.getBoard().getTiles();
                var tilesOnBoard = tiles.values()
                                              .stream()
                                              .filter((tile) -> !tile.equals(Tile.NOTILE))
                                              .count();
                var tilesInTileBag = model.getTileBag().currentTileNumber();
                assertEquals(tilesInTileBag, tilesOnBoard);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
    }
}