package it.polimi.ingsw.view.gui.controllers;


import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import javafx.fxml.FXML;
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
    public void setPlayerName(String name){
        playerName.setText(name);
    }
    
    @FXML
    public void setScore(int score){
        this.score.setText(String.valueOf(score));
    }
    
    @FXML
    void updateOtherShelf(Shelf shelf) {
       
        if( shelf != null ) {
            var matrix = shelf.getAllShelf();
            for( int i = Shelf.N_ROW - 1; i >= 0; i-- ) {
                for( int j = 0; j < Shelf.N_COL; j++ ) {
                    StringBuilder sb=new StringBuilder();
                    
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
                    
                    if( matrix[i][j]!=null) {
                        ImageView imageView = new ImageView(new Image("gui/assets/item_tiles/" + sb.toString()));
                        imageView.setFitHeight(30);
                       imageView.setFitWidth(30);
                       GridPane.setConstraints(imageView, j, -(i-Shelf.N_COL));
                       gridPane1.getChildren().add(imageView);
                    }
                }
                
            }
        }
    }
    
    @FXML
    private void initialize() {
        Shelf shelf = new Shelf();
        List<Tile> list = new ArrayList<Tile>();
        list.add(new Tile(Tile.Type.TROPHIES, Tile.Sprite.TWO));
        list.add(new Tile(Tile.Type.TROPHIES, Tile.Sprite.TWO));
        list.add(new Tile(Tile.Type.TROPHIES, Tile.Sprite.TWO));
        shelf.addTiles(list, 0);
        updateOtherShelf(shelf);
    }
    
    

}
