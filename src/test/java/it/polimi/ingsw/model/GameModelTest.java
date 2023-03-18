package it.polimi.ingsw.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests {@link GameModel}
 */
public class GameModelTest {
    @Test
    public void addPlayerTest() {
        int numPlayers = 4;
        var game = new GameModel(numPlayers, 0, 0);
        for (int i=0; i < numPlayers; i++) {
            var player=new Player("nick_"+i,i);
            game.addPlayer("nick_"+i,i);

            if (!(player.getNickname().equals(game.getPlayers().get(i).getNickname())&&player.getPg()==game.getPlayers().get(i).getPg())){
                fail();
            }

          //  assertTrue(player.equals(game.getPlayers().get(i)) );


        }
    }
    @Test
    public void getOccupiedTest(){
        var game = new GameModel(4, 0, 0);
        var occupied= game.getOccupied();
        var board= game.getAllCoordinates();
        for (Coordinate coordinate : board) {
            if (!(game.getTile(coordinate).equals(Tile.NOTILE))) {
                assertTrue(occupied.contains(coordinate));
            }
        }
    }
}
