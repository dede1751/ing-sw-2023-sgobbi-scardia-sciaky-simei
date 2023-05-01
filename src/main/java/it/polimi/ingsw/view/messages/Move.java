package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Tile;

import java.io.Serializable;
import java.util.List;

public record Move(List<Coordinate> selection, List<Tile> tiles, int column) implements Serializable {}


