package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * The GUIUtils class provides utility methods for the GUI application.
 */
public class GUIUtils {
    
    /**
     * Constructs a new GUIUtils instance.
     */
    public GUIUtils() {
 
    }
    
    /**
     * The thread pool for executing tasks.
     */
    public static final ExecutorService threadPool = Executors.newCachedThreadPool();
    /**
     * Transforms the given coordinate for GUI representation.
     *
     * @param coordinate   The coordinate to transform.
     * @param maxColSize   The maximum column size.
     * @return The transformed coordinate.
     */
    public static Coordinate guiCoordinateTransform(Coordinate coordinate, int maxColSize) {
        return new Coordinate(-(coordinate.row() - maxColSize + 1), coordinate.col());
    }
    /**
     * Transforms the given coordinate for model representation.
     *
     * @param coordinate   The coordinate to transform.
     * @param maxColSize   The maximum column size.
     * @return The transformed coordinate.
     */
    public static Coordinate modelCoordinateTrasform(Coordinate coordinate, int maxColSize) {
        return new Coordinate(maxColSize - coordinate.row() - 1, coordinate.col());
    }
    /**
     * Updates the shelf tiles images with the provided tiles .
     *
     * @param shelf       The shelf to update.
     * @param imageMap    The map of coordinates and image views.
     */
    public static void updateShelf(Shelf shelf, Map<Coordinate, ImageView> imageMap) {
        if( shelf != null ) {
            var matrix = shelf.getAllShelf();
            
            for( int i = Shelf.N_ROW - 1; i >= 0; i-- ) {
                for( int j = 0; j < Shelf.N_COL; j++ ) {
                    if( matrix[i][j] != null ) {
                        Coordinate guiCoord = GUIUtils.guiCoordinateTransform(new Coordinate(i, j), Shelf.N_ROW);
                        ImageView image = imageMap.get(guiCoord);
                        
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
                        Platform.runLater(() -> image.setImage(new Image("gui/assets/item_tiles/" + sb)));
                    }
                }
                
            }
            
        }
    }
}
