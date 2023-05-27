package it.polimi.ingsw.view.gui.controllers;

import it.polimi.ingsw.AppClient;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.view.gui.GUIApp;
import it.polimi.ingsw.view.messages.Move;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

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
    private GridPane localPlayerGrid;
    
    @FXML
    public void initialize() {
        
        b0.setOnAction(event -> buttonAction(0));
        b1.setOnAction(event -> buttonAction(1));
        b2.setOnAction(event -> buttonAction(2));
        b3.setOnAction(event -> buttonAction(3));
        b4.setOnAction(event -> buttonAction(4));
        
    }
    @FXML
    public void updateShelf(Shelf shelf) {
        
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
                        ImageView imageView = new ImageView(new Image("gui/assets/item_tiles/" + sb));
                        imageView.setFitHeight(60);
                        imageView.setFitWidth(60);
                        GridPane.setValignment(imageView, VPos.CENTER);
                        GridPane.setConstraints(imageView, j, -(i-Shelf.N_COL));
                        localPlayerGrid.getChildren().add(imageView);
                    }
                }
                
            }
        }
    }
    
    private void buttonAction(int column){
        AppClient.getViewInstance().notifyMove(new Move(GUIApp.getCoordinateSelection(), GUIApp.getTileSelection(), column));
    }
    
    
    public void setPersonalGoal(int id) {
        id = id+1;
        this.personalGoal.setImage(new Image("gui/assets/personal_goal_cards/Personal_Goals" + id +".png" ));
    }
}
