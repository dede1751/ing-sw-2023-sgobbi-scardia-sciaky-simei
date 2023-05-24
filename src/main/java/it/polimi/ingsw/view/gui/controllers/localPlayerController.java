package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Shelf;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class localPlayerController {
    @FXML
    private GridPane localPlayerGrid;
    
  
    void updateShelf(Shelf shelf) {
        
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
                        localPlayerGrid.getChildren().add(imageView);
                    }
                }
                
            }
        }
        
        
        
        
    }
    
    public void shelfColumnButton(ActionEvent event) {
        System.out.println("CIAO");
        ImageView imageView = new ImageView(new Image("gui/assets/item_tiles/" + "Trofei1.1.png"));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        GridPane.setConstraints(imageView, 2, 2);
        localPlayerGrid.getChildren().add(imageView);
        Button button = (Button) event.getSource();
        int row = GridPane.getRowIndex(button);
        int column = GridPane.getColumnIndex(button);
        button.setDisable(true);
        
        
        
    }
}
