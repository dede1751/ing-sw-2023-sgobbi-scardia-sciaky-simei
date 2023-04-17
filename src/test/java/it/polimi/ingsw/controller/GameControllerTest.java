package it.polimi.ingsw.controller;

import com.google.gson.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.utils.files.ResourcesManager;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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
    void needRefillTrue() {
        var name = ResourcesManager.getCurrentMethod();
        var json = getResource(name);
        Gson gson = new GsonBuilder().registerTypeAdapter(GameModel.class, new GameModel.ModelDeserializer()).create();
        var model = gson.fromJson(json, GameModel.class);
        var controller = new GameController(model, new ArrayList<>());
        assertTrue(controller.needRefill());
    }
}