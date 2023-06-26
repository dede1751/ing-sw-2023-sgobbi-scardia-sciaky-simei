package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Shelf;

/**
 * Message containing a single Shelf
 */
public class ShelfMessage extends ModelMessage<Shelf> {
    
    private final String player;
    
    /**
     * Initialize a new ShelfMessage object with the given Shelf and player
     * @param p The Shelf
     * @param player The player linked to the shelf
     */
    public ShelfMessage(Shelf p, String player) {
        super(p);
        this.player = player;
    }
    
    /**
     * Get the player linked to the shelf
     * @return The player's nickname
     */
    public String getPlayer() {
        return this.player;
    }
}
