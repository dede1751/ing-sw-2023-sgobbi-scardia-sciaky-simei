package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.messages.*;


import it.polimi.ingsw.utils.mvc.IntegrityChecks;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.CreateLobbyMessage;
import it.polimi.ingsw.view.messages.JoinLobbyMessage;
import it.polimi.ingsw.view.messages.Move;
import it.polimi.ingsw.view.messages.RecoverLobbyMessage;

import java.util.*;
import java.util.function.Predicate;

public class TUI extends View {
    
    
    private final Object loginLock = new Object();
    private final Queue<Response> responseList = new LinkedList<>();
    
    private final Object lobbyLock = new Object();
    
    protected boolean newLobbies = false;
    
    private Boolean flag = false;
    
    private final Object playerLock = new Object();
    
    @Override
    public void run() {
        userLogin();
        
        //noinspection InfiniteLoopStatement
        
        
        while( true ) {
            synchronized(playerLock) {
                try {
                    playerLock.wait();
                    
                    
                }
                catch( InterruptedException e ) {
                    throw new RuntimeException(e);
                }
            }
            
            //questo codice si esegue una volta sola quando è il tuo turno
            var sel = askSelection();
            int c = askColumn();
            //notifyMove(new Move());
        }
    }
    
    private void userLogin() {
        Scanner scanner = new Scanner(System.in);
        
        // fetch all lobbies
        notifyRequestLobby(null);
        
        waitLobbies();
        
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
                    
                    notifyCreateLobby(lobbySize);
                    Response r = waitLoginResponse(CreateLobbyMessage.class.getSimpleName());
                    if( r.isOk() ) {
                        break label;
                    }else if( r.msg().equals("NicknameTaken") ) {
                        System.out.println("Your nickname has been taken!. Please choose another one");
                        askNickname();
                    }
                }
                
                case "JOIN" -> {
                    notifyRequestLobby(null);
                    waitLobbies();
                    if( lobbies.stream().noneMatch((l) -> l.nicknames().size() < l.lobbySize()) ) {
                        System.out.println("\nNo lobbies are currently available, please create a new one");
                        continue;
                    }
                    
                    System.out.println("\nChoose one of the following lobbies (avoid ones that are full): ");
                    lobbies.forEach(System.out::print);
                    while( true ) {
                        System.out.print("\n>>  ");
                        try {
                            int lobbyId = Integer.parseInt(scanner.next());
                            if( lobbies.stream().anyMatch(
                                    (l) -> l.lobbyID() == lobbyId && l.nicknames().size() < l.lobbySize()) ) {
                                notifyJoinLobby(lobbyId);
                                Response r = waitLoginResponse(JoinLobbyMessage.class.getSimpleName());
                                if( r.isOk() ) {
                                    break label;
                                }else if( r.msg().equals("NicknameTaken") ) {
                                    System.out.println("Your nickname has been taken!. Please choose another one");
                                    askNickname();
                                    
                                }else if( r.msg().equals("LobbyUnvailable") ) {
                                    System.out.println("Please choose a viable lobby!");
                                }
                                break;
                            }else {
                                System.out.println("Please select a valid lobby identifier");
                            }
                        }
                        catch( NumberFormatException e ) {
                            System.out.println("Please write a number");
                        }
                    }
                }
                
                case "RECOVER" -> {
                    notifyRecoverLobby();
                    Response r = waitLoginResponse(RecoverLobbyMessage.class.getSimpleName());
                    if( r.isOk() ) {
                        break label;
                    }else if( r.msg().equals("LobbyUnavailable") ) {
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
                this.notifyDebugMessage("Turn Passed");
                break;
            }
        }
    }
    
    //TODO add tile order selection
    public List<Coordinate> askSelection() {
        
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
    
    
    private Response waitLoginResponse(String action) {
        synchronized(loginLock) {
            try {
                while( responseList.peek() == null || !responseList.peek().Action().matches(action) ) {
                    loginLock.wait();
                }
                return responseList.poll();
            }
            catch( InterruptedException e ) {
                throw new RuntimeException("received interrupted exception in waitLoginResponse", e);
            }
        }
    }
    
    private void waitLobbies() {
        synchronized(lobbyLock) {
            try {
                while( !newLobbies ) {
                    lobbyLock.wait();
                }
                newLobbies = false;
            }
            catch( InterruptedException e ) {
                throw new RuntimeException("received interrupted exception in waitLobbies", e);
            }
        }
    }
    
    
    @SuppressWarnings("unused")
    @Override
    public void onMessage(BoardMessage msg) {
        this.model.setBoard(msg.getPayload());
        String s = TUIUtils.printBoard(this.model.getBoard());
        System.out.println(s);
        
    }
    
    @SuppressWarnings("unused")
    @Override
    public void onMessage(AvailableLobbyMessage msg) {
        this.lobbies = msg.getPayload().lobbyViewList();
        synchronized(lobbyLock) {
            newLobbies = true;
            lobbyLock.notifyAll();
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
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(ServerResponseMessage msg) {
        Predicate<String> isLoginMessage = x -> {
            String action = msg.getPayload().Action();
            return action.matches("RecoverLobbyMessage|RequestLobbyMessage|JoinLobbyMessage|CreateLobbyMessage");
        };
        
        if( isLoginMessage.test(msg.getPayload().Action()) ) {
            synchronized(loginLock) {
                responseList.add(msg.getPayload());
                loginLock.notifyAll();
            }
        }else {
            //FIXME
            responseList.add(msg.getPayload());
        }
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(ShelfMessage msg) {
        this.model.setShelves(msg.getPayload(), msg.getPlayer());
        
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(IncomingChatMessage msg) {
        //TODO
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(UpdateScoreMessage msg) {
        //TODO
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(CommonGoalMessage msg) {
        //TODO
    }
    
    /**
     * @param msg
     */
    @Override
    public void onMessage(CurrentPlayerMessage msg) {
        this.model.setCurrentPlayer(msg.getPayload());
        synchronized(playerLock) {
            //lock che viene notificato quando è il tuo turno
            if( msg.getPayload().equals(this.nickname) ) {
                playerLock.notifyAll();
            }
        }
    }
    
}
