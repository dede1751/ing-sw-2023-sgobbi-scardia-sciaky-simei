package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.GameModel;
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
    
    private final Queue<Response> responseList = new LinkedList<>();
    
    private final Object loginLock = new Object();
    
    private final Object lobbyLock = new Object();
    protected boolean newLobbies = false;
    
    
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        userLogin();
        
        // wait for the game to start before allowing user input
        model.waitStart();
        
        //noinspection InfiniteLoopStatement
        while( true ) {
            System.out.print("Please select the action you want to take: [MOVE/CHAT]\n>> ");
            String command = scanner.next().trim();
            
            switch( command ) {
                case "MOVE" -> {
                    if( model.getCurrentPlayer().equals(this.nickname) ) {
                        List<Coordinate> selection = askSelection();
                        List<Tile> tiles = askSelectionOrder(selection);
                        int column = askColumn(tiles);
                        
                        Move move = new Move(selection, tiles, column);
                        notifyMove(move);
                        System.out.println("Move sent");
                    }else {
                        System.out.println("Please, wait for your turn!");
                    }
                }
                
                case "CHAT" -> System.out.println("To be implemented"); //TODO
            }
        }
    }
    
    /**
     * Take care of user login to a lobby on the server
     * This will set up the player's nickname and sign them up to an active lobby
     */
    private void userLogin() {
        Scanner scanner = new Scanner(System.in);
        
        askNickname();
        
        main_loop:
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
                        break main_loop;
                    }else if( r.msg().equals("NicknameTaken") ) {
                        System.out.println("Your nickname has been taken!. Please choose another one");
                        askNickname();
                    }
                }
                
                case "JOIN" -> {
                    // fetch all lobbies from the server
                    notifyRequestLobby(null);
                    waitLobbies();
                    if( lobbies.stream().noneMatch((l) -> l.nicknames().size() < l.lobbySize()) ) {
                        System.out.println("\nNo lobbies are currently available, please create a new one");
                        continue;
                    }
                    
                    System.out.println("\nChoose one of the following lobbies (avoid ones that are full): ");
                    lobbies.forEach(System.out::print);
                    
                    // ask the user to select a lobby
                    while( true ) {
                        System.out.print("\n>>  ");
                        try {
                            int lobbyId = Integer.parseInt(scanner.next());
                            boolean valid_lobby = lobbies.stream()
                                    .anyMatch((l) -> l.lobbyID() == lobbyId && l.nicknames().size() < l.lobbySize());
                            
                            if( valid_lobby ) {
                                notifyJoinLobby(lobbyId);
                                Response r = waitLoginResponse(JoinLobbyMessage.class.getSimpleName());
                                
                                if( r.isOk() ) {
                                    break main_loop;
                                }else if( r.msg().equals("NicknameTaken") ) {
                                    System.out.println("Your nickname has been taken!. Please choose another one");
                                    askNickname();
                                }else if( r.msg().equals("LobbyUnvailable") ) {
                                    System.out.println("Please choose a viable lobby!");
                                }
                                
                                // if we encounter an error, let the user choose what login action to take
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
                        break main_loop;
                    }else if( r.msg().equals("LobbyUnavailable") ) {
                        System.out.println("There is no lobby you can recover. Did you misspell your nickname?");
                        askNickname();
                    }
                }
                
                default -> System.out.println("Please choose one of [CREATE/JOIN/RECOVER]");
            }
        }
        
        if( !model.isStarted() ) {
            System.out.println("\nSuccesfully logged in to server. Awaiting game start... ");
        }
    }
    
    /**
     * Ask the user for a nickname, providing the list of all active lobbies fetched from the server.
     * Does not allow choice of nicknames already present in lobby list.
     */
    private void askNickname() {
        notifyRequestLobby(null);
        waitLobbies();
        
        if( !lobbies.isEmpty() ) {
            System.out.println(
                    "\nHere are all the currently available lobbies. To recover crashed lobbies, user your old nickname!");
            lobbies.forEach(System.out::print);
        }else {
            System.out.println("\nThere is currently no active lobby!");
        }
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nChoose the nickname you'll be using in game: (avoid those present in other lobbies)");
        
        while( true ) {
            System.out.print("\n>>  ");
            String nickname = scanner.next().trim();
            
            if( !nickname.equals("")
                && lobbies.stream().noneMatch((l) -> l.nicknames().contains(nickname))
            ) {
                this.setNickname(nickname);
                break;
            }
        }
    }
    
    /**
     * Ask the user to select a list of tiles from the board.
     * @return a list of coordinates representing the tiles selected by the user
     */
    private List<Coordinate> askSelection() {
        Scanner scanner = new Scanner(System.in);
        List<Coordinate> selection = null;
        
        while ( !IntegrityChecks.checkSelectionForm(selection, model.getBoard(), model.getShelf(this.nickname)) ) {
            if ( selection != null ) {
                System.out.print("\nThe coordinates you entered are not valid, please try again: \n>> ");
            } else {
                System.out.print("\nEnter the ROW,COL coordinates of the tiles you want to select (e.g. 1,2 2,2): \n>> ");
            }
            
            // try to parse selection
            selection = Arrays.stream(
                    scanner.nextLine()
                            .split(" "))
                            .map((s) -> {
                                String[] coordinates = s.split(",");
                                try {
                                    return new Coordinate(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
                                } catch (Exception e) {
                                    return new Coordinate(-1, -1); // this will invalidate the selection
                                }
                            }).toList();
        }
        
        return selection;
    }
    
    /**
     * Ask the user to pick the order in which the tiles get put on the shelf.
     * @param selection the list of coordinates already selected by the player
     * @return the ordered list of tiles at those coordinates picked by the player
     */
    private List<Tile> askSelectionOrder(List<Coordinate> selection) {
        Scanner scanner = new Scanner(System.in);
        List<Tile> tiles = null;
        
        while ( !IntegrityChecks.checkTileSelection(selection, tiles, model.getBoard()) ) {
            if ( tiles != null ) {
                System.out.print("\nThe order you specified is not valid, try again: \n>> ");
            } else {
                System.out.print("\nEnter the order you want to insert these tiles in ( e.g. 2 1 3, 2 goes to the bottom ):\n");
                TUIUtils.printSelection(selection);
                System.out.print("\n>> ");
            }
            
            // try to parse selection
            tiles = new LinkedList<>();
            for ( String s : scanner.nextLine().split(" ") ) {
                try {
                    tiles.add(
                            model.getBoard().getTile(selection.get(Integer.parseInt(s) - 1))
                    );
                } catch (Exception e) {
                    tiles.add(Tile.NOTILE); // this will invalidate the selection
                }
            }
        }
        
        return tiles;
    }
    
    /**
     * Ask the user to pick a column in the shelf to put the tiles in.
     * @param tiles the list of tiles to be put on the shelf
     * @return the column number picked by the user
     */
    public int askColumn(List<Tile> tiles) {
        Scanner scanner = new Scanner(System.in);
        int column = -1;
        
        while ( !IntegrityChecks.checkColumnValidity(tiles, column, model.getShelf(this.nickname)) ) {
            if ( column != -1 ) {
                System.out.print("\nThe column you specified cannot be filled, choose another one: \n>> ");
            } else {
                System.out.print("\nEnter the number of the column you want to insert your tiles into: [0,1,2,3,4]\n>> ");
            }
            
            // try to parse column
            try {
                column = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                column = -1; // this will invalidate the selection
            }
        }
        
        return column;
    }
    
    /**
     * Wait until a response for the given action is received
     *
     * @param action name of the action to wait for
     *
     * @return the response received
     */
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
    
    /**
     * Wait until the lobby list is updated by the server
     */
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
    
    /**
     * Respond to a BoardMessage, receiving a board update from the server
     *
     * @param msg the message received
     */
    @SuppressWarnings("unused")
    @Override
    public void onMessage(BoardMessage msg) {
        this.model.setBoard(msg.getPayload());
    }
    
    /**
     * Respond to an AvailableLobbyMessage, updating the list of available lobbies.
     * This frees up the newLobby lock, allowing the client to read the updated lobbies.
     *
     * @param msg the message received
     */
    @SuppressWarnings("unused")
    @Override
    public void onMessage(AvailableLobbyMessage msg) {
        this.lobbies = msg.getPayload().lobbyViewList();
        synchronized(lobbyLock) {
            newLobbies = true;
            lobbyLock.notifyAll();
        }
    }
    
    /**
     * Respond to an EndGameMessage, printing the leaderboard and terminating the game.
     *
     * @param msg the message received
     */
    @SuppressWarnings("unused")
    @Override
    public void onMessage(EndGameMessage msg) {
        var p = msg.getPayload();
        System.out.println("GAME FINISHED, THE WINNER IS " + p.winner());
        System.out.println("LEADERBOARD : ");
        for( var x : p.points().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).toList() ) {
            System.out.println(x.getKey() + " : " + x.getValue());
        }
        
        model.setStarted(false);
    }
    
    /**
     * Respond to a StartGameMessage, setting up the model's initial state and starting the game.
     *
     * @param msg the message received
     */
    @SuppressWarnings("unused")
    @Override
    public void onMessage(StartGameMessage msg) {
        var payload = msg.getPayload();
        
        model.setPlayersNicknames(payload.nicknames());
        
        model.setPgid(payload.personalGoalId());
        model.setCGXindex(payload.CGXIndex());
        model.setTopCGXscore(payload.topCGXScore());
        model.setCGYindex(payload.CGYIndex());
        model.setTopCGYscore(payload.topCGYScore());
        
        model.setBoard(payload.board());
        model.setCurrentPlayer(payload.currentPlayer());
        for( int i = 0; i < payload.nicknames().size(); i++ ) {
            model.setShelf(payload.shelves().get(i), payload.nicknames().get(i));
        }
        
        TUIUtils.printStartGame();
        TUIUtils.printGame(nickname);
        model.setStarted(true);
    }
    
    /**
     * Responde to a ServerResponseMessage.
     * These messages are received as a response to various requests to the server.
     * Messages involving registration always receive a response, while during the game one is received only for errors.
     *
     * @param msg the message received
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
     * Respond to a ShelfMessage, updating the current player's shelf.
     *
     * @param msg the message received
     */
    @Override
    public void onMessage(ShelfMessage msg) {
        this.model.setShelf(msg.getPayload(), msg.getPlayer());
    }
    
    /**
     * Respond to an incoming chat message
     *
     * @param msg the message received
     */
    @Override
    public void onMessage(IncomingChatMessage msg) {
        this.model.addChatMessage(msg.getSender(), msg.getPayload());
        if( this.model.isStarted() ) {
            TUIUtils.printGame(nickname);
        }
    }
    
    /**
     * Respond to an UpdateScoreMessage, providing score updates at the end of the turn.
     *
     * @param msg the message received
     */
    @Override
    public void onMessage(UpdateScoreMessage msg) {
        this.model.setPoints(msg.getPayload().type(), msg.getPayload().player(), msg.getPayload().score());
    }
    
    /**
     * Respond to a CommonGoalMessage, updating the score of the common goal stacks.
     *
     * @param msg the message received
     */
    @Override
    public void onMessage(CommonGoalMessage msg) {
        if( msg.getPayload().type() == GameModel.CGType.Y ) {
            this.model.setTopCGYscore(msg.getPayload().availableTopScore());
        }else {
            this.model.setTopCGXscore(msg.getPayload().availableTopScore());
        }
    }
    
    /**
     * Respond to a CurrentPlayerMessage, updating the current player
     *
     * @param msg the message received
     */
    @Override
    public void onMessage(CurrentPlayerMessage msg) {
        this.model.setCurrentPlayer(msg.getPayload());
        
        if( this.model.isStarted() ) {
            TUIUtils.printGame(nickname);
        }
    }
    
}
