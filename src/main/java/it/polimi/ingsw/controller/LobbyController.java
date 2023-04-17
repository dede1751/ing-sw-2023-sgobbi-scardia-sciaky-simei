package it.polimi.ingsw.controller;

import it.polimi.ingsw.view.ViewMessage;

import java.util.*;

public class LobbyController {
    
    private static LobbyController INSTANCE;
    
    public record Lobby(List<String> nicknames, List<Integer> clientIDs, int lobbySize, int lobbyID) {};
    
    private final HashMap<Integer, Lobby> lobbies;
    
    private int lobbyIDCounter;
    
    private LobbyController() {
        this.lobbies = new HashMap<>();
        this.lobbyIDCounter = 0;
    }
    
    public static LobbyController getInstance() {
        if ( INSTANCE == null ) {
            INSTANCE = new LobbyController();
        }
        
        return INSTANCE;
    }
    
    public Lobby getLobby(int lobbyID) { return this.lobbies.get(lobbyID); }
    
    public boolean availableNickname(String nickname) {
        return lobbies.values()
                .stream()
                .noneMatch((l) -> l.nicknames.contains(nickname));
    }
    
    public boolean availableLobby() {
        return lobbies.values()
                .stream()
                .anyMatch((l) -> l.nicknames.size() < l.lobbySize);
    }
    
    public boolean isFull(int lobbyID) {
        Lobby lobby = lobbies.get(lobbyID);
        return lobby.nicknames.size() >= lobby.lobbySize;
    }
    
    public void endGame(int lobbyID) {
        this.lobbies.remove(lobbyID);
    }
    
    public int createLobby(ViewMessage msg) {
        Lobby newLobby = new Lobby(
                new ArrayList<>(List.of(msg.getNickname())),
                new ArrayList<>(List.of(msg.getClientID())),
                msg.getLobbySize(),
                lobbyIDCounter
        );
        lobbies.put(lobbyIDCounter, newLobby);
        lobbyIDCounter++;
        
        return newLobby.lobbyID;
    }
    
    public int joinLobby(ViewMessage msg) {
        Lobby lobby = this.lobbies.values()
                .stream()
                .filter((l) -> l.nicknames.size() < l.lobbySize)
                .findFirst()
                .orElseThrow();
        
        lobby.nicknames.add(msg.getNickname());
        lobby.clientIDs.add(msg.getClientID());
        return lobby.lobbyID;
    }
    
}
