package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;



/*
stati gioco
+initgame (login + inizializzazione board, riceviamo i nomi dei giocatori dalla view) messaggio=player (String nickname)
    -inizzializza il gameModel e il primo giocator
    -waitplayers
    -controlla che i player abbiano nick diversi
    -inizzializza il primo giocatore a currentplayer
    -passa a stato game
    
+game (mossa ricevuta dalla view (list<coordinate>, il controller la invia al model che aggiorna tilebag e board e shelf)  messaggio=mossa (list<coordinate>, list<Tile>, int column)
    -controlla che i giocatori siano connessi (else pausegame)
    -prende la mossa del giocatore corrente
        -modifica board e shelf e updatea il model
    -controlla shelf giocatore
        -if shelf piena setta flag gameover
    -passa a next turn
    
+pauseGame (gli unici messaggi che accetta sono quelli in cui il giocatore torni a giocare)




 */

public class GameController {
    public final GameModel model;
    //public final GameView view;
    private int numPlayers;
    
    
    /* constructor tobe used for paused game*/
    public GameController(GameModel model) {
        this.model = model;
        //this.view=view;
    }
    
    
    /*constructor to be used for new games*/
    public GameController(int numPlayers, String firstPlayerNick) {
        this.numPlayers = numPlayers;
        this.model = new GameModel(numPlayers, 0, 0);
        this.model.addPlayer(firstPlayerNick, 0);
        //this.view=view;
    }
    
    
    public void awaitPlayers() {
        
        int currentPlayer = 0;
        while( currentPlayer != numPlayers ) {
            //    waitForPlayer();
            
            currentPlayer++;
        }
        //startGame();
    }
    
}
