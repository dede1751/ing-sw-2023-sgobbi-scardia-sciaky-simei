package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Shelf;

public class ShelfMessage extends ModelMessage<Shelf> {
    
    private final String player;
    public ShelfMessage(Shelf p, String player) {
        super(p);
        this.player = player;
    }
    public String getPlayer(){
        return this.player;
    }
}
