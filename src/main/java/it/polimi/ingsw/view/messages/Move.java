package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;

import java.io.Serializable;
import java.util.List;

/**
 * Payload for {@link MoveMessage}. Represents a full "move" by a player during their turn.
 *
 * @param selection List of coordinates of the tiles selected by the player
 * @param tiles     Ordered list of tiles selected by the player
 * @param column    Column where the player wants to place the tiles
 */
public record Move(List<Coordinate> selection, List<Tile> tiles, int column) implements Serializable {
}
