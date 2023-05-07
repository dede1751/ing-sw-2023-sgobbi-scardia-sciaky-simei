package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.utils.exceptions.InvalidStringException;
import it.polimi.ingsw.utils.mvc.IntegrityChecks;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.Move;

import java.util.*;

public class TUI extends View {
    
    @Override
    public void run() {
        userLogin();
        
        //noinspection InfiniteLoopStatement
        while( true ) {
            askPassTurn();
        }
    }
    
    private void userLogin() {
        Scanner scanner = new Scanner(System.in);
        
        // fetch all lobbies
        notifyRequestLobby(null);
        if( !lobbies.isEmpty() ) {
            System.out.println("\nHere are all the currently available lobbies. Avoid stealing someone's name!");
            System.out.println("To recover crashed lobbies, user your old nickname!");
            lobbies.forEach(System.out::print);
        }
        askNickname();
        
        label:
        while( true ) {
            System.out.println(
                    "\nDo you want to create your own lobby, join an existing one or recover a crashed lobby? [CREATE/JOIN/RECOVER]");
            System.out.print("\n>>  ");
            String choice = scanner.next().trim();
            
            switch( choice ) {
                
                case "CREATE" -> {
                    
                    int lobbySize = -1;
                    System.out.println("\nChoose the amount of players for the match (2-4): ");
                    while( !(lobbySize >= 2 && lobbySize <= 4) ) {
                        System.out.print("\n>>  ");
                        try {
                            lobbySize = Integer.parseInt(scanner.next());
                        }
                        catch( NumberFormatException e ) {
                            System.out.println("Please write a number");
                        }
                    }
                    
                    Response r = notifyCreateLobby(lobbySize);
                    if( r.isOk() ) {
                        break label;
                    }else if( r.msg().equals("Nickname Taken") ) {
                        System.out.println("Your nickname has been taken!. Please choose another one");
                        askNickname();
                    }
                    
                }
                
                case "JOIN" -> {
                    notifyRequestLobby(null);
                    
                    if( lobbies.stream().noneMatch((l) -> l.nicknames().size() < l.lobbySize()) ) {
                        System.out.println("\nNo lobbies are currently available, please create a new one");
                        continue;
                    }
                    
                    Response r;
                    System.out.println("\nChoose one of the following lobbies (avoid ones that are full): ");
                    lobbies.forEach(System.out::print);
                    while( true ) {
                        System.out.print("\n>>  ");
                        try {
                            int lobbyId = Integer.parseInt(scanner.next());
                            if( lobbies.stream().anyMatch(
                                    (l) -> l.lobbyID() == lobbyId && l.nicknames().size() < l.lobbySize()) ) {
                                r = notifyJoinLobby(lobbyId);
                                break;
                            }else {
                                System.out.println("Please select a valid lobby identifier");
                            }
                        }
                        catch( NumberFormatException e ) {
                            System.out.println("Please write a number");
                        }
                    }
                    
                    if( r.isOk() ) {
                        break label;
                    }else if( r.msg().equals("Nickname Taken") ) {
                        System.out.println("Your nickname has been taken!. Please choose another one");
                        askNickname();
                    }else if( r.msg().equals("Lobby Unavailable") ) {
                        System.out.println("The lobby you chose is no longer available. Please choose another one");
                    }
                    
                }
                
                case "RECOVER" -> {
                    Response r = notifyRecoverLobby();
                    
                    if( r.isOk() ) {
                        break label;
                    }else if( r.msg().equals("Lobby Unavailable") ) {
                        System.out.println("There is no lobby you can recover. Did you misspell your nickname?");
                        askNickname();
                    }
                }
                
                default -> System.out.println("Please choose one of [CREATE/JOIN/RECOVER]");
            }
        }
        System.out.println("\nSuccesfully logged in to server. Awaiting game start... ");
    }
    
    private void askNickname() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nChoose the nickname you'll be using in game:");
        
        while( true ) {
            System.out.print("\n>>  ");
            String nickname = scanner.next().trim();
            
            if( !nickname.equals("") ) {
                this.setNickname(nickname);
                break;
            }
        }
    }
    
    private void askPassTurn() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWrite 'PASS' to pass your turn: ");
        
        while( true ) {
            System.out.print("\n>>  ");
            String input = scanner.next().trim();
            if( input.equals("PASS") ) {
                var message = this.notifyDebugMessage("Turn Passed");
                System.out.println(message);
                break;
                
            }
        }
    }
    
    //TODO add tile order selection
    public List<Coordinate> askSelection(Move move) {
        
        Scanner scanner = new Scanner(System.in);
        List<Coordinate> selection = new ArrayList<>();
        System.out.print("Enter number of coordinates (1-3): ");
        int numCoordinates = scanner.nextInt();
        scanner.nextLine();
        
        while( numCoordinates > 3 || numCoordinates < 1 ) {//check if the number of coordinates is right
            System.out.print("The number must be between 1 and 3, try again: ");
            numCoordinates = scanner.nextInt();
            scanner.nextLine();
        }
        
        //le coordinate devono essere da 0 a 8
        do {
            for( int i = 0; i < numCoordinates; i++ ) {
                Coordinate coordinate = getCoordinate();
                boolean validCoordinate = false;
                while( !validCoordinate ) {
                    if( model.getBoard().getTile(coordinate) == Tile.NOTILE ||
                        model.getBoard().getTile(coordinate) == null ) {
                        validCoordinate = true;
                    }
                    coordinate = getCoordinate();
                }
                
                selection.add(getCoordinate());
                scanner.nextLine(); // consume the newline character
                
            }
        }
        while( IntegrityChecks.checkSelectionForm(selection) );
        System.out.println("Entered coordinates: " + selection);
        return selection;
        
    }
    
    public int askColumn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the column in which you want to place your selection: ");
        int column = scanner.nextInt();
        scanner.nextLine();
        return column;
    }
    
    private Coordinate getCoordinate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter x-coordinate: ");
        int x = scanner.nextInt();
        
        System.out.print("Enter y-coordinate: ");
        int y = scanner.nextInt();
        return new Coordinate(x, y);
    }
    
    
    @SuppressWarnings("unused")
    @Override
    public void onMessage(BoardMessage msg) throws InvalidStringException {
        this.model.setBoard(msg.getPayload());
        TUIUtils.printBoard(this.model.getBoard());
    }
    
    @SuppressWarnings("unused")
    @Override
    public void onMessage(AvailableLobbyMessage msg) {
        this.lobbies = msg.getPayload().lobbyViewList();
    }
    
    @SuppressWarnings("unused")
    @Override
    public void onMessage(EndGameMessage msg) {
        var p = msg.getPayload();
        System.out.println("GAME FINISHED, THE WINNER IS " + p.winner());
        System.out.println("LEADERBOARD : ");
        for( var x : p.points().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).toList() ) {
            System.out.println(x.getKey() + " : " + x.getValue());
        }
    }
    
    @SuppressWarnings("unused")
    @Override
    public void onMessage(StartGameMessage msg) {
        model.setPlayersNicknames(msg.getPayload().nicknames());
        System.out.println("GAME START!");
        System.out.println("Players name:");
        msg.getPayload().nicknames().forEach(System.out::println);
    }
    
}
