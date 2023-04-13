package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TUI extends View {
    
    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while( true ) {
            this.askPassTurn();
        }
    }
    
    @Override
    public LobbyController.LoginInfo userLogin(LobbyController.LobbyInfo info) {
        String nickname = askNickname(info.nicknames());
        int lobbySize = 0;
        
        if( info.lobbyState() == LobbyController.State.INITIALIZE_LOBBY ) {
            lobbySize = askLobbySize();
        }
        
        return new LobbyController.LoginInfo(nickname, lobbySize);
    }
    
    private String askNickname(List<String> others) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a nickname to add to the game, the following have already been taken:\n");
        
        for( String s : others ) {
            System.out.print(s + " ");
        }
        System.out.println();
        
        while( true ) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if( !others.contains(input) ) {
                return input;
            }
        }
    }
    
    private int askLobbySize() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the amount of players for the match (2-4): ");
        
        while( true ) {
            System.out.print("\n>>  ");
            int input = Integer.parseInt(scanner.next());
            
            if( input >= 2 && input <= 4 ) {
                return input;
            }
        }
    }
    
    private void askPassTurn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write 'PASS' to pass your turn: ");
        
        while( true ) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            
            if( input.equals("PASS") ) {
                this.setChangedAndNotifyObservers(Action.PASS_TURN);
                break;
            }
        }
    }
    
    
    public void askSelection(GameModelView model) {
        Scanner scanner = new Scanner(System.in);
        List<Coordinate> selection = new ArrayList<Coordinate>();
        
        System.out.print("Enter number of coordinates (1-3): ");
        int numCoordinates = scanner.nextInt();
        scanner.nextLine();
        
        if( numCoordinates > 3 || numCoordinates < 1 ) {//check if the number of coordinates is right
            System.out.print("The number must be between 1 and 3, try again: ");
            numCoordinates = scanner.nextInt();
            scanner.nextLine();
        }
        
        
        //le coordinate devono essere da 0 a 8
        
        
        for( int i = 0; i < numCoordinates; i++ ) {
            System.out.print("Enter x-coordinate: ");
            int x = scanner.nextInt();
            
            System.out.print("Enter y-coordinate: ");
            int y = scanner.nextInt();
            
            selection.add(new Coordinate(x, y));
            scanner.nextLine(); // consume the newline character
        }
        
        System.out.println("Entered coordinates: " + selection);
        
    }
    
    private void printBoard(Board board) {
        for( int i = 0; i < 8; i++ ) {
            
        }
    }
    
    @Override
    public void update(GameModelView model, GameModel.Event evt) {
        switch( evt ) {
            case GAME_START -> {
                System.out.println("The game has started!");
                new Thread(this).start();
            }
            case NEW_CURRENT_PLAYER -> {
                System.out.println("Current player has changed to " + model.getCurrentPlayerIndex() + ". Players: ");
                
                for( Player player : model.getPlayers() ) {
                    System.out.print(player.getNickname() + " ");
                }
                
                
                printBoard(model.getBoard());
                askSelection(model);
                System.out.println();
            }
            case FINISHED_GAME -> System.out.println("GAME OVER");
            default -> System.err.println("Ignoring event from " + model + ": " + evt);
        }
    }
    
    
}
