package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.view.LocalModel;
import it.polimi.ingsw.view.gui.GUIApp;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardController {
    
    @FXML
    public HBox selection;
    
    private record TileElement(Button button, ImageView imageView) {
    }
    
    private final Map<Coordinate, TileElement> boardMap = new HashMap<>();
    private final List<Coordinate> selected = new ArrayList<>();
    
    private final Background selectedBackground =
            new Background(new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(1), new Insets(1)));
    private final Background mouseOverBackgound =
            new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(1), new Insets(1)));
    
    @FXML
    private GridPane gridPane;
    //buttons' name follow the pattern : button<x><y>
    
    @FXML
    private void initialize() {
        selection.setSpacing(30);
        for( int row = 0; row < Board.maxSize; row++ ) {
            for( int col = 0; col < Board.maxSize; col++ ) {
                
                Button button = new Button(row + "," + col);
                button.setOnAction(this::boardButton);
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setOpacity(0);
                ImageView imageView = new ImageView();
                boardMap.put(new Coordinate(row, col), new TileElement(button, imageView));
                imageView.setFitHeight(42);
                imageView.setFitWidth(42);
                gridPane.add(imageView, col, row);
                gridPane.add(button, col, row);
            }
        }
    }
    
    @FXML
    private void boardButton(Event event) {
        
        Button button = (Button) event.getSource();
        Coordinate coord = new Coordinate(GridPane.getRowIndex(button), GridPane.getColumnIndex(button));
        
        if( selected.contains(coord) ) {
            button.setBackground(null);
            button.setOpacity(0);
            selected.remove(coord);
            List<Coordinate> fixedCoord =
                    selected.stream().map((x) -> new Coordinate(Board.maxSize - x.row() - 1, x.col())).toList();
            GUIApp.setCoordinateSelection( fixedCoord);
            GUIApp.setTileSelection(
                    fixedCoord.stream().map((x) -> LocalModel.getInstance().getBoard().getTile(x)).toList()
            );
            updateSelected();
        }else {
            if( selected.size() == 3 )
                return;
            selected.add(coord);
            List<Coordinate> fixedCoord =
                    selected.stream().map((x) -> new Coordinate(Board.maxSize - x.row() - 1, x.col())).toList();
            GUIApp.setCoordinateSelection( fixedCoord);
            GUIApp.setTileSelection(
                    fixedCoord.stream().map((x) -> LocalModel.getInstance().getBoard().getTile(x)).toList()
            );
            updateSelected();
            button.setBackground(selectedBackground);
            button.setOpacity(0.4);
        }
        System.out.println("Pressed " + coord.row() + coord.col());
        
    }
    
    private void updateSelected() {
        selection.getChildren().clear();
        for( var x : selected ) {
            ImageView image = new ImageView(boardMap.get(x).imageView().getImage());
            image.setPreserveRatio(true);
            image.setFitWidth(60.0);
            
            selection.getChildren().add(image);
        }
    }
    
    public void resetSelected(){
        selection.getChildren().clear();
        for(var x : selected){
            boardMap.get(x).button().setOpacity(0);
            boardMap.get(x).button().setBackground(null);
        }
        GUIApp.setCoordinateSelection(null);
        GUIApp.setTileSelection(null);
        selected.clear();
    }
    
    
    public void updateBoard(Board board) {
        Map<Coordinate, Tile> tiles = board.getTiles();
        for( Coordinate coordinate : tiles.keySet() ) {
            StringBuilder sb = new StringBuilder();
            boolean noTileFlag = false;
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
            Coordinate fixedCoord = new Coordinate(-(x - Board.maxSize + 1), y);
            ImageView image = boardMap.get(fixedCoord).imageView();
            if( !noTileFlag ) {
                image.setImage(new Image("gui/assets/item_tiles/" + sb));
                image.setFitHeight(42);
                image.setFitWidth(42);
            }else{
                image.setImage(null);
            }
            
        }
        
    }
}



