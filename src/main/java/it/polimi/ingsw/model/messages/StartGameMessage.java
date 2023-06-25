package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;

import java.io.Serializable;
import java.util.List;

public class StartGameMessage extends ModelMessage<StartGameMessage.StartGamePayload> {
    
    public record PlayerRecord(
            String nickname,
            Shelf shelf,
            int commonGoalScore,
            int personalGoalScore,
            int adjacencyScore,
            int bonusScore
    ) implements Serializable {
        private static PlayerRecord fromPlayer(Player p) {
            return new PlayerRecord(
                    p.getNickname(),
                    p.getShelf(),
                    p.getCommonGoalScore(),
                    p.getPersonalGoalScore(),
                    p.getAdjacencyScore(),
                    p.getBonusScore());
        }
    }
    
    public record StartGamePayload(
            Board board,
            List<PlayerRecord> players,
            String currentPlayer,
            Integer personalGoalId,
            int CGXIndex, int topCGXScore,
            int CGYIndex, int topCGYScore
    ) implements Serializable {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Game started | Players : ");
            for( var x : players() ) {
                sb.append(x.nickname).append(" ");
            }
            return sb.toString();
        }
    }
    
    public StartGameMessage(
            List<Player> players,
            Integer personalGoalId,
            Board board,
            int CGXIndex, int topCGXScore,
            int CGYIndex, int topCGYScore,
            String currentPlayer
    ) {
        super(
                new StartGamePayload(
                        board,
                        players.stream().map(PlayerRecord::fromPlayer).toList(),
                        currentPlayer,
                        personalGoalId,
                        CGXIndex, topCGXScore,
                        CGYIndex, topCGYScore));
    }
    
}
