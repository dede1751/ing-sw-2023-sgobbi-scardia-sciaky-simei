package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.goals.personal.PersonalGoal;
import it.polimi.ingsw.utils.files.ResourcesManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Controller")
class GameControllerTest {
    
    private String getResource(String className) throws IOException {
        return Files.readString(
                Path.of(ResourcesManager.testRootDir,
                        "controller/resources/" + className + "Test.json"),
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
                var controller = new GameController(model);
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
                var controller = new GameController(model);
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
                var controller = new GameController(model);
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
                var controller = new GameController(model);
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
                var controller = new GameController(model);
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
                var controller = new GameController(model);
                controller.turnManager();
                var gameOver = model.isLastTurn();
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
                var controller = new GameController(model);
                controller.turnManager();
                var gameOver = model.isLastTurn();
                assertFalse(gameOver);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @DisplayName("Common goal X achieved by the current player")
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
                var controller = new GameController(model);
                
                var completedGoalX = model.getCurrentPlayer().isCompletedGoalX();
                assertFalse(completedGoalX);
                
                var commonGoalScore = model.getCurrentPlayer().getCommonGoalScore();
                var goalPeek = model.peekStackCGX();
                
                controller.turnManager();
                
                var newCommonGoalScore = model.getCurrentPlayer().getCommonGoalScore();
                var expectedCommonGoalScore = commonGoalScore + goalPeek;
                
                assertEquals(expectedCommonGoalScore, newCommonGoalScore);
                
                completedGoalX = model.getCurrentPlayer().isCompletedGoalX();
                assertTrue(completedGoalX);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @DisplayName("Common goal X already achieved by the current player")
        @Test
        public void turnManagerGoalXFalse1() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                var completedGoalX = model.getCurrentPlayer().isCompletedGoalX();
                assertTrue(completedGoalX);
                
                var commonGoaLScore = model.getCurrentPlayer().getCommonGoalScore();
                var goalPeek = model.peekStackCGX();
                
                controller.turnManager();
                
                var newCommonGoaLScore = model.getCurrentPlayer().getCommonGoalScore();
                var newGoalPeek = model.peekStackCGX();
                
                assertEquals(commonGoaLScore, newCommonGoaLScore);
                assertEquals(goalPeek, newGoalPeek);
                
                completedGoalX = model.getCurrentPlayer().isCompletedGoalX();
                assertTrue(completedGoalX);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @DisplayName("Common goal X not achieved by the current player")
        @Test
        public void turnManagerGoalXFalse2() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                var completedGoalX = model.getCurrentPlayer().isCompletedGoalX();
                assertFalse(completedGoalX);
                
                var commonGoaLScore = model.getCurrentPlayer().getCommonGoalScore();
                var goalPeek = model.peekStackCGX();
                
                controller.turnManager();
                
                var newCommonGoaLScore = model.getCurrentPlayer().getCommonGoalScore();
                var newGoalPeek = model.peekStackCGX();
                
                assertEquals(commonGoaLScore, newCommonGoaLScore);
                assertEquals(goalPeek, newGoalPeek);
                
                completedGoalX = model.getCurrentPlayer().isCompletedGoalX();
                assertFalse(completedGoalX);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @DisplayName("Common goal Y achieved by the current player")
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
                var controller = new GameController(model);
                
                var completedGoalY = model.getCurrentPlayer().isCompletedGoalY();
                assertFalse(completedGoalY);
                
                var commonGoalScore = model.getCurrentPlayer().getCommonGoalScore();
                var goalPeek = model.peekStackCGY();
                var expectedCommonGoalScore = commonGoalScore + goalPeek;
                
                controller.turnManager();
                
                var newCommonGoalScore = model.getCurrentPlayer().getCommonGoalScore();
                
                assertEquals(expectedCommonGoalScore, newCommonGoalScore);
                
                completedGoalY = model.getCurrentPlayer().isCompletedGoalY();
                assertTrue(completedGoalY);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @DisplayName("Common goal Y already achieved by the current player")
        @Test
        public void turnManagerGoalYFalse1() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                var completedGoalY = model.getCurrentPlayer().isCompletedGoalY();
                assertTrue(completedGoalY);
                
                var commonGoaLScore = model.getCurrentPlayer().getCommonGoalScore();
                var goalPeek = model.peekStackCGY();
                
                controller.turnManager();
                
                var newCommonGoaLScore = model.getCurrentPlayer().getCommonGoalScore();
                var newGoalPeek = model.peekStackCGY();
                
                assertEquals(commonGoaLScore, newCommonGoaLScore);
                assertEquals(goalPeek, newGoalPeek);
                
                completedGoalY = model.getCurrentPlayer().isCompletedGoalY();
                assertTrue(completedGoalY);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @DisplayName("Common goal Y not achieved by the current player")
        @Test
        public void turnManagerGoalYFalse2() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                var completedGoalY = model.getCurrentPlayer().isCompletedGoalX();
                assertFalse(completedGoalY);
                
                var commonGoaLScore = model.getCurrentPlayer().getCommonGoalScore();
                var goalPeek = model.peekStackCGY();
                
                controller.turnManager();
                
                var newCommonGoaLScore = model.getCurrentPlayer().getCommonGoalScore();
                var newGoalPeek = model.peekStackCGY();
                
                assertEquals(commonGoaLScore, newCommonGoaLScore);
                assertEquals(goalPeek, newGoalPeek);
                
                completedGoalY = model.getCurrentPlayer().isCompletedGoalY();
                assertFalse(completedGoalY);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @Test
        public void turnManagerPersonalGoalTrue() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                var shelf = model.getCurrentPlayer().getShelf();
                var pgID = model.getCurrentPlayer().getPg();
                var personalGoal = new PersonalGoal(pgID);
                var personalScore = personalGoal.checkGoal(shelf);
                
                controller.turnManager();
                
                var newPersonalScore = model.getCurrentPlayer().getPersonalGoalScore();
                
                assertEquals(personalScore, newPersonalScore);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @Test
        public void turnManagerPersonalGoalFalse() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                var shelf = model.getCurrentPlayer().getShelf();
                var pgID = model.getCurrentPlayer().getPg();
                var personalGoal = new PersonalGoal(pgID);
                var personalScore = personalGoal.checkGoal(shelf);
                
                controller.turnManager();
                
                var newPersonalScore = model.getCurrentPlayer().getPersonalGoalScore();
                
                assertEquals(personalScore, newPersonalScore);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
    }
    
    
    @Tag("calculateAdjency")
    @Nested
    class calculateAdjencyTest {
        
        @Test
        public void calculateAdjencyTrue() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                int score = model.getCurrentPlayer().getScore();
                int adjencyScore = controller.calculateAdjacency(model.getCurrentPlayer().getShelf());
                
                controller.turnManager();
                int newScore = model.getCurrentPlayer().getScore();
                
                assertEquals(score + adjencyScore, newScore);
                assertEquals(adjencyScore, 3);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
        @Test
        public void calculateAdjencyFalse() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                int score = model.getCurrentPlayer().getScore();
                int adjencyScore = controller.calculateAdjacency(model.getCurrentPlayer().getShelf());
                
                controller.turnManager();
                int newScore = model.getCurrentPlayer().getScore();
                
                assertNotEquals(score + 5, newScore);
                assertNotEquals(adjencyScore, 5);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
    }
    
    
    @Tag("setNextPlayer")
    @Nested
    class setNextPlayerTest {
        
        
        @Test
        public void setNextPlayerTrue() {
            var name = ResourcesManager.getCurrentMethod();
            String json;
            try {
                json = getResource(name);
                Gson gson =
                        new GsonBuilder().registerTypeAdapter(GameModel.class,
                                                              new GameModel.ModelDeserializer()).create();
                var model = gson.fromJson(json, GameModel.class);
                var controller = new GameController(model);
                
                var currentPlayerIndex = model.getCurrentPlayerIndex();
                
                controller.nextPlayerSetter();
                
                var newCurrentPlayerIndex = model.getCurrentPlayerIndex();
                var newCurrentPlayer = model.getCurrentPlayer();
                
                assertEquals(currentPlayerIndex + 1, newCurrentPlayerIndex);
                assertEquals(model.getPlayers().get(currentPlayerIndex + 1), newCurrentPlayer);
            }
            catch( IOException e ) {
                e.printStackTrace();
                fail();
            }
        }
        
    }
}