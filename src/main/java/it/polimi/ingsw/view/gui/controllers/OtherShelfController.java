package it.polimi.ingsw.view.gui.controllers;


import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class OtherShelfController {
    @FXML
    private GridPane gridPane1;
    
    @FXML
    private Text playerName;
    
    @FXML
    private Text score;
    
    @FXML
    public void setPlayerName(String name) {
        playerName.setText(name);
    }
    
    public String getPlayerNickname(){
        return playerName.getText();
    }
    
    @FXML
    public void setScore(int score) {
        this.score.setText(String.valueOf(score));
    }
    
    @FXML
    void updateOtherShelf(Shelf shelf) {
        
        if( shelf != null ) {
            var matrix = shelf.getAllShelf();
            for( int i = Shelf.N_ROW - 1; i >= 0; i-- ) {
                for( int j = 0; j < Shelf.N_COL; j++ ) {
                    StringBuilder sb = new StringBuilder();
                    
                    switch( matrix[i][j].type() ) {
                        case TROPHIES -> sb.append("Trofei1.");
                        case CATS -> sb.append("Gatti1.");
                        case BOOKS -> sb.append("Libri1.");
                        case PLANTS -> sb.append("Piante1.");
                        case FRAMES -> sb.append("Cornici1.");
                        case GAMES -> sb.append("Giochi1.");
                        
                    }
                    
                    switch( matrix[i][j].sprite() ) {
                        case ONE -> sb.append("1.png");
                        case TWO -> sb.append("2.png");
                        case THREE -> sb.append("3.png");
                    }
                    
                    if( matrix[i][j] != null ) {
                        ImageView imageView = new ImageView(new Image("gui/assets/item_tiles/" + sb.toString()));
                        imageView.setPreserveRatio(true);
                        imageView.setFitWidth(40);
                        gridPane1.getChildren().add(imageView);
                        GridPane.setConstraints(imageView, j, -(i - Shelf.N_COL));
                        GridPane.setValignment(imageView, VPos.CENTER);
                    }
                }
                
            }
        }
    }
    
    @FXML
    private void initialize() {
    }
    
    
}
