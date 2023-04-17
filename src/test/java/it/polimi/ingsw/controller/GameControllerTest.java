package it.polimi.ingsw.controller;

import com.google.gson.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.utils.files.ResourcesManager;
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
    
    private String getResource(String className) {
        String result = null;
        try {
            result = Files.readString(
                    Path.of(ResourcesManager.testRootDir,
                            "it/polimi/ingsw/controller/resources/" + className + "Test.json"),
                    StandardCharsets.UTF_8);
        }
        catch( IOException e ) {
            e.printStackTrace(System.err);
        }
        return result;
    }
    
    @Test
    public void needRefillTrue() {
        var name = ResourcesManager.getCurrentMethod();
        var json = getResource(name);
        Gson gson = new GsonBuilder().registerTypeAdapter(GameModel.class, new GameModel.ModelDeserializer()).create();
        var model = gson.fromJson(json, GameModel.class);
        var controller = new GameController(model, new ArrayList<>());
        assertTrue(controller.needRefill());
    }
    
    @Test
    public void turnManagerRefillTrue() {
        var name = ResourcesManager.getCurrentMethod();
        var json = getResource(name);
        Gson gson = new GsonBuilder().registerTypeAdapter(GameModel.class, new GameModel.ModelDeserializer()).create();
        var model = gson.fromJson(json, GameModel.class);
        var controller = new GameController(model, new ArrayList<>());
        assertTrue(controller.needRefill());
        var tiles = model.getBoard().getTiles();
        var numofNoTiles = Collections.frequency(tiles.values()
                                                           .stream()
                                                           .map(Tile::type)
                                                           .toList(), Tile.Type.NOTILE);
        controller.turnManager();
        var numofTiles = tiles.values().stream().filter((tile) -> tile != Tile.NOTILE).count();
        assertEquals( numofNoTiles, numofTiles );
    }
}