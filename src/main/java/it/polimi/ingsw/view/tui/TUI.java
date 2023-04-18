package it.polimi.ingsw.view.tui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TUI extends View {
    
    private GameModelView model;
    
    @Override
    public void run() {
        userLogin();
        System.out.println("\nSuccesfully logged in to server. Awaiting game start... ");
        
        //noinspection InfiniteLoopStatement
        while( true ) {
            askPassTurn();
            
            //FIXME getviewid nonè ciò che è inteso nel seguente codice
            /*if(this.getViewID()==model.getCurrentPlayerIndex()){
                Player currentPlayer = model.getPlayers().get(model.getCurrentPlayerIndex());
                System.out.println("my score is "+ currentPlayer.getScore());
                printBoard(model.getBoard());
                printShelf(currentPlayer.getShelf());
                askSelection(model);            //set the asked selection to the view message selection
                askColumn(model);
                this.setChangedAndNotifyObservers(Action.MOVE);
            }*/
        }
    }
    
    private void userLogin() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Choose the nickname you'll be using in game:");
        while( true ) {
            System.out.print("\n>>  ");
            String nickname = scanner.next().trim();
            
            if( !nickname.equals("") ) {
                this.setNickname(nickname);
                break;
            }
        }
        
        // ask the user to create or join a lobby
        System.out.println("Do you want to create your own lobby or join an existing one? [CREATE/JOIN]");
        while( true ) {
            System.out.print("\n>>  ");
            String choice = scanner.next().trim();
            
            if( choice.equals("CREATE") ) {
                // ask the user for the number of players in the lobby.
                System.out.println("Choose the amount of players for the match (2-4): ");
                while( true ) {
                    System.out.print("\n>>  ");
                    try {
                        int lobbySize = Integer.parseInt(scanner.next());
                        
                        if( lobbySize >= 2 && lobbySize <= 4 ) {
                            this.setSelectedPlayerCount(lobbySize);
                            break;
                        }
                    } catch( NumberFormatException e ) {
                        System.out.println("Please write a number");
                    }
                }
                this.setChangedAndNotifyObservers(Action.CREATE_LOBBY);
                return;
            } else if ( choice.equals("JOIN") ) {
                this.setChangedAndNotifyObservers(Action.JOIN_LOBBY);
                return;
            } else {
                System.out.println("Please choose one of [CREATE/JOIN]");
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
    
    
    //TODO add tile order selection
    public void askSelection(GameModelView model) {
        
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
        
        System.out.println("Entered coordinates: " + selection);
        this.setSelectedCoordinates(selection);
        
    }
    
    public void askColumn(GameModelView model) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the column in which you want to place your selection: ");
        int column = scanner.nextInt();
        scanner.nextLine();
        this.setColumn(column);
        
        
    }
    
    private Coordinate getCoordinate() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter x-coordinate: ");
        int x = scanner.nextInt();
        
        System.out.print("Enter y-coordinate: ");
        int y = scanner.nextInt();
        return new Coordinate(x, y);
    }
    
    
    //TODO check
    private void printBoard(Board board) {
        Gson gson =new GsonBuilder().registerTypeAdapter(Board.class,new Board.BoardSerializer()).create();
        var x=gson.toJson(board);
        for(var y : x.replace("\"Board\": [", "").split("],")){
            System.out.println(y);
        }
    }
    
    //TODO
    private void printShelf(Shelf shelf) {
    
    }
    
    //TODO
    public Boolean checkSelection(List<Coordinate> selection) {
        return true;
    }
    
    
    public GameModelView getModel() {
        return model;
    }
    
    public void setModel(GameModelView model) {
        this.model = model;
    }
    
    @Override
    public void update(GameModelView model, GameModel.Event evt) {
        switch( evt ) {
            case GAME_START -> System.out.println("The game has started!");
            
            case NEW_CURRENT_PLAYER -> {
                System.out.println("Current player has changed to " + model.getCurrentPlayerIndex() + ". Players: ");
                
                for( Player player : model.getPlayers() ) {
                    System.out.println(player.getNickname() + " " + player.getScore());
                }
                
                /*setModel(model);
                System.out.println();
                
                printBoard(model.getBoard());
                askSelection(model);            //set the asked selection to the view message selection
                askColumn(model);
                //TODO change event
                this.setChangedAndNotifyObservers(Action.MOVE);
                System.out.println();*/
                
            }
            case LAST_TURN -> {
            
            }
            case FINISHED_GAME -> System.out.println("GAME OVER, THE WINNER IS " + model.getWinner());
            default -> System.err.println("Ignoring event from " + model + ": " + evt);
        }
    }
    

}
