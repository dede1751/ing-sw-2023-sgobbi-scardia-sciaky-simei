package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;

import java.io.Serializable;

public record Response(int status, String msg) implements Serializable, Message{
    public static Response Ok() {
        return new Response(0, "OK");
    }
    
    public static Response IllegalMove(String playerNick) {
        System.err.println("Illegal move by player : " + playerNick + " will be ignored");
        return new Response(-1, "Illegal Move : ignoring player action");
    }
    
    public static Response NotCurrentPlayer(String playerNick) {
        System.err.println(playerNick + " is not the current player, this event will be ignored");
        return new Response(-1, "Not the current player : event will be ignored");
    }
    
}
