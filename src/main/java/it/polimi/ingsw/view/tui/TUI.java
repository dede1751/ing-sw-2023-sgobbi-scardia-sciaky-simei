package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Scanner;

public class TUI extends View {
    
    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            this.askPassTurn();
        }
    }
    
    @Override
    public LobbyController.LoginInfo userLogin(LobbyController.LobbyInfo info) {
        String nickname = askNickname(info.nicknames());
        int lobbySize = 0;
        
        if ( info.lobbyState() == LobbyController.State.INITIALIZE_LOBBY) {
            lobbySize = askLobbySize();
        }
        
        return new LobbyController.LoginInfo(nickname, lobbySize);
    }
    
    private String askNickname(List<String> others) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a nickname to add to the game, the following have already been taken:\n");
        
        for (String s: others) {
            System.out.print(s + " ");
        }
        System.out.println();
        
        while (true) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if (!others.contains(input)) {
                return input;
            }
        }
    }
    
    private int askLobbySize() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the amount of players for the match (2-4): ");
        
        while (true) {
            System.out.print("\n>>  ");
            int input = Integer.parseInt(scanner.next());
            
            if (input >= 2 && input <= 4) {
                return input;
            }
        }
    }
    
    private void askPassTurn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write 'PASS' to pass your turn: ");
        
        while (true) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if ( input.equals("PASS") ) {
                this.setChangedAndNotifyObservers(Action.PASS_TURN);
                break;
            }
        }
    }
    
    @Override
    public void update(GameView model, GameModel.Event evt) {
        switch (evt) {
            case GAME_START -> {
                System.out.println("The game has started!");
                new Thread(this).start();
            }
            case NEW_CURRENT_PLAYER -> {
                System.out.println("Current player has changed to " + model.getCurrentPlayerIndex() + ". Players: ");
                for ( Player player: model.getPlayers() ) {
                    System.out.print(player.getNickname() + " ");
                }
                System.out.println();
            }
            case FINISHED_GAME -> System.out.println("GAME OVER");
            default -> System.err.println("Ignoring event from " + model + ": " + evt);
        }
    }
    
}
