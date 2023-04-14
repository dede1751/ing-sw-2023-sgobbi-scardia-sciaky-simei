package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelView;
import it.polimi.ingsw.network.Client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LobbyController {
    
    public enum State {
        INITIALIZE_LOBBY,
        JOIN_LOBBY,
        FULL_LOBBY,
    }
    
    public record LobbyInfo(List<String> nicknames, State lobbyState) implements Serializable {};
    
    public record LoginInfo(String nickname, int lobbySize) implements Serializable {};
    
    private State state;
    
    private final List<String> nicknames;
    
    private final List<Client> clients;
    
    private Client servedClient;
    
    private int lobbySize = 0;
    
    public LobbyController() {
        state = State.INITIALIZE_LOBBY;
        nicknames = new ArrayList<>();
        clients = new ArrayList<>();
    }
    
    public void addClient(Client client) {
        try {
            servedClient = client;
            client.sendLobbyInfo(new LobbyInfo(new ArrayList<>(nicknames), state));
        } catch( RemoteException e ) {
            System.err.println("Unable to update the client: " + e.getMessage() + ". Skipping the update...");
        }
    }
    
    public boolean login(LoginInfo info) {
        if ( state != State.FULL_LOBBY ) {
            try {
                servedClient.setViewID(clients.size());
                nicknames.add(info.nickname());
                clients.add(servedClient);
            } catch( RemoteException e ) {
                System.err.println("Unable to set the client's ID: " + e.getMessage() + ". Skipping the login...");
                return false;
            }
        }
        
        switch ( state ) {
            case INITIALIZE_LOBBY -> {
                lobbySize = info.lobbySize();
                state = State.JOIN_LOBBY;
                return true;
            }
            case JOIN_LOBBY -> {
                state = lobbySize > clients.size() ? State.JOIN_LOBBY : State.FULL_LOBBY;
                return true;
            }
            default -> { return false; }
        }
    }
    
    public boolean isFull() { return state == State.FULL_LOBBY; }
    
    private int[] randDistinctIndices(int count) {
        return new Random().ints(0, 12)
                .distinct()
                .limit(count)
                .toArray();
    }
    
    public GameController initGame() {
        int[] commonGoalIndices = randDistinctIndices(2);
        GameModel model = new GameModel(lobbySize, commonGoalIndices[0], commonGoalIndices[1]);
        model.getBoard().refill(model.getTileBag());
        
        for (Client client: clients) {
            model.addObserver((o, evt) -> {
                try {
                    client.update(new GameModelView(model), evt);
                } catch ( RemoteException e) {
                    System.err.println("Unable to update the client: " + e.getMessage() + ". Skipping the update...");
                }
            });
        }
        
        int[] personalGoalIndices = randDistinctIndices(4);
        for (int i = 0; i < lobbySize; i++) {
            model.addPlayer(nicknames.get(i), personalGoalIndices[i]);
        }
        
        return new GameController(model, clients);
    }
    
}
