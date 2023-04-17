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
    
    @Tag("needRefill")
    @Nested
    class needRefillTest {
        @Test
        public void needRefillTrue() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
                assertTrue(controller.needRefill());
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @Test
        public void needRefillFalse() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
                assertFalse(controller.needRefill());
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
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
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
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
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
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
        
        @Test
        public void turnManagerRefillFalse() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
                var boardPrev = model.getBoard();
                assertFalse(controller.needRefill());
                controller.turnManager();
                var boardNext = model.getBoard();
                assertEquals(boardPrev, boardNext);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @Test
        public void turnManagerGameOverTrue() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
                controller.turnManager();
                var gameOver = model.isFinalTurn();
                assertTrue(gameOver);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @Test
        public void turnManagerGameOverFalse() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
                controller.turnManager();
                var gameOver = model.isFinalTurn();
                assertFalse(gameOver);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @Test
        public void turnManagerGoalXTrue() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
                var completedGoalX = model.getCurrentPlayer().isCompletedGoalX();
                assertFalse(completedGoalX);
                var score = model.getCurrentPlayer().getScore();
                var goalScore = model.getStackCGX().peek();
                var expectedScore = score + goalScore;
                controller.turnManager();
                var newScore = model.getCurrentPlayer().getScore();
                assertEquals(expectedScore, newScore);
                completedGoalX = model.getCurrentPlayer().isCompletedGoalX();
                assertTrue(completedGoalX);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
    
        @Test
        public void turnManagerGoalXFalse() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
            
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
    
        @Test
        public void turnManagerGoalYTrue() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
            
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
    
        @Test
        public void turnManagerGoalYFalse() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model, new ArrayList<>());
            
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
    }
}