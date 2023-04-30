package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.network.Response;
import it.polimi.ingsw.utils.mvc.IntegrityChecks;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.JoinLobby;
import it.polimi.ingsw.view.messages.LobbyInformation;
import it.polimi.ingsw.view.messages.Move;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TUI extends View {
    
    private final Object LobbyRequestLock = new Object();
    
    List<LobbyController.LobbyView> lobbies;
    
    @Override
    public void run() {
        userLogin();
        
        //noinspection InfiniteLoopStatement
        while( true ) {
            askPassTurn();
            
            //FIXME getviewid nonè ciò che è inteso nel seguente codice
            /*if(this.getViewID()==modelView.getCurrentPlayerIndex()){
                Player currentPlayer = modelView.getPlayers().get(modelView.getCurrentPlayerIndex());
                System.out.println("my score is "+ currentPlayer.getScore());
                printBoard(modelView.getBoard());
                printShelf(currentPlayer.getShelf());
                askSelection(modelView);            //set the asked selection to the view message selection
                askColumn(modelView);
                this.setChangedAndNotifyObservers(Action.MOVE);
            }*/
        }
    }
    
    private void userLogin() {
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
        
        // ask the user to create or join a lobby
        System.out.println("\nDo you want to create your own lobby or join an existing one? [CREATE/JOIN]");
        while( true ) {
            System.out.print("\n>>  ");
            String choice = scanner.next().trim();
            
            if( choice.equals("CREATE") ) {
                // ask the user for the number of players in the lobby.
                System.out.println("\nChoose the amount of players for the match (2-4): ");
                int lobbySize = -1;
                while( !(lobbySize >= 2 && lobbySize <= 4) ) {
                    System.out.print("\n>>  ");
                    try {
                        lobbySize = Integer.parseInt(scanner.next());
                    }
                    catch( NumberFormatException e ) {
                        System.out.println("Please write a number");
                    }
                }
                Response r = notifyCreateLobby(new LobbyInformation(lobbySize, this.getNickname()));
                System.out.println(r);
                break;
            }else if( choice.equals("JOIN") ) {
                
                //FIXME SUPER SKETCHY!!!
                if( this.service ) {
                    Response r = notifyRequestLobby(new LobbyInformation(null, null));
                    System.out.println(r);
                    synchronized(LobbyRequestLock) {
                        while( !model.isChangedLobby() ) {
                            try {
                                LobbyRequestLock.wait();
                            }
                            catch( InterruptedException ignored ) {
                            }
                        }
                        model.toggleHasChangedLobby();
                    }
                }else {
                    Response r = notifyRequestLobby(new LobbyInformation(null, null));
                    System.out.println(r);
                }
                if( lobbies.isEmpty() ) {
                    System.out.println("No lobbies are currently available, please create a new one");
                }else {
                    
                    while( true ) {
                        try {
                            for( LobbyController.LobbyView lobby : lobbies ) {
                                System.out.print(lobby);
                            }
                            System.out.println("\nChose the lobby identifier");
                            System.out.print("\n>>  ");
                            int lobbyId = Integer.parseInt(scanner.next());
                            //TODO to fix to incorporate lobby name
                            Optional<LobbyController.LobbyView> name =
                                    lobbies.stream().filter((x) -> x.lobbyID() == lobbyId).findFirst();
                            if( name.isPresent() ) {
                                this.notifyJoinLobby(new JoinLobby("", lobbyId));
                                break;
                            }else {
                                System.out.println("Please select a valid lobby identifier");
                            }
                        }
                        catch( NumberFormatException e ) {
                            System.out.println("Please write a number");
                        }
                        
                    }
                    break;
                }
            }else {
                System.out.println("Please choose one of [CREATE/JOIN]");
            }
        }
        
        System.out.println("\nSuccesfully logged in to server. Awaiting game start... ");
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
    
    private void printBoard(Board board) {
        
        var def = "C-,-)";
        if( board == null ) {
            System.out.println("Board Still not initialized!");
            for( int i = 8; i >= 0; i-- ) {
                for( int j = 0; j < 9; j++ ) {
                    System.out.print(def + ",");
                }
                System.out.print("\n");
            }
        }else {
            var matrix = board.getAsMatrix();
            for( int i = 8; i >= 0; i-- ) {
                for( int j = 0; j < 9; j++ ) {
                    if( matrix[i][j] == null ) {
                        System.out.print(def + ",");
                    }else {
                        System.out.print(matrix[i][j].toString() + ",");
                    }
                }
                System.out.print("\n");
            }
        }
    }
    
    //TODO
    private void printShelf(Shelf shelf) {
    
    }
    
    //FIXME
    @SuppressWarnings("unused")
    @Override
    public void onMessage(BoardMessage msg) {
        this.model.setBoard(msg.getPayload());
        printBoard(this.model.getBoard());
    }
    
    //FIXME sketchy part 3
    @SuppressWarnings("unused")
    @Override
    public void onMessage(AvailableLobbyMessage msg) {
        this.lobbies = msg.getPayload().lobbyViewList();
        synchronized(LobbyRequestLock) {
            model.toggleHasChangedLobby();
            LobbyRequestLock.notifyAll();
        }
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
