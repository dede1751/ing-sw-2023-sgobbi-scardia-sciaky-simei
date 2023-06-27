package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.view.LocalModel;
import it.polimi.ingsw.view.gui.GUIApp;
import it.polimi.ingsw.view.gui.GUIUtils;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller class for the board view in the GUI application.
 * It handles various methods and event handlers related to the board.
 */
public class BoardController {
    /**
     * The HBox element representing the selection area.
     */
    @FXML
    public HBox selection;
    /**
     * The ImageView element for the common goal X stack.
     */
    @FXML
    private ImageView commonGoalXStack;
    /**
     * The ImageView element for the common goal Y stack.
     */
    @FXML
    private ImageView commonGoalYStack;
    /**
     * The ImageView element for the common goal X.
     */
    @FXML
    private ImageView commonGoalX;
    /**
     * The ImageView element for the common goal Y.
     */
    @FXML
    private ImageView commonGoalY;
    
    /**
     * Constructs a new BoardController.
     */
    public BoardController() {
        // Default constructor
    }
    
    /**
     * Sets the image for the common goal X stack based on the given ID.
     *
     * @param id The ID of the common goal X stack.
     *           Note: The ID should be 0-based.
     *           The image is loaded from "gui/assets/common_goal_cards/{id + 1}.png".
     */
    public void setCommonGoalX(int id) {
        id = id + 1;
        this.commonGoalXStack.setImage(new Image("gui/assets/common_goal_cards/" + id + ".png"));
    }
    /**
     * Sets the image for the common goal Y stack based on the given ID.
     *
     * @param id The ID of the common goal Y stack.
     *           Note: The ID should be 0-based.
     *           The image is loaded from "gui/assets/common_goal_cards/{id + 1}.png".
     */
    public void setCommonGoalY(int id) {
        id = id + 1;
        this.commonGoalYStack.setImage(new Image("gui/assets/common_goal_cards/" + id + ".png"));
    }
    /**
     * Sets the image for the common goal X based on the given score.
     *
     * @param score The score of the common goal X.
     *              The image is loaded from "gui/assets/scoring_tokens/scoring_{score}.png".
     */
    public void setCommonGoalXStack(int score) {
        this.commonGoalX.setImage(new Image("gui/assets/scoring_tokens/scoring_" + score + ".png"));
    }
    /**
     * Sets the image for the common goal Y based on the given score.
     *
     * @param score The score of the common goal Y.
     *              The image is loaded from "gui/assets/scoring_tokens/scoring_{score}.png".
     */
    public void setCommonGoalYStack(int score) {
        this.commonGoalY.setImage(new Image("gui/assets/scoring_tokens/scoring_" + score + ".png"));
    }
    /**
     * Represents a tile element consisting of a button and an image view.
     */
    private record TileElement(Button button, ImageView imageView) {
    }
    /**
     * A mapping of coordinates to tile elements on the board.
     */
    private final Map<Coordinate, TileElement> boardMap = new HashMap<>();
    /**
     * The list of currently selected coordinates on the board.
     */
    private final List<Coordinate> selected = new ArrayList<>();
    /**
     * The background style for selected tiles.
     */
    private final Background selectedBackground =
            new Background(new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(1), new Insets(1)));
    /**
     * The background style for tiles when the mouse is over them.
     */
    private final Background mouseOverBackgound =
            new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(1), new Insets(1)));
    /**
     * The GridPane element representing the board grid.
     */
    @FXML
    private GridPane gridPane;
    //buttons' name follow the pattern : button<x><y>
    /**
     * Initializes the board view by setting up buttons and images for the board grid.
     */
    @FXML
    private void initialize() {
        selection.setSpacing(30);
        for( int row = 0; row < Board.MAX_SIZE; row++ ) {
            for( int col = 0; col < Board.MAX_SIZE; col++ ) {
                
                //setting buttons
                Button button = new Button(row + "," + col);
                button.setOnAction(this::boardButton);
                button.setOnMouseEntered(this::onMouseOverEnter);
                button.setOnMouseExited(this::onMouseOverExit);
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setOpacity(0);
                
                //setting images
                ImageView imageView = new ImageView();
                boardMap.put(new Coordinate(row, col), new TileElement(button, imageView));
                //set dimensions
                imageView.setFitHeight(42);
                imageView.setFitWidth(42);
                
                //add image and buttons
                gridPane.add(imageView, col, row);
                gridPane.add(button, col, row);
            }
        }
    }
    /**
     * Handles the mouse enter event when the mouse is over a board button.
     * Changes the background and opacity of the button.
     *
     * @param event The MouseEvent triggered by the mouse entering the button.
     */
    @FXML
    private void onMouseOverEnter(MouseEvent event) {
        Button button = (Button) event.getSource();
        Coordinate coord = new Coordinate(GridPane.getRowIndex(button), GridPane.getColumnIndex(button));
        if( selected.contains(coord) ||
            LocalModel.getInstance().getBoard().getTile(GUIUtils.modelCoordinateTrasform(coord, Board.MAX_SIZE)) ==
            null )
            return;
        button.setBackground(mouseOverBackgound);
        button.setOpacity(0.35);
    }
    /**
     * Handles the mouse exit event when the mouse leaves a board button.
     * Restores the background and opacity of the button.
     *
     * @param event The MouseEvent triggered by the mouse exiting the button.
     */
    private void onMouseOverExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        Coordinate coord = new Coordinate(GridPane.getRowIndex(button), GridPane.getColumnIndex(button));
        if( selected.contains(coord) ||
            LocalModel.getInstance().getBoard().getTile(GUIUtils.modelCoordinateTrasform(coord, Board.MAX_SIZE)) ==
            null )
            return;
        button.setBackground(null);
        button.setOpacity(0);
    }
    /**
     * Handles the click event on a board button.
     * Updates the selected tiles and their appearance on the board.
     *
     * @param event The Event triggered by clicking the board button.
     */
    @FXML
    private void boardButton(Event event) {
        
        Button button = (Button) event.getSource();
        Coordinate coord = new Coordinate(GridPane.getRowIndex(button), GridPane.getColumnIndex(button));
        Tile tile =
                LocalModel.getInstance().getBoard().getTile(GUIUtils.modelCoordinateTrasform(coord, Board.MAX_SIZE));
        if( tile == null || tile.equals(Tile.NOTILE) )
            return;
        if( selected.contains(coord) ) {
            button.setBackground(null);
            button.setOpacity(0);
            selected.remove(coord);
            List<Coordinate> fixedCoord =
                    selected.stream().map((x) -> new Coordinate(Board.MAX_SIZE - x.row() - 1, x.col())).toList();
            GUIApp.setCoordinateSelection(fixedCoord);
            GUIApp.setTileSelection(
                    fixedCoord.stream().map((x) -> LocalModel.getInstance().getBoard().getTile(x)).toList()
            );
            updateSelected();
        }else {
            if( selected.size() == 3 )
                return;
            selected.add(coord);
            List<Coordinate> fixedCoord =
                    selected.stream().map((x) -> new Coordinate(Board.MAX_SIZE - x.row() - 1, x.col())).toList();
            GUIApp.setCoordinateSelection(fixedCoord);
            GUIApp.setTileSelection(
                    fixedCoord.stream().map((x) -> LocalModel.getInstance().getBoard().getTile(x)).toList()
            );
            updateSelected();
            button.setBackground(selectedBackground);
            button.setOpacity(0.4);
        }
    }
    
    /**
     * Updates the selected tiles in the selection area.
     */
    private void updateSelected() {
        selection.getChildren().clear();
        for( var x : selected ) {
            ImageView image = new ImageView(boardMap.get(x).imageView().getImage());
            image.setPreserveRatio(true);
            image.setFitWidth(60.0);
            
            selection.getChildren().add(image);
        }
    }
    
    /**
     * Resets the selected tiles and clears the selection area.
     */
    public void resetSelected() {
        selection.getChildren().clear();
        for( var x : selected ) {
            boardMap.get(x).button().setOpacity(0);
            boardMap.get(x).button().setBackground(null);
        }
        GUIApp.setCoordinateSelection(null);
        GUIApp.setTileSelection(null);
        selected.clear();
    }
    
    /**
     * Updates the board view with the tiles from the given board object.
     *
     * @param board The Board object containing the tiles to update.
     */
    public void updateBoard(Board board) {
        GUIUtils.threadPool
                .submit(() -> {
                            Map<Coordinate, Tile> tiles = board.getTiles();
                            for( Coordinate coordinate : tiles.keySet() ) {
                                StringBuilder sb = new StringBuilder();
                                boolean noTileFlag = false;
                                // Access each coordinate
                                int x = coordinate.row();
                                int y = coordinate.col();
                                //select correct image
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
                                
                                Coordinate fixedCoord = new Coordinate(-(x - Board.MAX_SIZE + 1), y);
                                ImageView image = boardMap.get(fixedCoord).imageView();
                                boolean finalNoTileFlag = noTileFlag;
                                //Update images
                                Platform.runLater(() -> {
                                    if( !finalNoTileFlag ) {
                                        image.setImage(new Image("gui/assets/item_tiles/" + sb));
                                        image.setFitHeight(42);
                                        image.setFitWidth(42);
                                    }else {
                                        image.setImage(null);
                                    }
                                });
                            }
                        }
                );
    }
}



