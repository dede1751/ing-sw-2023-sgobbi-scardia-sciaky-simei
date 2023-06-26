package it.polimi.ingsw.view.gui.controllers;


import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.view.gui.GUIUtils;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
/**
 
 The OtherShelfController class is responsible for managing the display of the other player's shelves.
 
 It controls the grid pane, player name, score, and image map.
 */
public class OtherShelfController {
    @FXML
    private GridPane gridPane1;
    
    @FXML
    private Text playerName;
    
    @FXML
    private Text score;
    
    private final Map<Coordinate, ImageView> imageMap = new HashMap<>();
    /**
     
     Sets the player name to be displayed.
     @param name The player's name.
     */
    @FXML
    public void setPlayerName(String name) {
        playerName.setFont(new Font("Noto Sans", 20));
        playerName.setText(name);
    }
    /**
     
     Returns the player's nickname.
     @return The player's nickname.
     */
    public String getPlayerNickname() {
        return playerName.getText();
    }
    /**
     
     Sets the player's score to be displayed.
     @param score The player's score.
     */
    @FXML
    public void setScore(int score) {
        this.score.setFont(new Font("Noto Sans", 20));
        this.score.setText("Score: " + score);
    }
    /**
     
     Updates the other player's shelf with the given shelf information.
     @param shelf The other player's shelf.
     */
    @FXML
    void updateOtherShelf(Shelf shelf) {
        GUIUtils.threadPool.submit(() -> GUIUtils.updateShelf(shelf, imageMap));
    }
    /**
     
     Initializes the OtherShelfController.
     Configures the grid pane and creates image views for each cell in the shelf.
     */
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
