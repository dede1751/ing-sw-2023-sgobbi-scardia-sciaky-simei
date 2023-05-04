package it.polimi.ingsw.model.messages;

import java.io.Serializable;

/**
 * Generic response returned to each client request
 *
 * @param status 0 if OK, -1 if error
 * @param msg    Message to be displayed to the client
 * @param Action The type of action that generated this response
 */
public record Response(int status, String msg, String Action) implements Serializable {
    
    public static Response Ok(String Action) {
        return new Response(0, "OK", Action);
    }
    
    public static Response NicknameTaken(String Action) {
        return new Response(-1, "NicknameTaken", Action);
    }
    
    public static Response LobbyUnavailable(String Action) {
        return new Response(-1, "LobbyUnavailable", Action);
    }
    
    public static Response IllegalMove(String playerNick, String Action) {
        System.err.println("Illegal move by player : " + playerNick + " will be ignored");
        return new Response(-1, "Illegal Move : ignoring player action", Action);
    }
    
    public static Response NotCurrentPlayer(String playerNick, String Action) {
        System.err.println(playerNick + " is not the current player, this event will be ignored");
        return new Response(-1, "Not the current player : event will be ignored", Action);
    }
    
    public static Response ServerError(String Action) {
        return new Response(-2, "Server is acting up, please be patient...", Action);
    }
    
    /**
     * @return true if the response is OK, false otherwise
     */
    public boolean isOk() {
        return status == 0;
    }
    
    @Override
    public String toString() {
        return status + ", " + msg;
    }
    
}
