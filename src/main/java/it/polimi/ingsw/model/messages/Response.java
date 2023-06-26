package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.view.messages.CreateLobbyMessage;

import java.io.Serializable;

/**
 * Generic response returned to each client request
 *
 * @param status 0 if OK, -1 if error
 * @param msg    Message to be displayed to the client
 * @param Action The type of action that generated this response
 */
public record Response(int status, String msg, String Action) implements Serializable {
    
    /**
     * Create a new OK response
     * @param Action The type of action that generated this response
     * @return A new OK response
     */
    public static Response Ok(String Action) {
        return new Response(0, "OK", Action);
    }
    
/**
     * Create a new NicknameTaken response, notifying the client the specified nickname is not available.
     * @param Action The type of action that generated this response
     * @return A new Error response
     */
    public static Response NicknameTaken(String Action) {
        return new Response(-1, "NicknameTaken", Action);
    }
    
    /**
     * Create a new LobbyUnavailable response, notifying the client the specified lobby is not available.
     * @param Action The type of action that generated this response
     * @return A new Error response
     */
    public static Response LobbyUnavailable(String Action) {
        return new Response(-1, "LobbyUnavailable", Action);
    }
    
    /**
     * Create a new Invalid Lobby Size response.
     * @return A new Error response
     */
    public static Response InvalidLobbySize() {
        return new Response(-1, "Invalid Lobby Size", CreateLobbyMessage.class.getSimpleName());
    }
    
    /**
     * Create a new IllegalMove response, notifying the client the specified move is not valid.
     * @param playerNick The nickname of the player that sent the illegal move
     * @param Action The type of action that generated this response
     * @return A new Error response
     */
    public static Response IllegalMove(String playerNick, String Action) {
        System.err.println("Illegal move by player : " + playerNick + " will be ignored");
        return new Response(-1, "Illegal Move : ignoring player action", Action);
    }
    
    /**
     * Create a new NotCurrentPlayer response, notifying the client that he is not the current player.
     * @param playerNick The nickname of the player that sent the illegal move
     * @param Action The type of action that generated this response
     * @return A new Error response
     */
    public static Response NotCurrentPlayer(String playerNick, String Action) {
        System.err.println(playerNick + " is not the current player, this event will be ignored");
        return new Response(-1, "Not the current player : event will be ignored", Action);
    }
    
    /**
     * Create a new NicknameNull response, notifying the client that the message did not contain a nickname.
     * @param Action The type of action that generated this response
     * @return A new Error response
     */
    public static Response NicknameNull(String Action) {
        return new Response(-1, "Nickname is null", Action);
    }
    
    
    /**
     * Check if the response is OK or an error.
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
