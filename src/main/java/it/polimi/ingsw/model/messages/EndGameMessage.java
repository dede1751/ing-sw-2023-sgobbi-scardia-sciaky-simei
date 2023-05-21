package it.polimi.ingsw.model.messages;

import java.io.Serializable;
import java.util.Map;

public class EndGameMessage extends ModelMessage<EndGameMessage.EndGamePayload> {
    
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
    
    public EndGameMessage(String winner, Map<String, Integer> points) {
        super(new EndGamePayload(winner, points));
    }
    
}
