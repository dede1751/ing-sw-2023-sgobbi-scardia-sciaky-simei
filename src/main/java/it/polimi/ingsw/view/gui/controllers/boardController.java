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

public class boardController {
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private void boardButton(ActionEvent event)  {
        System.out.println("CIAO");
        
        Board board = new Board(3);
        TileBag tileBag = new TileBag();
        board.refill(tileBag);
        updateBoard(board);
        
        
        Button button = (Button) event.getSource();
        int row = GridPane.getRowIndex(button);
        int column = GridPane.getColumnIndex(button);
        
        button.setDisable(true);
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
                imageView.setFitHeight(44);
                imageView.setFitWidth(44);
                GridPane.setConstraints(imageView, x, y);
                gridPane.getChildren().add(imageView);
            }
            
        }
        
    }
}



