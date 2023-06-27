package it.polimi.ingsw.model.messages;

import java.io.Serializable;
import java.util.Map;

/**
 * Message notifying the end of the game, along with the final leaderboard.
 */
public class EndGameMessage extends ModelMessage<EndGameMessage.EndGamePayload> {
    
    /**
     * Specific payload of the EndGameMessage. <br>
     * Contains the winner and the final leaderboard, mapping each player to its score.
     *
     * @param winner The nickname of the winner
     * @param points The final leaderboard
     */
    public record EndGamePayload(String winner, Map<String, Integer> points) implements Serializable {
        @Override
        public String toString() {
            StringBuilder header = new StringBuilder("GAME FINISHED, THE WINNER IS : " + winner() +
                                                     "\n" +
                                                     "LEADERBOARD : \n");
            for( var x : points.entrySet() ) {
                header.append("\t").append(x.getKey()).append(" : ").append(x.getValue()).append("\n");
            }
            return header.toString();
        }
    }
    
    /**
     * Initialize a EndGameMessage object with the payload parameters
     *
     * @param winner The nickname of the winner
     * @param points The final leaderboard
     */
    public EndGameMessage(String winner, Map<String, Integer> points) {
        super(new EndGamePayload(winner, points));
    }
    
}
