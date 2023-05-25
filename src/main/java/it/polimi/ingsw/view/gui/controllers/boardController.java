package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileBag;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Map;

public class boardController {
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private void initialize(){
       /* int rows = gridPane.getRowCount();
        int columns = gridPane.getColumnCount();
    
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Button button = new Button("Button"+row +col);
                button.setOnAction(event -> boardButton());
                gridPane.add(button, col, row);
            }
        }*/
        
        
        
        Board board = new Board(3);
        TileBag tileBag = new TileBag();
        board.refill(tileBag);
        updateBoard(board);
    }
    
    @FXML
    private void boardButton()  {
        
        
        
        
        
        //Button button = (Button) event.getSource();
        //int row = GridPane.getRowIndex(button);
        //int column = GridPane.getColumnIndex(button);
        System.out.println("CIAO");
       
    }
    
    
    
    
    
    void updateBoard(Board board) {
        Map<Coordinate, Tile> tiles = board.getTiles();
        for( Coordinate coordinate : tiles.keySet() ) {
            StringBuilder sb = new StringBuilder();
            Boolean noTileFlag = false;
            // Access each coordinate
            int x = coordinate.row();
            int y = coordinate.col();
            switch( tiles.get(coordinate).type() ) {
                case TROPHIES -> sb.append("Trofei1.");
                case CATS -> sb.append("Gatti1.");
                case BOOKS -> sb.append("Libri1.");
                case PLANTS -> sb.append("Piante1.");
                case FRAMES -> sb.append("Cornici1.");
                case GAMES -> sb.append("Giochi1.");
                case NOTILE -> noTileFlag = true;
            }
            
            switch( tiles.get(coordinate).sprite() ) {
                case ONE -> sb.append("1.png");
                case TWO -> sb.append("2.png");
                case THREE -> sb.append("3.png");
            }
            if( !noTileFlag ) {
                ImageView imageView = new ImageView(new Image("gui/assets/item_tiles/" + sb.toString()));
                imageView.setFitHeight(42);
                imageView.setFitWidth(42);
                GridPane.setConstraints(imageView, x, y);
                gridPane.getChildren().add(imageView);
            }
            
        }
        
    }
}



