package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.view.View;

import java.util.Scanner;

public class TUI extends View {
    
    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.println("--- NEW TURN ---");
            /* Player chooses */
            this.setNickname(askNickname());
        }
    }
    
    private String askNickname() {
        Scanner s = new Scanner(System.in);
        System.out.println("Choose a nickname to add to the game: ");
        
        while (true) {
            String input = s.next();
            
            if (!input.equals("")) {
                return input;
            }
        }
    }
    
    @Override
    public void update(GameView model, GameModel.Event evt) {
        switch (evt) {
            case ADDED_PLAYER -> System.out.println(model.getPlayers());
            case FINISHED_GAME -> System.out.println("GAME OVER");
            default -> System.err.println("Ignoring event from " + model + ": " + evt);
        }
    }
    
}
