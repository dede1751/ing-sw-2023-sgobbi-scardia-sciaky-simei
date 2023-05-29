package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.view.gui.GUIApp;
import it.polimi.ingsw.view.gui.GUIUtils;
import it.polimi.ingsw.view.messages.Move;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class LocalPlayerController {
    
    @FXML
    private ImageView personalGoal;
    @FXML
    private Button b0;
    @FXML
    private Button b1;
    @FXML
    private Button b2;
    @FXML
    private Button b3;
    @FXML
    private Button b4;
    @FXML
    private Text score;
    
    @FXML
    private GridPane localPlayerGrid;
    
    private Map<Coordinate, ImageView> imageMap = new HashMap<>();
    
    @FXML
    public void initialize() {
        
        b0.setOnAction(event -> buttonAction(0));
        b1.setOnAction(event -> buttonAction(1));
        b2.setOnAction(event -> buttonAction(2));
        b3.setOnAction(event -> buttonAction(3));
        b4.setOnAction(event -> buttonAction(4));
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                ImageView imageView = new ImageView();
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(52);
                GridPane.setValignment(imageView, VPos.CENTER);
                GridPane.setHalignment(imageView, HPos.CENTER);
                localPlayerGrid.add(imageView, j, i);
                imageMap.put(new Coordinate(i, j), imageView);
            }
        }
        
        
    }
    
    @FXML
    public void updateShelf(Shelf shelf) {
        GUIUtils.threadPool.submit(() -> GUIUtils.updateShelf(shelf, imageMap));
    }
    
    @FXML
    public void setScore(int score) {
        this.score.setFont(new Font("Noto Sans", 25));
        this.score.setText("Score: " + score);
    }
    private void buttonAction(int column) {
        AppClient.getViewInstance().notifyMove(
                new Move(GUIApp.getCoordinateSelection(), GUIApp.getTileSelection(), column));
    }
    
    
    public void setPersonalGoal(int id) {
        id = id + 1;
        this.personalGoal.setImage(new Image("gui/assets/personal_goal_cards/Personal_Goals" + id + ".png"));
    }
}
