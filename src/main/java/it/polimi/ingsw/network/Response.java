package it.polimi.ingsw.network;

import java.io.Serializable;

public record Response(int status, String msg, String Action) implements Serializable, Message{
    public static Response Ok(String Action) {
        return new Response(0, "OK", Action);
    }
    
    public static Response IllegalMove(String playerNick, String Action) {
        System.err.println("Illegal move by player : " + playerNick + " will be ignored");
        return new Response(-1, "Illegal Move : ignoring player action", Action );
    }
    
    public static Response NotCurrentPlayer(String playerNick, String Action) {
        System.err.println(playerNick + " is not the current player, this event will be ignored");
        return new Response(-1, "Not the current player : event will be ignored", Action);
    }
    
    @Override
    public String toString(){
        return status + "," + msg;
    }
    
}
