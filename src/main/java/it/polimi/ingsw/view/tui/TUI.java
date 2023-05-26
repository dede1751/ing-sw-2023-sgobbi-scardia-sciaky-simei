package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.model.Coordinate;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.messages.*;
import it.polimi.ingsw.utils.mvc.IntegrityChecks;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.messages.*;

import java.util.*;
import java.util.function.Predicate;


public class TUI extends View {
    
    private final Queue<Response> responseList = new LinkedList<>();
    
    private final Object loginLock = new Object();
    
    private final Object lobbyLock = new Object();
    protected boolean newLobbies = false;
    
    private String prompt = null;
    private String error = null;
    
    /**
     * Runs the main TUI thread.
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        userLogin();
        
        // wait for the game to start before allowing user input
        model.waitStart();
        
        // noinspection InfiniteLoopStatement
        while( true ) {
            prompt = "Please select the action you want to take: [MOVE/CHAT]";
            TUIUtils.printGame(nickname, prompt, error);
            
            switch( scanner.next().trim().toUpperCase() ) {
                case "MOVE" -> {
                    if( model.getCurrentPlayer().equals(this.nickname) ) {
                        List<Coordinate> selection = askSelection();
                        List<Tile> tiles = askSelectionOrder(selection);
                        int column = askColumn(tiles);
                        
                        Move move = new Move(selection, tiles, column);
                        notifyMove(move);
                        
                        error = null;
                    }else {
                        error = "Please, wait for your turn to make moves!";
                    }
                }
                
                case "CHAT" -> {
                    String choice = askMsgType();
                    if (choice.equals("Y")) {
                        askBroadcastMessage();
                    } else {
                        askPrivateMessage();
                    }
                }
                
                default -> error = "Invalid command!";
            }
        }
    }
    
    /**
     * Ask the user what type of chat message they would like to send
     * @return "Y" for broadcast message, "N" for private message
     */
    private String askMsgType() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        
        error = null;
        while( true ) {
            prompt = "Do you want to send a broadcast message? [Y/N]";
            TUIUtils.printGame(nickname, prompt, error);
    
            choice = scanner.next().trim().toUpperCase();
            if( choice.equals("Y") || choice.equals("N") ) {
                break;
            }else {
                error = "Invalid command!";
            }
        }
        
