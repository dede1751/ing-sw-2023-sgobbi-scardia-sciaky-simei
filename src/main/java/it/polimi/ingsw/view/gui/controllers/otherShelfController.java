package it.polimi.ingsw.view.gui.controllers;


import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.exceptions.OccupiedTileException;
import it.polimi.ingsw.utils.exceptions.OutOfBoundCoordinateException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class otherShelfController {
    @FXML
    private GridPane gridPane1;
    
    
    
     @FXML
    public void boardButton1(ActionEvent event) {
         ImageView imageView = new ImageView(new Image("gui/assets/item_tiles/" + "Trofei1.1.png"));
         imageView.setFitHeight(20);
         imageView.setFitWidth(20);
         GridPane.setConstraints(imageView, 2, 2);
         gridPane1.getChildren().add(imageView);
         Button button = (Button) event.getSource();
         int row = GridPane.getRowIndex(button);
         int column = GridPane.getColumnIndex(button);
         button.setDisable(true);
         
         
       Shelf shelf=new Shelf();
       List<Tile> tile= new ArrayList<>();
       tile.add(new Tile(Tile.Type.CATS, Tile.Sprite.TWO));
       tile.add(new Tile(Tile.Type.TROPHIES, Tile.Sprite.TWO));
       shelf.addTiles(tile,0);
       updateOtherShelf(shelf);
    
       
    }
    
    
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
                        imageView.setFitHeight(20);
                       imageView.setFitWidth(20);
                       GridPane.setConstraints(imageView, i, j);
                       gridPane1.getChildren().add(imageView);
                    }
                }
                
            }
        }
        
        
        
        
    }
    
}
