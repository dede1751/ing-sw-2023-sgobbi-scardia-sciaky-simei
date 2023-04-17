package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.goals.common.CommonGoal;
import it.polimi.ingsw.model.goals.personal.PersonalGoal;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewMessage;

import java.util.*;


public class GameController {
    
    private final GameModel model;
    
    private final List<Client> clients;
    private Integer currentPlayerIndex;
    private final Integer playerNumber;
    
    
    public GameController(GameModel model, List<Client> clients) {
        this.model = model;
        this.clients = clients;
        playerNumber = clients.size();
        currentPlayerIndex = 0;
    }
    
    public Boolean needRefill() {
        Map<Coordinate, Tile> toBeChecked = model.getBoard().getTiles();
        
        for( var entry : toBeChecked.entrySet() ) {
            if( !(entry.getValue().equals(Tile.NOTILE)) ) {
                if( !(model.getBoard().getTile(entry.getKey().getDown()) == Tile.NOTILE) ) {
                    return false;
                }
                if( !(model.getBoard().getTile(entry.getKey().getUp()) == Tile.NOTILE) ) {
                    return false;
                }
                if( !(model.getBoard().getTile(entry.getKey().getLeft()) == Tile.NOTILE) ) {
                    return false;
                }
                if( !(model.getBoard().getTile(entry.getKey().getRight()) == Tile.NOTILE) ) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public int calculateAdjacency(Shelf shelf) {
        
        record Coord(int r, int c) {
            Coord sum(Coord offset) {
                return new Coord(r + offset.r, c + offset.c);
            }
            
            Coord sub(Coord offset) {
                return new Coord(r - offset.r, c - offset.c);
            }
            
            List<Coord> sumList(List<Coord> offset) {
                return offset.stream().map((x) -> x.sum(this)).toList();
            }
            
            List<Coord> sumDir() {
                return this.sumList(List.of(new Coord(-1, 0), new Coord(1, 0), new Coord(0, -1), new Coord(0, 1)));
            }
            
        }
        
        var mat = shelf.getAllShelf();
        var checked = new boolean[Shelf.N_ROW][Shelf.N_COL];
        int adjacentScore = 0;
        for( int i = 0; i < Shelf.N_ROW; i++ ) {
            for( int j = 0; j < Shelf.N_COL; j++ ) {
                Tile.Type type = mat[i][j].type();
                if( checked[i][j] || type == Tile.Type.NOTILE )
                    continue;
                
                var current = new Coord(i, j);
                
                //select chunck
                List<Coord> selected = new ArrayList<>();
                Queue<Coord> visited = new LinkedList<>();
                visited.add(current);
                checked[current.r()][current.c()] = true;
                while( !visited.isEmpty() ) {
                    current = visited.poll();
                    selected.add(current);
                    current.sumDir().stream()
                            .filter((x) -> x.r() < Shelf.N_ROW &&
                                           x.r() > -1 &&
                                           x.c() < Shelf.N_COL &&
                                           x.c() > -1 &&
                                           mat[x.r()][x.c()].type() == type &&
                                           !checked[x.r()][x.c()])
                            .forEach((x) -> {
                                visited.add(x);
                                checked[x.r()][x.c()] = true;
                            });
                }
                
                
                if( selected.size() == 3 ) {
                    adjacentScore += 2;
                }else if( selected.size() == 4 ) {
                    adjacentScore += 3;
                }else if( selected.size() == 5 ) {
                    adjacentScore += 5;
                }else if( selected.size() > 6 ) {
                    adjacentScore += 8;
                }
                
                
            }
        }
        return adjacentScore;
    }
    
    
    public void turnManager() {
        
        //reference to the current player
        Player currentPlayer = model.getCurrentPlayer();
        
        if( !model.getCurrentPlayer().isCompletedGoalX() &&
            CommonGoal.getCommonGoal(model.getCommonGoalX()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerCommongGoalScore(model.popStackCGX());
            model.getCurrentPlayer().setCompletedGoalX(true);
        }
        if( !model.getCurrentPlayer().isCompletedGoalY() &&
            CommonGoal.getCommonGoal(model.getCommonGoalY()).checkGoal(currentPlayer.getShelf()) ) {
            model.addCurrentPlayerCommongGoalScore(model.popStackCGY());
            model.getCurrentPlayer().setCompletedGoalY(true);
        }
        
        //calculate and set in every turn the personalGoalScore
        currentPlayer.setPersonalGoalScore(
                PersonalGoal.getPersonalGoal(currentPlayer.getPg()).checkGoal(currentPlayer.getShelf()));
        
        //calculate and set in every turn the adjacentTiles and score
        currentPlayer.setAdjacentScore(calculateAdjacency(currentPlayer.getShelf()));
        
        if( needRefill() ) {
            model.getBoard().refill(model.getTileBag());
        }
        
        if( currentPlayer.getShelf().isFull() && !model.isFinalTurn() ) {
            model.setLastTurn();
        }
    }
    
    public void nextPlayerSetter() {
        if( model.isFinalTurn() && model.getCurrentPlayerIndex() == 3 ) {
            endGame();
            //check if the current player is the last in the list of players, if it is, set current player to the first in the list
        }else {
            currentPlayerIndex = (++currentPlayerIndex) % playerNumber;
            model.setCurrentPlayerIndex(currentPlayerIndex);
        }
    }
    
    public void endGame() {
        int winnerIndex = 0;
        
        for( int i = 0; i < model.getNumPlayers() - 1; i++ ) {
            if( model.getPlayers().get(i).getScore() > model.getPlayers().get(winnerIndex).getScore() ) {
                winnerIndex = i;
            }
        }
        
        model.setWinner(model.getPlayers().get(winnerIndex).getNickname());
    }
    
    //TODO change name to ViewMessage
    public void update(ViewMessage o, View.Action evt) {
        int currentPlayerIndex = model.getCurrentPlayerIndex();
        if( o.getViewID() != currentPlayerIndex ) {
            System.err.println("Ignoring event from view:" + o.getViewID() + ": " + evt + ". Not the current Player.");
            return;
        }
        switch( evt ) {
            case MOVE -> {
                model.removeSelection(o.getSelection());
                model.shelveSelection(o.getTiles(), o.getColumn());
                turnManager();
                nextPlayerSetter();
            }
            case CHAT -> {
                //TODO chat functions;
            }
            case PASS_TURN -> {
                //FIXME only for debug porpouses, to be removed in final version
                System.out.println("Player " + currentPlayerIndex + " passed his turn.");
                nextPlayerSetter();
            }
        }
    }
}
