package it.polimi.ingsw.model;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
public class GameModelTest {
    @Test
    public void addPlayerTest() {
        int numPlayers = 4;
        var game = new GameModel(numPlayers, 0, 0);
        for (int i=0; i < numPlayers; i++) {
            var player=new Player("nick_"+i,i);
            game.addPlayer("nick_"+i,i);






            if (!(player.getNickname().equals(game.getPlayers().get(i).getNickname())&&player.getPg()==game.getPlayers().get(i).getPg())){
                assertTrue(false);
           }

          //  assertTrue(player.equals(game.getPlayers().get(i)) );


        }
    }
    @Test
    public void getOccupiedTest(){
        var game = new GameModel(4, 0, 0);
        var occupied= game.getOccupied();
        var board= game.getAllCoordinates();
        for(int i=0;i< board.size();i++){
            if(!(game.getTile(board.get(i)).equals(Tile.NOTILE))){
                assertTrue(occupied.contains(board.get(i)));
            }

        }


    }



}
