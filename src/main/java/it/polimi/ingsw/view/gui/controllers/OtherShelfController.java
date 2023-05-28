package it.polimi.ingsw.view.gui.controllers;


import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.view.gui.GUIUtils;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherShelfController {
    @FXML
    private GridPane gridPane1;
    
    @FXML
    private Text playerName;
    
    @FXML
    private Text score;
    
    private final Map<Coordinate, ImageView> imageMap = new HashMap<>();
    
    @FXML
    public void setPlayerName(String name) {
        playerName.setText(name);
    }
    
    public String getPlayerNickname() {
        return playerName.getText();
    }
    
    @FXML
    public void setScore(int score) {
        this.score.setText("Score: " + score);
    }
    
    @FXML
    void updateOtherShelf(Shelf shelf) {
       GUIUtils.threadPool.submit(() -> GUIUtils.updateShelf(shelf, imageMap));
    }
    
    @FXML
    private void initialize() {
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(36);
                gridPane1.add(imageView, j, i);
                imageMap.put(new Coordinate(i, j), imageView);
                GridPane.setValignment(imageView, VPos.CENTER);
                GridPane.setHalignment(imageView, HPos.CENTER);
            }
        }
    }
    
    
    
    
}
