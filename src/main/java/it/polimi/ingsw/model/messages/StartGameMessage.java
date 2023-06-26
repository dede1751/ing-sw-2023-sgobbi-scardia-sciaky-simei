package it.polimi.ingsw.model.messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;

import java.io.Serializable;
import java.util.List;

/**
 * Message notifying clients that the game has started. <br>
 * Contains the initial state of the game. This is needed because the game can be resumed after a server crash. <br>
 * Each StartGameMessage is unique for each client, because it contains the personal goal of the client.
 */
public class StartGameMessage extends ModelMessage<StartGameMessage.StartGamePayload> {
    
    /**
     * Record containing the information of a player. <br>
     * Contains the nickname, the shelf and the scores of the player.
     *
     * @param nickname The nickname of the player
     * @param shelf The shelf of the player
     * @param commonGoalScore The score of the player on the common goals
     * @param personalGoalScore The score of the player on the personal goal
     * @param adjacencyScore The adjacency score of the player
     * @param bonusScore The bonus score of the player
     */
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
    
    /**
     * Specific payload of the StartGameMessage.
     * @param board The board
     * @param players The list of players playing
     * @param currentPlayer The nickname of the current player
     * @param personalGoalId The id of the personal goal of the client receiving the message
     * @param CGXIndex The index of the common goal X
     * @param topCGXScore The score on the top of the stack of the common goal X
     * @param CGYIndex The index of the common goal Y
     * @param topCGYScore The score on the top of the stack of the common goal Y
     */
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
    
    /**
     * Initialize a new StartGameMessage object with the given parameters
     * @param board The board
     * @param players The list of players playing
     * @param currentPlayer The nickname of the current player
     * @param personalGoalId The id of the personal goal of the client receiving the message
     * @param CGXIndex The index of the common goal X
     * @param topCGXScore The score on the top of the stack of the common goal X
     * @param CGYIndex The index of the common goal Y
     * @param topCGYScore The score on the top of the stack of the common goal Y
     */
    public StartGameMessage(
            Board board,
            List<Player> players,
            String currentPlayer,
            Integer personalGoalId,
            int CGXIndex, int topCGXScore,
            int CGYIndex, int topCGYScore
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