        error = null;
        return choice;
    }
    
    private void askBroadcastMessage() {
        Scanner scanner = new Scanner(System.in);
    
        String msg;
        while( true ) {
            prompt = "Please, enter your message:";
            TUIUtils.printGame(nickname, prompt, error);
        
            msg = scanner.nextLine();
            if( msg.length() > 70 ) {
                error = "Message too long!";
            }else {
                error = null;
                break;
            }
        }
    
        notifyChatMessage(msg);
    }
    
    private void askPrivateMessage() {
        Scanner scanner = new Scanner(System.in);
        
        String player;
        while( true ) {
            prompt = "Enter the nickname of the player you want to send your message:";
            TUIUtils.printGame(nickname, prompt, error);
        
            player = scanner.nextLine().trim();
            if( !model.getPlayersNicknames().contains(player) ) {
                error = "Player does not exist!";
            }else if( player.equals(nickname) ){
                error = "Choose another player's nickname!";
            }else {
                error = null;
                break;
            }
        }
    
        String msg;
        while( true ) {
            prompt = "Please, enter your message:";
            TUIUtils.printGame(nickname, prompt, error);
        
            msg = scanner.nextLine();
            if( msg.length() > 70 ) {
                error = "Message too long!";
            }else {
                break;
            }
        }
        
        notifyChatMessage(msg, player);
    }
    
    
    /**
     * Take care of user login to a lobby on the server
     * This will set up the player's nickname and sign them up to an active lobby
     */
    private void userLogin() {
        Scanner scanner = new Scanner(System.in);
        
        error = null;
        boolean selectedRecovery = askNickname();
        
        main_loop:
        //noinspection LoopConditionNotUpdatedInsideLoop
        while( !selectedRecovery ) {
            prompt = "Do you want to create your own lobby or join an existing one? [CREATE/JOIN]";
            TUIUtils.printLoginScreen(prompt, error);
            
            switch( scanner.next().trim().toUpperCase() ) {
                
                case "CREATE" -> {
                    int lobbySize;
                    
                    error = null;
                    while( true ) {
                        prompt = "Choose the amount of players for the match (2-4): ";
                        TUIUtils.printLoginScreen(prompt, error);
                        
                        try {
                            lobbySize = Integer.parseInt(scanner.next());
                            if( lobbySize < 2 || lobbySize > 4 ) {
                                error = "Please choose a number between 2 and 4";
                            }else {
                                error = null;
                                break;
                            }
                        }
                        catch( NumberFormatException e ) {
                            error = "Please write a number";
                        }
                    }
                    
                    notifyCreateLobby(lobbySize);
                    Response r = waitLoginResponse(CreateLobbyMessage.class.getSimpleName());
                    
                    if( r.isOk() ) {
                        break main_loop;
                    }else if( r.msg().equals("NicknameTaken") ) {
                        error = "Your nickname has been taken!";
                        askNickname();
                    }
                }
                
                case "JOIN" -> {
                    // fetch all lobbies from the server
                    notifyRequestLobby(null);
                    waitLobbies();
                    
                    if( lobbies.stream().allMatch(LobbyController.LobbyView::isFull) ) {
                        error = "No lobbies are currently available!";
                        continue;
                    } else {
                        error = null;
                    }
                    
                    StringBuilder joinPrompt =
                            new StringBuilder("Choose one of the following lobbies:");
                    for( var l : lobbies ) {
                        if ( !l.isRecovery() && !l.isFull()) {
                            joinPrompt.append("\n").append(l);
                        }
                    }
                    
                    // ask the user to select a lobby
                    while( true ) {
                        prompt = joinPrompt.toString();
                        TUIUtils.printLoginScreen(prompt, error);
                        
                        try {
                            int lobbyId = Integer.parseInt(scanner.next());
                            boolean valid_lobby = lobbies.stream()
                                    .anyMatch((l) -> l.lobbyID() == lobbyId && !l.isFull());
                            
                            if( valid_lobby ) {
                                notifyJoinLobby(lobbyId);
                                Response r = waitLoginResponse(JoinLobbyMessage.class.getSimpleName());
                                
                                if( r.isOk() ) {
                                    error = null;
                                    break main_loop;
                                }else if( r.msg().equals("NicknameTaken") ) {
                                    error = "Your nickname has been taken!";
                                    askNickname();
                                }else if( r.msg().equals("LobbyUnvailable") ) {
                                    error = "Lobby is not available!";
                                }
                                
                                // if we encounter an error, let the user choose what login action to take
                                break;
                            }else {
                                error = "Invalid lobby ID!";
                            }
                        }
                        catch( NumberFormatException e ) {
                            error = "Please write a number!";
                        }
                    }
                }
                
                default -> error = "Please choose one of [CREATE/JOIN]";
            }
        }
        
        if( !model.isStarted() ) {
            TUIUtils.printLoginScreen("Succesfully logged in to server. Awaiting game start... ", null);
        }
    }
    
    /**
     * Ask the user for a nickname, providing the list of all active lobbies fetched from the server.
     * Does not allow choice of nicknames already present in lobby list.
     * If a player requests a nickname from a recovery lobby, send the recovery request.
     *
     * @return true if the user selected a recovery lobby, false otherwise
     */
    private boolean askNickname() {
        notifyRequestLobby(null);
        waitLobbies();
        
        StringBuilder lobbyPrompt = new StringBuilder();
        if( !lobbies.isEmpty() ) {
            lobbyPrompt.append(
                    "\nHere are all the currently available lobbies. To recover crashed lobbies, use your old nickname!");
            for( var l : lobbies ) {
                lobbyPrompt.append("\n").append(l.toString());
            }
        }else {
            lobbyPrompt.append("\nThere is currently no active lobby!");
        }
        
        Scanner scanner = new Scanner(System.in);
        prompt = "Choose the nickname you'll be using in game (avoid those present in other lobbies):" + lobbyPrompt;
        while( true ) {
            TUIUtils.printLoginScreen(prompt, error);
            String nickname = scanner.next().trim();
            
            if( !nickname.equals("") ) {
                // Nickname is already taken in a non-recovery lobby
                if( lobbies.stream().anyMatch((l) -> !l.isRecovery() && l.nicknames().contains(nickname)) ) {
                    error = "Nickname already taken!";
                    continue;
                }
                
                this.setNickname(nickname);
                
                // nickname matches a recovery lobby, try connecting to it
                if( lobbies.stream().anyMatch((l) -> l.isRecovery() && l.nicknames().contains(nickname)) ) {
                    notifyRecoverLobby();
                    Response r = waitLoginResponse(RecoverLobbyMessage.class.getSimpleName());
                    
                    if( r.isOk() ) {
                        error = null;
                        return true;
                    }else if( r.msg().equals("LobbyUnavailable") ) {
                        error = "The lobby you are trying to recover is unavailable!";
                    }else if( r.msg().equals("NicknameTaken") ) {
                        error = "Somebody has already picked that recovery nickname!";
                    }else {
                        error = "Recovery request failed!";
                    }
                }else {
                    error = null;
                    return false;
                }
            } else {
                error = "Please choose a valid nickname!";
            }
        }
    }
    
    
    /**
     * Ask the user to select a list of tiles from the board.
     *
     * @return a list of coordinates representing the tiles selected by the user
     */
    private List<Coordinate> askSelection() {
        Scanner scanner = new Scanner(System.in);
        List<Coordinate> selection = null;
        
        while( !IntegrityChecks.checkSelectionForm(selection, model.getBoard(), model.getShelf(this.nickname)) ) {
            if( selection != null ) {
                error = "Coordinates are not valid";
            }
            prompt = "Enter the ROW,COL coordinates of the tiles you want to select (e.g. 1,2 2,2):";
            TUIUtils.printGame(nickname, prompt, error);
            
            // try to parse selection
            selection = Arrays.stream(
                            scanner.nextLine()
                                    .split(" "))
                    .map((s) -> {
                        String[] coordinates = s.split(",");
                        try {
                            return new Coordinate(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
                        }
                        catch( Exception e ) {
                            return new Coordinate(-1, -1); // this will invalidate the selection
                        }
                    }).toList();
        }
        
        error = null;
        return selection;
    }
    
    /**
     * Ask the user to pick the order in which the tiles get put on the shelf.
     *
     * @param selection the list of coordinates already selected by the player
     *
     * @return the ordered list of tiles at those coordinates picked by the player
     */
    private List<Tile> askSelectionOrder(List<Coordinate> selection) {
        Scanner scanner = new Scanner(System.in);
        List<Tile> tiles = null;
        
        while( !IntegrityChecks.checkTileSelection(selection, tiles, model.getBoard()) ) {
            if( tiles != null ) {
                error = "The order specified is invalid!";
            }
            
            prompt = "Enter the order you want to insert these tiles in ( e.g. 2 1 3, 2 goes to the bottom ):\n" +
                     TUIUtils.generateSelection(selection);
            TUIUtils.printGame(nickname, prompt, error);
            
            // try to parse selection
            tiles = new LinkedList<>();
            for( String s : scanner.nextLine().split(" ") ) {
                try {
                    Tile newTile = model.getBoard().getTile(selection.get(Integer.parseInt(s) - 1));
                    tiles.add(newTile);
                    
                }
                catch( Exception e ) {
                    tiles.add(Tile.NOTILE); // this will invalidate the selection
                }
            }
        }
        
        error = null;
        return tiles;
    }
    
    /**
     * Ask the user to pick a column in the shelf to put the tiles in.
     *
     * @param tiles the list of tiles to be put on the shelf
     *
     * @return the column number picked by the user
     */
    public int askColumn(List<Tile> tiles) {
        Scanner scanner = new Scanner(System.in);
        int column = -1;
        
        while( !IntegrityChecks.checkColumnValidity(tiles, column, model.getShelf(this.nickname)) ) {
            if( column != -1 ) {
                error = "The column you specified cannot be filled by this selection!";
            }
            prompt = "Enter the number of the column you want to insert your tiles into: [0,1,2,3,4]\n" +
                     TUIUtils.generateTiles(tiles);
            TUIUtils.printGame(nickname, prompt, error);
            
            // try to parse column
            try {
                column = Integer.parseInt(scanner.nextLine());
            }
            catch( Exception e ) {
                column = -1; // this will invalidate the selection
            }
        }
        
        error = null;
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
        TUIUtils.printEndGameScreen(msg.getPayload(), model.getPlayersNicknames().get(0));
        System.exit(0);
    }
    
    /**
     * Respond to a StartGameMessage, setting up the model's initial state and starting the game.
     *
     * @param msg the message received
     */
    @SuppressWarnings("unused")
    @Override
    public void onMessage(StartGameMessage msg) {
        model.setModel(msg);
        TUIUtils.printGame(nickname, prompt, null);
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
        this.model.addChatMessage(msg.getSender(), msg.getPayload(), msg.getDestination());
        if( this.model.isStarted() ) {
            TUIUtils.printGame(nickname, prompt, error);
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
            TUIUtils.printGame(nickname, prompt, error);
        }
    }
}
